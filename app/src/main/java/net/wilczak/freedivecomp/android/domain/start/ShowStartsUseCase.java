package net.wilczak.freedivecomp.android.domain.start;

import net.wilczak.freedivecomp.android.domain.race.Race;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface ShowStartsUseCase {
    Observable<List<Start>> getStartsInLane(Race race, String startingLaneId);
    Completable forceRefresh(Race race);
}
