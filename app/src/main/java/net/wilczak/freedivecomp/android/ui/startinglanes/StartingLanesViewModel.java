package net.wilczak.freedivecomp.android.ui.startinglanes;

import android.databinding.Bindable;

import net.wilczak.freedivecomp.android.domain.race.Race;
import net.wilczak.freedivecomp.android.domain.startinglanes.StartingLane;
import net.wilczak.freedivecomp.android.domain.startinglanes.StartingLanesRepository;
import net.wilczak.freedivecomp.android.ui.application.AndroidScheduler;
import net.wilczak.freedivecomp.android.ui.application.BackgroundScheduler;
import net.wilczak.freedivecomp.android.ui.application.BaseViewModel;
import net.wilczak.freedivecomp.android.BR;
import net.wilczak.freedivecomp.android.ui.application.BindableList;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;

public class StartingLanesViewModel extends BaseViewModel implements StartingLanesItemViewModel.OnClickListener {
    private final Scheduler backgroundScheduler;
    private final Scheduler foregroundScheduler;
    private final StartingLanesRepository startingLanesRepository;
    private InternalView view;
    private List<StartingLanesItemViewModel> startingLanes;
    private Race race;
    private Disposable loadingDisposable;
    private BindableList.TemplateSelector templateSelector;

    @Inject
    public StartingLanesViewModel(
            StartingLanesRepository startingLanesRepository,
            @BackgroundScheduler Scheduler backgroundScheduler,
            @AndroidScheduler Scheduler foregroundScheduler) {
        this.startingLanesRepository = startingLanesRepository;
        this.backgroundScheduler = backgroundScheduler;
        this.foregroundScheduler = foregroundScheduler;
        this.templateSelector = new StartingLanesTemplateSelector();
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

    public void onStart() {
        if (loadingDisposable == null) {
            loadingDisposable = startingLanesRepository
                    .getRootStartingLanes(race)
                    .subscribeOn(backgroundScheduler)
                    .observeOn(foregroundScheduler)
                    .subscribe(this::showLanes, this::showError);
        }
    }

    private void showLanes(List<StartingLane> startingLanes) {
        List<StartingLanesItemViewModel> viewModels = new ArrayList<>();
        for (StartingLane startingLane : startingLanes) {
            addItemViewModel(viewModels, startingLane);
        }
        setStartingLanes(viewModels);
    }

    private void addItemViewModel(List<StartingLanesItemViewModel> viewModels, StartingLane startingLane) {
        StartingLanesItemViewModel viewModel = new StartingLanesItemViewModel(startingLane);
        viewModel.setClickListener(this);
        viewModels.add(viewModel);
        for (StartingLane subLane : startingLane.getChildren()) {
            addItemViewModel(viewModels, subLane);
        }
    }

    private void showError(Throwable throwable) {
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (loadingDisposable != null) {
            loadingDisposable.dispose();
            loadingDisposable = null;
        }
    }

    @Bindable
    public List<StartingLanesItemViewModel> getStartingLanes() {
        return startingLanes;
    }

    private void setStartingLanes(List<StartingLanesItemViewModel> startingLanes) {
        this.startingLanes = startingLanes;
        notifyPropertyChanged(BR.startingLanes);
    }

    @Override
    public void onLaneClick(StartingLane lane) {
        if (view != null) {
            view.openStartingList(race, lane);
        }
    }

    @Bindable
    public BindableList.TemplateSelector getTemplateSelector() {
        return templateSelector;
    }

    public interface InternalView {
        void openStartingList(Race race, StartingLane lane);
    }
}
