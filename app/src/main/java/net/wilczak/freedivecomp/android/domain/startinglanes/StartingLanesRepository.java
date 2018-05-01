package net.wilczak.freedivecomp.android.domain.startinglanes;

import net.wilczak.freedivecomp.android.domain.race.Race;
import net.wilczak.freedivecomp.android.remote.messages.StartingLaneDto;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface StartingLanesRepository {
    Observable<List<StartingLaneDto>> getLanes(Race race);
    Completable saveLanes(Race race, List<StartingLaneDto> lanes);
    void requestRefresh(Race race);
}
