package net.wilczak.freedivecomp.android.domain.race;

import io.reactivex.Single;

interface SelectRaceUseCase {
    Single<Race> selectRace(Race searchRace);

    Single<Race> autoSelect();
}
