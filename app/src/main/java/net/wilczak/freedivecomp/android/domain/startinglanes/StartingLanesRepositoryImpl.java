package net.wilczak.freedivecomp.android.domain.startinglanes;

import net.wilczak.freedivecomp.android.domain.race.Race;
import net.wilczak.freedivecomp.android.remote.messages.StartingLaneDto;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class StartingLanesRepositoryImpl implements StartingLanesRepository {
    private final StartingLanesLocalDtoRepository dtoRepository;

    public StartingLanesRepositoryImpl(StartingLanesLocalDtoRepository dtoRepository) {
        this.dtoRepository = dtoRepository;
    }

    @Override
    public Observable<List<StartingLane>> getRootStartingLanes(Race race) {
        return dtoRepository.getLanes(race).map(this::extractLanes);
    }

    private List<StartingLane> extractLanes(List<StartingLaneDto> dtos) {
        List<StartingLane> lanes = new ArrayList<>();
        for (StartingLaneDto dto : dtos) {
            StartingLane lane = new StartingLane(dto.getStartingLaneId(), dto.getShortName());
            addSubLanesFor(lane, dto.getSubLanes());
            lanes.add(lane);
        }
        return lanes;
    }

    private void addSubLanesFor(StartingLane parent, List<StartingLaneDto> dtos) {
        for (StartingLaneDto dto : dtos) {
            StartingLane lane = new StartingLane(dto.getStartingLaneId(), dto.getShortName());
            parent.addChild(lane);
            addSubLanesFor(lane, dto.getSubLanes());
        }
    }

    @Override
    public Observable<StartingLane> getLeafStartingLane(Race race, String startingLaneId) {
        return getRootStartingLanes(race).map(lanes -> extractLane(lanes, startingLaneId));
    }

    private StartingLane extractLane(List<StartingLane> lanes, String startingLaneId) {
        StartingLane found = extractLaneNullable(lanes, startingLaneId);
        if (found != null) return found;
        return StartingLane.missing(startingLaneId);
    }

    private StartingLane extractLaneNullable(List<StartingLane> lanes, String startingLaneId) {
        for (StartingLane lane : lanes) {
            if (startingLaneId.equals(lane.getId())) return lane;
            StartingLane child = extractLaneNullable(lane.getChildren(), startingLaneId);
            if (child != null) return child;
        }
        return null;
    }
}
