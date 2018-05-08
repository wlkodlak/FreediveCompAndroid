package net.wilczak.freedivecomp.android.domain.startinglanes;

import net.wilczak.freedivecomp.android.domain.race.Race;

import java.util.List;

import io.reactivex.Observable;

public interface StartingLanesRepository {
    Observable<List<StartingLane>> getRootStartingLanes(Race race);
    Observable<StartingLane> getLeafStartingLane(Race race, String startingLaneId);
}
