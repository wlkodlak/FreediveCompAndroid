package net.wilczak.freedivecomp.android.domain.start;

import com.google.gson.Gson;

import net.wilczak.freedivecomp.android.domain.race.Race;
import net.wilczak.freedivecomp.android.domain.startinglanes.StartingLanesRepository;
import net.wilczak.freedivecomp.android.remote.messages.ReportActualResultDto;
import net.wilczak.freedivecomp.android.remote.messages.ReportAnnouncementDto;
import net.wilczak.freedivecomp.android.remote.messages.ReportDisciplineDto;
import net.wilczak.freedivecomp.android.remote.messages.StartingListReportEntryDto;
import net.wilczak.freedivecomp.android.room.AppDatabase;
import net.wilczak.freedivecomp.android.room.StartEntity;
import net.wilczak.freedivecomp.android.rxutils.ValueCacheObservable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class StartsLocalRepository implements StartsRepository {
    private final AppDatabase database;
    private final Gson gson;
    private final PublishSubject<String> changeNotification;
    private final ConcurrentHashMap<String, Observable<List<Start>>> observableStarts;
    private final StartingLanesRepository startingLanesRepository;

    @Inject
    public StartsLocalRepository(AppDatabase database, StartingLanesRepository startingLanesRepository) {
        this.database = database;
        this.startingLanesRepository = startingLanesRepository;
        this.gson = new Gson();
        this.changeNotification = PublishSubject.create();
        this.observableStarts = new ConcurrentHashMap<>();
    }

    @Override
    public Observable<List<Start>> getAll(Race race) {
        Observable<List<Start>> existing = observableStarts.get(race.getRaceId());
        if (existing != null) return existing;
        Observable<List<Start>> newObservable = createObservable(race);
        existing = observableStarts.putIfAbsent(race.getRaceId(), newObservable);
        return existing != null ? existing : newObservable;
    }

    private Observable<List<Start>> createObservable(Race race) {
        return Observable
                .fromCallable(() -> loadFromDatabase(race))
                .compose(ValueCacheObservable.transform(changeNotification));
    }

    private List<Start> loadFromDatabase(Race race) {
        List<Start> starts = new ArrayList<>();
        List<StartEntity> entities = database.startDao().getAllForRace(race.getRaceId());

        for (StartEntity entity : entities) {
            StartingListReportEntryDto startDto = gson.fromJson(entity.getStartJson(), StartingListReportEntryDto.class);
            ReportActualResultDto localResultDto = gson.fromJson(entity.getResultJson(), ReportActualResultDto.class);

            Start start = new Start()
                    .setInternalId(entity.getId())
                    .setState(Enum.valueOf(Start.StartState.class, entity.getResultStatus()))
                    .setAthleteId(entity.getAthleteId())
                    .setDisciplineId(entity.getDisciplineId())
                    .setDisciplineName(startDto.getDiscipline().getName())
                    .setDisciplineRules(startDto.getDiscipline().getRules())
                    .setAthlete(startDto.getAthlete())
                    .setStartTimes(startDto.getStart())
                    .setAnnouncement(startDto.getAnnouncement().getPerformance())
                    .setConfirmedResult(startDto.getCurrentResult())
                    .setLocalResult(localResultDto)
                    .freeze();

            starts.add(start);
        }
        return starts;
    }

    @Override
    public Completable replaceAll(Race race, List<Start> starts) {
        return Completable.fromAction(() -> replaceAllInternal(race, starts));
    }

    private void replaceAllInternal(Race race, List<Start> starts) {
        HashMap<StartReference, StartEntity> unmatched = new HashMap<>();

        for (StartEntity entity : database.startDao().getAllForRace(race.getRaceId())) {
            StartReference reference = new StartReference(entity.getId(), entity.getAthleteId(), entity.getDisciplineId());
            unmatched.put(reference, entity);
        }

        for (Start start : starts) {
            StartReference reference = start.getReference();
            unmatched.remove(reference);
            StartEntity entity = buildEntity(race, start);
            database.startDao().insert(entity);
        }

        for (StartEntity entity : unmatched.values()) {
            database.startDao().delete(entity);
        }
    }

    private StartEntity buildEntity(Race race, Start start) {
        StartingListReportEntryDto startDto = new StartingListReportEntryDto();
        startDto.setAthlete(start.getAthlete());
        startDto.setDiscipline(new ReportDisciplineDto().setDisciplineId(start.getDisciplineId()).setName(start.getDisciplineName()).setRules(start.getDisciplineRules()));
        startDto.setAnnouncement(new ReportAnnouncementDto().setPerformance(start.getAnnouncement()));
        startDto.setStart(start.getStartTimes());
        startDto.setCurrentResult(start.getConfirmedResult());

        StartEntity entity = new StartEntity();
        entity.setId(start.getInternalId());
        entity.setRaceId(race.getRaceId());
        entity.setResultStatus(start.getState().toString());
        entity.setAthleteId(start.getAthleteId());
        entity.setDisciplineId(start.getDisciplineId());
        entity.setStartJson(gson.toJson(startDto));
        entity.setResultJson(gson.toJson(start.getLocalResult()));

        return entity;
    }

    @Override
    public Completable merge(Race race, StartReference reference, Start replacement) {
        return Completable.fromAction(() -> mergeInternal(race, reference, replacement));
    }

    public void mergeInternal(Race race, StartReference reference, Start replacement) {
        if (reference == null) {
            reference = replacement.getReference();
        }
        StartEntity existing = database.startDao().getByAthlete(race.getRaceId(), reference.getAthleteId(), reference.getDisciplineId());
        StartEntity newEntity = replacement == null ? null : buildEntity(race, replacement);

        if (newEntity == null) {
            if (existing != null) {
                database.startDao().delete(existing);
            }
        } else {
            if (existing != null) {
                newEntity.setId(existing.getId());
            }
            database.startDao().insert(newEntity);
        }
    }
}
