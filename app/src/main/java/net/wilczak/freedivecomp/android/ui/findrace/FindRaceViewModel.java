package net.wilczak.freedivecomp.android.ui.findrace;

import android.databinding.Bindable;
import android.view.View;

import net.wilczak.freedivecomp.android.BR;
import net.wilczak.freedivecomp.android.domain.race.Race;
import net.wilczak.freedivecomp.android.domain.race.SearchRacesUseCase;
import net.wilczak.freedivecomp.android.domain.race.SelectRaceUseCase;
import net.wilczak.freedivecomp.android.ui.application.AndroidScheduler;
import net.wilczak.freedivecomp.android.ui.application.BackgroundScheduler;
import net.wilczak.freedivecomp.android.ui.application.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;

public class FindRaceViewModel extends BaseViewModel implements SelectableRaceViewModel.OnClickListener {
    private final SearchRacesUseCase searchRacesUseCase;
    private final SelectRaceUseCase selectRaceUseCase;
    private final Scheduler backgroundScheduler, foregroundScheduler;
    private Disposable searchRacesCancellation;
    private Disposable selectRaceCancellation;
    private String searchQuery;
    private final List<SelectableRaceViewModel> foundRaces;
    private InternalView view;

    @Inject
    public FindRaceViewModel(
            SearchRacesUseCase searchRacesUseCase,
            SelectRaceUseCase selectRaceUseCase,
            @BackgroundScheduler Scheduler backgroundScheduler,
            @AndroidScheduler Scheduler foregroundScheduler) {
        this.searchRacesUseCase = searchRacesUseCase;
        this.selectRaceUseCase = selectRaceUseCase;
        this.backgroundScheduler = backgroundScheduler;
        this.foregroundScheduler = foregroundScheduler;
        this.foundRaces = new ArrayList<>();
    }

    public void attachView(InternalView view) {
        this.view = view;
    }

    public void onStart() {
        if (searchRacesCancellation == null && selectRaceCancellation == null) {
            searchRacesCancellation = searchRacesUseCase
                    .search(searchQuery)
                    .subscribeOn(backgroundScheduler)
                    .observeOn(foregroundScheduler)
                    .subscribe(this::showRaces, this::showError);
        }
    }

    public void onSearchClick(View v) {
        if (selectRaceCancellation != null) return; // actions disabled
        if (searchRacesCancellation != null) {
            searchRacesCancellation.dispose();
        }
        searchRacesCancellation = searchRacesUseCase
                .search(searchQuery)
                .subscribeOn(backgroundScheduler)
                .observeOn(foregroundScheduler)
                .subscribe(this::showRaces, this::showError);
    }

    @Bindable
    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
        notifyPropertyChanged(BR.searchQuery);

        if (searchRacesCancellation != null) {
            searchRacesCancellation.dispose();
            searchRacesCancellation = null;
        }
    }

    @Bindable
    public List<SelectableRaceViewModel> getFoundRaces() {
        return foundRaces;
    }

    private void showRaces(List<Race> newRaces) {
        foundRaces.clear();
        for (Race race : newRaces) {
            SelectableRaceViewModel raceViewModel = new SelectableRaceViewModel(race);
            raceViewModel.setClickListener(this);
            foundRaces.add(raceViewModel);
        }
        notifyPropertyChanged(BR.foundRaces);
    }

    private void showError(Throwable throwable) {
        searchRacesCancellation = null;
        selectRaceCancellation = null;
    }

    @Override
    protected void onCleared() {
        if (searchRacesCancellation != null) {
            searchRacesCancellation.dispose();
            searchRacesCancellation = null;
        }
        if (selectRaceCancellation != null) {
            selectRaceCancellation.dispose();
            selectRaceCancellation = null;
        }
    }

    @Override
    public void onRaceClicked(Race clickedRace) {
        selectRaceCancellation = selectRaceUseCase
                .selectRace(clickedRace)
                .subscribe(this::onRaceSelected, this::showError);
    }

    private void onRaceSelected(Race selectedRace) {
        selectRaceCancellation = null;
        if (view == null)
        if (selectedRace.isAuthenticated()) {
            view.openStartingLanes(selectedRace);
        } else {
            view.openPairing(selectedRace);
        }
    }

    public interface InternalView {
        void openPairing(Race race);
        void openStartingLanes(Race race);
    }
}
