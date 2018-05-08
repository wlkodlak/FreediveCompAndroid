package net.wilczak.freedivecomp.android.domain.startinglanes;

import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.wilczak.freedivecomp.android.domain.race.Race;
import net.wilczak.freedivecomp.android.remote.messages.StartingLaneDto;
import net.wilczak.freedivecomp.android.room.AppDatabase;
import net.wilczak.freedivecomp.android.room.StartingLanesEntity;
import net.wilczak.freedivecomp.android.rxutils.ValueCacheObservable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class StartingLanesLocalRepository implements StartingLanesRepository {
    private final AppDatabase database;
    private final Gson gson;
    private final PublishSubject<String> changeNotification;
    private final ConcurrentHashMap<String, Observable<List<StartingLaneDto>>> observableLanes;

    public StartingLanesLocalRepository(AppDatabase database) {
        this.database = database;
        this.gson = new Gson();
        this.changeNotification = PublishSubject.create();
        this.observableLanes = new ConcurrentHashMap<>();
    }

    @Override
    public Observable<List<StartingLaneDto>> getLanes(Race race) {
        Observable<List<StartingLaneDto>> existing = observableLanes.get(race.getRaceId());
        if (existing != null) return existing;
        Observable<List<StartingLaneDto>> newObservable = createLanesObservable(race);
        existing = observableLanes.putIfAbsent(race.getRaceId(), newObservable);
        return existing != null ? existing : newObservable;
    }

    private Observable<List<StartingLaneDto>> createLanesObservable(Race race) {
        return Observable
                .fromCallable(() -> loadFromDatabase(race))
                .compose(ValueCacheObservable.transform(changeNotification));
    }

    @Nullable
    private List<StartingLaneDto> loadFromDatabase(Race race) {
        StartingLanesEntity entity = database.startingLanesDao().get(race.getRaceId());
        if (entity == null) return new ArrayList<>();
        return gson.fromJson(entity.getLanesJson(), getListType());
    }

    @Override
    public Completable saveLanes(Race race, List<StartingLaneDto> lanes) {
        return Completable.fromAction(() -> {
            StartingLanesEntity entity = new StartingLanesEntity(race.getRaceId());
            entity.setLanesJson(gson.toJson(lanes, getListType()));
            database.startingLanesDao().insert(entity);
            changeNotification.onNext(race.getRaceId());
        });
    }

    private Type getListType() {
        return new TypeToken<ArrayList<StartingLaneDto>>(){
        }.getType();
    }
}
