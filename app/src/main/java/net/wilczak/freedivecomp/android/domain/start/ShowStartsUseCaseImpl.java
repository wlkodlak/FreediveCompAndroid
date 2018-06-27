package net.wilczak.freedivecomp.android.domain.start;

import net.wilczak.freedivecomp.android.domain.race.Race;
import net.wilczak.freedivecomp.android.domain.startinglanes.StartingLane;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class ShowStartsUseCaseImpl implements ShowStartsUseCase {
    private final StartsRepository repository;
    private final SynchronizeStartsUseCase synchronize;

    @Inject
    public ShowStartsUseCaseImpl(StartsRepository repository, SynchronizeStartsUseCase synchronize) {
        this.repository = repository;
        this.synchronize = synchronize;
    }

    @Override
    public Observable<List<Start>> getStartsInLane(Race race, StartingLane startingLane) {
        return repository.getAll(race).map(all -> filterStartingLane(all, startingLane.getId()));
    }

    private List<Start> filterStartingLane(List<Start> allStarts, String startingLaneId) {
        List<Start> filtered = new ArrayList<>();
        for (Start start : allStarts) {
            if (startingLaneId.equals(start.getStartTimes().getStartingLaneId())) {
                filtered.add(start);
            }
        }
        return filtered;
    }

    @Override
    public Completable forceRefresh(Race race) {
        return synchronize.synchronize(race);
    }
}
