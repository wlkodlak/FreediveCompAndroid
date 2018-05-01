package net.wilczak.freedivecomp.android.domain.startinglanes;

import net.wilczak.freedivecomp.android.domain.race.Race;
import net.wilczak.freedivecomp.android.remote.messages.StartingLaneDto;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;

public class StartingLanesCompositeRepository implements StartingLanesRepository {
    private static final Object NOTHING = new Object();

    private final StartingLanesRepository localRepository;
    private final StartingLanesRepository remoteRepository;
    private final ConcurrentHashMap<String, Object> refreshingRaces;

    public StartingLanesCompositeRepository(StartingLanesRepository localRepository, StartingLanesRepository remoteRepository) {
        this.localRepository = localRepository;
        this.remoteRepository = remoteRepository;
        this.refreshingRaces = new ConcurrentHashMap<>();
    }

    @Override
    public Observable<List<StartingLaneDto>> getLanes(Race race) {
        boolean needsRefresh = this.refreshingRaces.contains(race.getRaceId());
        if (needsRefresh) {
            return getRemoteLanesAndSave(race);
        } else {
            return localRepository
                    .getLanes(race)
                    .flatMap(list -> list.size() > 0 ? Observable.just(list) : getRemoteLanesAndSave(race));
        }
    }

    private Observable<List<StartingLaneDto>> getRemoteLanesAndSave(Race race) {
        return remoteRepository
                .getLanes(race)
                .flatMap(list -> saveListAndPassOn(race, list));
    }

    private ObservableSource<List<StartingLaneDto>> saveListAndPassOn(Race race, List<StartingLaneDto> list) {
        return saveLanes(race, list).andThen(Observable.just(list));
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
