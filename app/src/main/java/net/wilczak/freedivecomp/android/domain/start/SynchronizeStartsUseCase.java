package net.wilczak.freedivecomp.android.domain.start;

import net.wilczak.freedivecomp.android.domain.race.Race;

import io.reactivex.Completable;

public interface SynchronizeStartsUseCase {
    Completable synchronize(Race race);
}
