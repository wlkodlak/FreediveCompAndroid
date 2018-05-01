package net.wilczak.freedivecomp.android.domain.startinglanes;

import net.wilczak.freedivecomp.android.domain.race.Race;
import net.wilczak.freedivecomp.android.remote.messages.StartingLaneDto;
import net.wilczak.freedivecomp.android.remote.remoteservice.RemoteServiceProvider;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class StartingLanesCompositeRepository implements StartingLanesRepository {
    private static final Object NOTHING = new Object();

    private final RemoteServiceProvider remoteServiceProvider;
    private final StartingLanesRepository localRepository;
    private final ConcurrentHashMap<String, Object> refreshingRaces;

    public StartingLanesCompositeRepository(RemoteServiceProvider remoteServiceProvider, StartingLanesRepository localRepository) {
        this.remoteServiceProvider = remoteServiceProvider;
        this.localRepository = localRepository;
        this.refreshingRaces = new ConcurrentHashMap<>();
    }

    @Override
    public Observable<List<StartingLaneDto>> getLanes(Race race) {
        return null;
    }

    @Override
    public Completable saveLanes(Race race, List<StartingLaneDto> lanes) {
        return localRepository
                .saveLanes(race, lanes)
                .doOnComplete(() -> refreshingRaces.remove(race.getRaceId()));
    }

    @Override
    public void requestRefresh(Race race) {
        refreshingRaces.put(race.getRaceId(), NOTHING);
    }
}
