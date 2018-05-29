package net.wilczak.freedivecomp.android.domain.start;

import net.wilczak.freedivecomp.android.domain.race.Race;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface StartsRepository {
    Observable<List<Start>> getAll(Race race);
    Completable replaceAll(Race race, List<Start> starts);
    Completable merge(Race race, StartReference reference, Start replacement);
}
