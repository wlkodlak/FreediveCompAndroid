package net.wilczak.freedivecomp.android.domain.startinglanes;

import net.wilczak.freedivecomp.android.domain.race.Race;
import net.wilczak.freedivecomp.android.remote.messages.RaceSetupDto;
import net.wilczak.freedivecomp.android.remote.messages.StartingLaneDto;
import net.wilczak.freedivecomp.android.remote.remoteservice.RemoteService;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class StartingLanesRemoteRepository implements StartingLanesRepository {
    private final RemoteService remoteService;

    public StartingLanesRemoteRepository(RemoteService remoteService) {
        this.remoteService = remoteService;
    }

    @Override
    public Observable<List<StartingLaneDto>> getLanes(Race race) {
        return remoteService
                .getRaceSetup(race.getRaceId())
                .map(RaceSetupDto::getStartingLanes)
                .toObservable();
    }

    @Override
    public Completable saveLanes(Race race, List<StartingLaneDto> lanes) {
        return Completable.error(new UnsupportedOperationException("Cannot save on remote interface"));
    }

    @Override
    public void requestRefresh(Race race) {
    }
}
