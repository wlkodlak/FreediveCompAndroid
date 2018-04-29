package net.wilczak.freedivecomp.android.domain.race;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface RaceRepository {
    Observable<List<Race>> search(String query);
    Observable<List<Race>> getSavedRaces();
    Observable<Race> getSavedRace(String raceId);
    Completable saveRace(Race race);
    Completable forgetRace(String raceId);
}
