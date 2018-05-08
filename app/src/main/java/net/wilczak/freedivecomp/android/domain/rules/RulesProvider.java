package net.wilczak.freedivecomp.android.domain.rules;

import net.wilczak.freedivecomp.android.domain.race.Race;

import io.reactivex.Single;

public interface RulesProvider {
    Single<Rules> getByDiscipline(Race race, String disciplineId);
}
