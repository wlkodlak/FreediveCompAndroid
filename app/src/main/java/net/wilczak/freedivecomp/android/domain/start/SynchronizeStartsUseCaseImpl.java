package net.wilczak.freedivecomp.android.domain.start;

import net.wilczak.freedivecomp.android.domain.race.Race;
import net.wilczak.freedivecomp.android.remote.messages.ActualResultDto;
import net.wilczak.freedivecomp.android.remote.messages.StartingListReportDto;
import net.wilczak.freedivecomp.android.remote.messages.StartingListReportEntryDto;
import net.wilczak.freedivecomp.android.remote.remoteservice.RemoteService;
import net.wilczak.freedivecomp.android.remote.remoteservice.RemoteServiceProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.HttpException;

public class SynchronizeStartsUseCaseImpl implements SynchronizeStartsUseCase {
    private static final int STATE_INACTIVE = 0;
    private static final int STATE_ACTIVE = 1;
    private static final int STATE_RESTART_NOW = 2;
    private static final int STATE_RESTART_LATER = 3;

    private final StartsRepository repository;
    private final RemoteServiceProvider remoteServiceProvider;
    private final AtomicInteger state;

    @Inject
    public SynchronizeStartsUseCaseImpl(StartsRepository repository, RemoteServiceProvider remoteServiceProvider) {
        this.repository = repository;
        this.remoteServiceProvider = remoteServiceProvider;
        this.state = new AtomicInteger(STATE_INACTIVE);
    }

    @Override
    public Completable synchronize(Race race) {
        return synchronizeInternal(race)
                .toObservable()
                .share()
                .ignoreElements()
                .doOnSubscribe(consumer -> {
                    while (true) {
                        if (state.compareAndSet(STATE_INACTIVE, STATE_ACTIVE)) break;
                        if (state.compareAndSet(STATE_ACTIVE, STATE_RESTART_NOW)) break;
                    }
                });
    }

    private Single<Integer> synchronizeInternal(Race race) {
        return Single
                .defer(() -> {
                    state.set(STATE_ACTIVE);
                    return repository.getAll(race).firstOrError();
                })
                .flatMap(starts -> synchronizeOneStart(race, new ArrayList<>(starts)))
                .flatMap(newState -> {
                    if (newState == STATE_RESTART_NOW) {
                        return synchronizeInternal(race);
                    } else if (newState == STATE_RESTART_LATER) {
                        return Single
                                .just(1)
                                .delay(5, TimeUnit.SECONDS)
                                .flatMap(i -> synchronizeInternal(race));
                    } else {
                        return updateFromRemote(race).andThen(Single.defer(() -> {
                            if (state.get() == STATE_RESTART_NOW) {
                                return synchronizeInternal(race);
                            } else if (state.get() == STATE_RESTART_LATER) {
                                return Single
                                        .just(1)
                                        .delay(5, TimeUnit.SECONDS)
                                        .flatMap(i -> synchronizeInternal(race));
                            } else {
                                state.set(STATE_INACTIVE);
                                return Single.just(STATE_INACTIVE);
                            }
                        }));
                    }
                });
    }

    private Single<Integer> synchronizeOneStart(Race race, ArrayList<Start> starts) {
        while (starts.size() > 0) {
            Start start = starts.remove(0);
            if (start.getState() == Start.StartState.PENDING) {
                return synchronizePendingStart(race, start)
                        .flatMap(updatedStart -> {
                            if (updatedStart.getState() == Start.StartState.PENDING) {
                                return Single.just(STATE_RESTART_LATER);
                            } else {
                                return synchronizeOneStart(race, starts);
                            }
                        });
            }
        }
        return Single.just(STATE_ACTIVE);
    }

    private Single<Start> synchronizePendingStart(Race race, Start start) {
        return Completable
                .defer(() -> {
                    RemoteService remoteService = remoteServiceProvider.getService(race.getUri());
                    return remoteService.postAthleteResult(
                            race.getRaceId(), start.getAthleteId(),
                            race.getAuthenticationToken(), buildPostResultRequest(start));
                })
                .toSingleDefault(buildCommittedStart(start))
                .onErrorReturn(t -> doesErrorRejectStart(t) ? buildRejectedStart(start) : start)
                .flatMap(updatedStart -> {
                    if (updatedStart == start) {
                        return Single.just(start);
                    } else {
                        return repository
                                .merge(race, start.getReference(), updatedStart)
                                .andThen(Single.just(updatedStart));
                    }
                });
    }

