package net.wilczak.freedivecomp.android.ui.splash;

import net.wilczak.freedivecomp.android.domain.race.Race;
import net.wilczak.freedivecomp.android.domain.race.SelectRaceUseCase;
import net.wilczak.freedivecomp.android.ui.application.AndroidScheduler;
import net.wilczak.freedivecomp.android.ui.application.BackgroundScheduler;
import net.wilczak.freedivecomp.android.ui.application.BaseViewModel;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;

public class SplashViewModel extends BaseViewModel {
    private final SelectRaceUseCase selectRaceUseCase;
    private final Scheduler backgroundScheduler, foregroundScheduler;
    private InternalView view;
    private Disposable selectRaceCancellation;

    @Inject
    public SplashViewModel(SelectRaceUseCase selectRaceUseCase, @BackgroundScheduler Scheduler backgroundScheduler, @AndroidScheduler Scheduler foregroundScheduler) {
        this.selectRaceUseCase = selectRaceUseCase;
        this.backgroundScheduler = backgroundScheduler;
        this.foregroundScheduler = foregroundScheduler;
    }

    public void setView(InternalView view) {
        this.view = view;
    }

    public void onStart() {
        selectRaceCancellation = selectRaceUseCase
                .autoSelect()
                .subscribeOn(backgroundScheduler)
                .observeOn(foregroundScheduler)
                .subscribe(race -> {
                    if (race.isMissing()) {
                        view.openFindRace();
                    } else if (race.isAuthenticated()) {
                        view.openStartingLanes(race);
                    } else {
                        view.openPairing(race);
                    }
                });
    }

    public void onStop() {
        if (selectRaceCancellation != null) {
            selectRaceCancellation.dispose();
            selectRaceCancellation = null;
        }
    }

    public interface InternalView {
        void openFindRace();
        void openPairing(Race race);
        void openStartingLanes(Race race);
    }
}
