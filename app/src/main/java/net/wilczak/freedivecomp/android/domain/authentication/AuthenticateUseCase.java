package net.wilczak.freedivecomp.android.domain.authentication;

import net.wilczak.freedivecomp.android.domain.race.Race;

import io.reactivex.Observable;

public interface AuthenticateUseCase {
    Observable<Status> authenticateRace(Race race);

    interface Status {
        Race getRace();
        Throwable getError();
        boolean isAuthenticated();
        String getConnectCode();
        State getState();
    }

    enum State {
        STARTING,
        BETWEEN_ATTEMPTS,
        BUSY,
        AUTHENTICATED,
        FAILED,
    }
}
