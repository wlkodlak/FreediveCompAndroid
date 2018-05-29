package net.wilczak.freedivecomp.android.domain.start;

import net.wilczak.freedivecomp.android.domain.race.Race;

import io.reactivex.Completable;

public class SynchronizeStartsUseCaseImpl implements SynchronizeStartsUseCase {
    @Override
    public Completable synchronize(Race race) {
        return Completable.error(new UnsupportedOperationException("Not yet implemented"));
    }
}
