package net.wilczak.freedivecomp.android.domain.authentication;

import net.wilczak.freedivecomp.android.domain.race.Race;
import net.wilczak.freedivecomp.android.domain.race.RaceRepository;

import io.reactivex.Observable;
import io.reactivex.Single;

public class AuthenticateUseCase {
    private final RaceRepository raceRepository;

    public AuthenticateUseCase(RaceRepository raceRepository) {
        this.raceRepository = raceRepository;
    }

    public Observable<Race> observeRace(Race race) {
        return raceRepository.getSavedRace(race.getRaceId());
    }

    public Single<Race> authenticateRace(Race race) {
        return Single.error(new UnsupportedOperationException());
    }
}
