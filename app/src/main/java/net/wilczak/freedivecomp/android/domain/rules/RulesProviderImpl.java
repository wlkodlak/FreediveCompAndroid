package net.wilczak.freedivecomp.android.domain.rules;

import net.wilczak.freedivecomp.android.domain.race.Race;
import net.wilczak.freedivecomp.android.remote.messages.DisciplineDto;
import net.wilczak.freedivecomp.android.remote.messages.RulesDto;

import java.util.List;
import java.util.NoSuchElementException;

import io.reactivex.Single;

public class RulesProviderImpl implements RulesProvider {
    private final RulesDtoRepository rulesRepository;
    private final DisciplinesDtoRepository disciplinesRepository;

    public RulesProviderImpl(RulesDtoRepository rulesRepository, DisciplinesDtoRepository disciplinesRepository) {
        this.rulesRepository = rulesRepository;
        this.disciplinesRepository = disciplinesRepository;
    }

    @Override
    public Single<Rules> getByDiscipline(Race race, String disciplineId) {
        return disciplinesRepository.getDisciplines(race).firstOrError()
                .map(disciplines -> extractRulesNameForDiscipline(disciplines, disciplineId))
                .flatMap(rulesName -> rulesRepository.getRules(race).map(allRules -> extractRuleByName(allRules, rulesName)))
                .map(this::buildDomainRules);
    }

    private String extractRulesNameForDiscipline(List<DisciplineDto> disciplines, String id) {
        for (DisciplineDto discipline : disciplines) {
            if (discipline.getDisciplineId().equals(id)) {
                return discipline.getRules();
            }
        }
        throw new NoSuchElementException();
    }

    private RulesDto extractRuleByName(List<RulesDto> rulesDtos, String rulesName) {
        for (RulesDto rulesDto : rulesDtos) {
            if (rulesDto.getName().equals(rulesName)) {
                return rulesDto;
            }
        }
        throw new NoSuchElementException();
    }

    private Rules buildDomainRules(RulesDto dto) {
        throw new UnsupportedOperationException();
    }
}