    private ActualResultDto buildPostResultRequest(Start start) {
        return new ActualResultDto()
                .setDisciplineId(start.getDisciplineId())
                .setPerformance(start.getLocalResult().getPerformance())
                .setPenalizations(start.getLocalResult().getPenalizations())
                .setFinalPerformance(start.getLocalResult().getFinalPerformance())
                .setCardResult(start.getLocalResult().getCardResult())
                .setJudgeComment(start.getLocalResult().getJudgeComment());
    }

    private Start buildCommittedStart(Start original) {
        return original
                .mutate()
                .setState(Start.StartState.READY)
                .setLocalResult(null)
                .setConfirmedResult(original.getLocalResult())
                .freeze();
    }

    private boolean doesErrorRejectStart(Throwable t) {
        if (t instanceof HttpException) {
            HttpException httpException = (HttpException) t;
            if (httpException.code() >= 500)
                return false;  // this is server's error, we should retry later
            if (httpException.code() >= 400 && httpException.code() <= 499)
                return true;  // our error, reject result
        }
        return false;
    }

    private Start buildRejectedStart(Start original) {
        return original
                .mutate()
                .setState(Start.StartState.REJECTED)
                .freeze();
    }

    private Completable updateFromRemote(Race race) {
        return Single
                .defer(() -> {
                    RemoteService remoteService = remoteServiceProvider.getService(race.getUri());
                    return remoteService.getReportStartingLane(race.getRaceId(), null);
                })
                .zipWith(repository.getAll(race).first(new ArrayList<>()), this::buildMergedList)
                .flatMapCompletable(updatedStarts -> repository.replaceAll(race, updatedStarts));

    }

    private List<Start> buildMergedList(StartingListReportDto remoteDto, List<Start> localList) {
        HashMap<StartReference, Start> existingMap = new HashMap<>();
        HashSet<StartReference> unusedStarts = new HashSet<>();
        for (Start start : localList) {
            StartReference startReference = start.getReference();
            unusedStarts.add(startReference);
            existingMap.put(startReference, start);
        }

        List<Start> mergedList = new ArrayList<>();

        for (StartingListReportEntryDto entryDto : remoteDto.getEntries()) {
            StartReference startReference = getStartReference(entryDto);
            Start existing = existingMap.get(startReference);
            unusedStarts.remove(startReference);
            Start merged = buildUpdatedStart(entryDto, existing);
            mergedList.add(merged);
        }

        for (StartReference unusedReference : unusedStarts) {
            Start existing = existingMap.get(unusedReference);
            Start merged = buildDeletedStart(existing);
            if (merged != null) {
                mergedList.add(merged);
            }
        }

        return mergedList;
    }

    private StartReference getStartReference(StartingListReportEntryDto entryDto) {
        return new StartReference(
                0,
                entryDto.getAthlete().getAthleteId(),
                entryDto.getDiscipline().getDisciplineId());
    }

    private Start buildUpdatedStart(StartingListReportEntryDto entryDto, Start existing) {
        Start updated;
        if (existing == null) {
            updated = new Start()
                    .setAthleteId(entryDto.getAthlete().getAthleteId())
                    .setDisciplineId(entryDto.getDiscipline().getDisciplineId())
                    .setState(Start.StartState.READY);
        } else {
            updated = existing.mutate();
        }

        updated
                .setAthleteId(entryDto.getAthlete().getAthleteId())
                .setDisciplineId(entryDto.getDiscipline().getDisciplineId())
                .setDisciplineName(entryDto.getDiscipline().getName())
                .setAthlete(entryDto.getAthlete())
                .setStartTimes(entryDto.getStart())
                .setAnnouncement(entryDto.getAnnouncement().getPerformance())
                .setConfirmedResult(entryDto.getCurrentResult());

        return updated.freeze();
    }

    private Start buildDeletedStart(Start existing) {
        return existing
                .mutate()
                .setState(Start.StartState.GONE)
                .freeze();
    }

}
