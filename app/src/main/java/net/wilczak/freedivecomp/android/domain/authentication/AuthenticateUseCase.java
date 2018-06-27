package net.wilczak.freedivecomp.android.domain.authentication;

import net.wilczak.freedivecomp.android.domain.race.Race;

import io.reactivex.Observable;

public interface AuthenticateUseCase {
    Observable<AuthenticateUseCaseImpl.Status> authenticateRace(Race race);
}
