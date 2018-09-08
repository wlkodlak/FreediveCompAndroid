package net.wilczak.freedivecomp.android.ui.startinglist;

import android.databinding.Bindable;

import net.wilczak.freedivecomp.android.BR;
import net.wilczak.freedivecomp.android.domain.race.Race;
import net.wilczak.freedivecomp.android.domain.start.ShowStartsUseCase;
import net.wilczak.freedivecomp.android.domain.start.Start;
import net.wilczak.freedivecomp.android.domain.start.StartReference;
import net.wilczak.freedivecomp.android.domain.startinglanes.StartingLane;
import net.wilczak.freedivecomp.android.domain.startinglanes.StartingLanesRepository;
import net.wilczak.freedivecomp.android.ui.application.AndroidScheduler;
import net.wilczak.freedivecomp.android.ui.application.BackgroundScheduler;
import net.wilczak.freedivecomp.android.ui.application.BaseViewModel;
import net.wilczak.freedivecomp.android.ui.application.Localization;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;

public class StartingListViewModel extends BaseViewModel implements StartingListItemViewModel.OnClickListener {
    private final StartingLanesRepository startingLanesRepository;
    private final ShowStartsUseCase showStartsUseCase;
    private final Scheduler backgroundScheduler, foregroundScheduler;
    private final Localization localization;
    private Disposable startingLanesDisposable;
    private Disposable showStartsDisposable;
    private Race race;
    private String startingLaneId;
    private StartingLane startingLane;
    private InternalView view;
    private List<StartingListItemViewModel> starts;
    private StartReference finishedResultEntered;

    @Inject
    public StartingListViewModel(
            StartingLanesRepository startingLanesRepository,
            ShowStartsUseCase showStartsUseCase,
            @BackgroundScheduler Scheduler backgroundScheduler,
            @AndroidScheduler Scheduler foregroundScheduler,
            Localization localization) {
        this.startingLanesRepository = startingLanesRepository;
        this.showStartsUseCase = showStartsUseCase;
        this.backgroundScheduler = backgroundScheduler;
        this.foregroundScheduler = foregroundScheduler;
        this.localization = localization;
    }

    public void attachView(InternalView view) {
        this.view = view;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public StartingLane getStartingLane() {
        return startingLane;
    }

    private void setStartingLane(StartingLane startingLane) {
        this.startingLane = startingLane;
        notifyPropertyChanged(BR.title);
    }

    public void setStartingLaneId(String startingLaneId) {
        this.startingLaneId = startingLaneId;
    }

    public StartReference getFinishedResultEntered() {
        return finishedResultEntered;
    }

    public void setFinishedResultEntered(StartReference finishedResultEntered) {
        this.finishedResultEntered = finishedResultEntered;
    }

    @Bindable
    public String getTitle() {
        if (startingLane == null) return null;
        return startingLane.getFullName();
    }

    @Bindable
    public List<StartingListItemViewModel> getStarts() {
        return starts;
    }

    private void setStarts(List<StartingListItemViewModel> starts) {
        this.starts = starts;
        notifyPropertyChanged(BR.starts);
    }

    public void onStart() {
        if (startingLanesDisposable != null) {
            startingLanesDisposable = startingLanesRepository
                    .getLeafStartingLane(race, startingLaneId)
                    .subscribeOn(backgroundScheduler)
                    .observeOn(foregroundScheduler)
                    .subscribe(this::setStartingLane);
        }
        if (showStartsDisposable != null) {
            showStartsDisposable = showStartsUseCase
                    .getStartsInLane(race, startingLaneId)
                    .subscribeOn(backgroundScheduler)
                    .observeOn(foregroundScheduler)
                    .subscribe(this::setRawStarts);
        }
        if (starts != null && finishedResultEntered != null) {
            advanceToNextAthlete();
        }
    }

    private void setRawStarts(List<Start> starts) {
        List<StartingListItemViewModel> viewModels = new ArrayList<>();
        for (Start start : starts) {
            StartingListItemViewModel viewModel = new StartingListItemViewModel(race, start, localization);
            viewModel.setClickListener(this);
            viewModels.add(viewModel);
        }
        setStarts(viewModels);
        if (finishedResultEntered != null) {
            advanceToNextAthlete();
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (startingLanesDisposable != null) {
            startingLanesDisposable.dispose();
            startingLanesDisposable = null;
        }
    }

    @Override
    public void onStartClick(Start start) {
        if (view != null) {
            view.openEnterPerformance(race, start.getReference());
        }
    }

    private void advanceToNextAthlete() {
        boolean justFoundStart = false;
        StartReference nextStart = null;
        for (StartingListItemViewModel viewModel : getStarts()) {
            StartReference reference = viewModel.getStart().getReference();
            if (reference.equals(finishedResultEntered)) {
                justFoundStart = true;
            } else if (justFoundStart) {
                nextStart = reference;
            }
        }
        finishedResultEntered = null;
        if (nextStart != null) {
            view.openEnterPerformance(race, nextStart);
        }
    }

    public interface InternalView {
        void openEnterPerformance(Race race, StartReference startReference);
    }
}
