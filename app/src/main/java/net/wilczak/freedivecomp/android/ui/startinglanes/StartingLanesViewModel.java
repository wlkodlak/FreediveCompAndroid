package net.wilczak.freedivecomp.android.ui.startinglanes;

import android.databinding.Bindable;

import net.wilczak.freedivecomp.android.domain.race.Race;
import net.wilczak.freedivecomp.android.domain.startinglanes.StartingLane;
import net.wilczak.freedivecomp.android.ui.application.BaseViewModel;

import java.util.List;

import javax.inject.Inject;

public class StartingLanesViewModel extends BaseViewModel {
    private InternalView view;
    private List<StartingLanesItemViewModel> startingLanes;

    @Inject
    public StartingLanesViewModel() {
    }

    public void attachView(InternalView view) {
        this.view = view;
    }

    public void onStart() {

    }

    @Bindable
    public List<StartingLanesItemViewModel> getStartingLanes() {
        return startingLanes;
    }

    private void setStartingLanes(List<StartingLanesItemViewModel> startingLanes) {
        this.startingLanes = startingLanes;
    }

    public interface InternalView {
        void openStartingList(Race race, StartingLane lane);
    }
}
