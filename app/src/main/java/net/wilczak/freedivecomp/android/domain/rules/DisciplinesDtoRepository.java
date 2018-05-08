package net.wilczak.freedivecomp.android.domain.rules;

import net.wilczak.freedivecomp.android.domain.race.Race;
import net.wilczak.freedivecomp.android.remote.messages.DisciplineDto;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface DisciplinesDtoRepository {
    Observable<List<DisciplineDto>> getDisciplines(Race race);
    Completable saveDisciplines(Race race, List<DisciplineDto> disciplines);
}
