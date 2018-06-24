package net.wilczak.freedivecomp.android.domain.start;

import net.wilczak.freedivecomp.android.domain.race.Race;
import net.wilczak.freedivecomp.android.remote.messages.ReportActualResultDto;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import io.reactivex.Completable;
import io.reactivex.Single;

public class EnterPerformanceUseCaseImpl implements EnterPerformanceUseCase {
    private final StartsRepository repository;
    private final SynchronizeStartsUseCase synchronize;

    public EnterPerformanceUseCaseImpl(StartsRepository repository, SynchronizeStartsUseCase synchronize) {
        this.repository = repository;
        this.synchronize = synchronize;
    }

    @Override
    public Single<Start> getStart(Race race, StartReference reference) {
        return repository.getAll(race).map(all -> extractStart(all, reference)).first(Start.MISSING);
    }

    private Start extractStart(List<Start> all, StartReference reference) {
        for (Start start : all) {
            if (reference.equals(start.getReference())) {
                return start;
            }
        }
        return Start.MISSING;
    }

    @Override
    public Completable postResult(Race race, StartReference reference, ReportActualResultDto localResult) {
        return repository.getAll(race)
                .first(Collections.emptyList())
                .map(all -> extractStart(all, reference))
                .flatMapCompletable(start -> {
                    if (!start.exists()) {
                        return Completable.error(new NoSuchElementException("Referenced start does not exist"));
                    }
                    Start updatedStart = start.mutate()
                            .setLocalResult(localResult)
                            .setState(Start.StartState.PENDING)
                            .freeze();
                    return repository
                            .merge(race, reference, updatedStart.freeze())
                            .andThen(Completable.fromAction(() -> synchronize.synchronize(race)));
                });
    }
}
