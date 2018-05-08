package net.wilczak.freedivecomp.android.domain.rules;

import net.wilczak.freedivecomp.android.domain.race.Race;
import net.wilczak.freedivecomp.android.remote.messages.RulesDto;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface RulesDtoRepository {
    Single<List<RulesDto>> getRules(Race race);
    Completable saveRules(Race race, List<RulesDto> rules);
}
