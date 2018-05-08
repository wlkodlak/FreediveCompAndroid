package net.wilczak.freedivecomp.android.domain.rules;

import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.wilczak.freedivecomp.android.domain.race.Race;
import net.wilczak.freedivecomp.android.remote.messages.DisciplineDto;
import net.wilczak.freedivecomp.android.room.AppDatabase;
import net.wilczak.freedivecomp.android.room.DisciplinesEntity;
import net.wilczak.freedivecomp.android.rxutils.ValueCacheObservable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class DisciplinesLocalDtoRepository implements DisciplinesDtoRepository {
    private final AppDatabase database;
    private final Gson gson;
    private final PublishSubject<String> changeNotification;
    private final ConcurrentHashMap<String, Observable<List<DisciplineDto>>> observableLanes;

    public DisciplinesLocalDtoRepository(AppDatabase database) {
        this.database = database;
        this.gson = new Gson();
        this.changeNotification = PublishSubject.create();
        this.observableLanes = new ConcurrentHashMap<>();
    }

    @Override
    public Observable<List<DisciplineDto>> getDisciplines(Race race) {
        Observable<List<DisciplineDto>> existing = observableLanes.get(race.getRaceId());
        if (existing != null) return existing;
        Observable<List<DisciplineDto>> newObservable = createDisciplinesObservable(race);
        existing = observableLanes.putIfAbsent(race.getRaceId(), newObservable);
        return existing != null ? existing : newObservable;
    }

    private Observable<List<DisciplineDto>> createDisciplinesObservable(Race race) {
        return Observable
                .fromCallable(() -> loadFromDatabase(race))
                .compose(ValueCacheObservable.transform(changeNotification));
    }

    @Nullable
    private List<DisciplineDto> loadFromDatabase(Race race) {
        DisciplinesEntity entity = database.disciplinesDao().get(race.getRaceId());
        if (entity == null) return new ArrayList<>();
        return gson.fromJson(entity.getDisciplinesJson(), getListType());
    }

    @Override
    public Completable saveDisciplines(Race race, List<DisciplineDto> disciplines) {
        return Completable.fromAction(() -> {
            DisciplinesEntity entity = new DisciplinesEntity(race.getRaceId());
            entity.setDisciplinesJson(gson.toJson(disciplines, getListType()));
            database.disciplinesDao().insert(entity);
            changeNotification.onNext(race.getRaceId());
        });
    }

    private Type getListType() {
        return new TypeToken<ArrayList<DisciplineDto>>(){
        }.getType();
    }
}
