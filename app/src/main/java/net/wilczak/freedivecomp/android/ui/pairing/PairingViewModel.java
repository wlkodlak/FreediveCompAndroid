package net.wilczak.freedivecomp.android.ui.pairing;

import android.databinding.Bindable;

import net.wilczak.freedivecomp.android.BR;
import net.wilczak.freedivecomp.android.R;
import net.wilczak.freedivecomp.android.domain.authentication.AuthenticateUseCase;
import net.wilczak.freedivecomp.android.domain.authentication.AuthenticateUseCaseImpl;
import net.wilczak.freedivecomp.android.domain.race.Race;
import net.wilczak.freedivecomp.android.ui.application.AndroidScheduler;
import net.wilczak.freedivecomp.android.ui.application.BackgroundScheduler;
import net.wilczak.freedivecomp.android.ui.application.BaseViewModel;
import net.wilczak.freedivecomp.android.ui.application.Localization;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;

public class PairingViewModel extends BaseViewModel {
    private final AuthenticateUseCase authenticateUseCase;
    private final Scheduler backgroundScheduler;
    private final Scheduler foregroundScheduler;
    private final Localization localization;
    private InternalView view;
    private Race race;
    private Disposable authenticationCancellation;
    private String connectCode;

    @Inject
    public PairingViewModel(
            AuthenticateUseCase authenticateUseCase,
            @BackgroundScheduler Scheduler backgroundScheduler,
            @AndroidScheduler Scheduler foregroundScheduler, Localization localization) {
        this.authenticateUseCase = authenticateUseCase;
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
        notifyPropertyChanged(BR.pairingMessage);
    }

    public void onStart() {
        if (authenticationCancellation == null) {
            authenticationCancellation = authenticateUseCase
                    .authenticateRace(race)
                    .subscribeOn(backgroundScheduler)
                    .observeOn(foregroundScheduler)
                    .subscribe(this::onStatusUpdated, this::showError);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (authenticationCancellation != null) {
            authenticationCancellation.dispose();
            authenticationCancellation = null;
        }
    }

    @Bindable
    public String getPairingMessage() {
        if (race == null) return null;
        if (race.isAuthenticated()) {
            return String.format(
                    localization.getString(R.string.pairing_paired_message),
                    race.getName()
            );
        } else {
            return String.format(
                    localization.getString(R.string.pairing_unpaired_message),
                    race.getName()
            );
        }
    }

    @Bindable
    public String getConnectCode() {
        return connectCode;
    }

    private void setConnectCode(String connectCode) {
        this.connectCode = connectCode;
        notifyPropertyChanged(BR.connectCode);
    }

    private void onStatusUpdated(AuthenticateUseCaseImpl.Status status) {
        setRace(status.getRace());
        setConnectCode(status.getConnectCode());
        AuthenticateUseCase.State state = status.getState();
        if (state == AuthenticateUseCase.State.AUTHENTICATED) {
            view.openStartingLanes(status.getRace());
        }
    }

    private void showError(Throwable throwable) {
    }

    public interface InternalView {
        void openStartingLanes(Race race);
    }
}
