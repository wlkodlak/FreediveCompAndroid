package net.wilczak.freedivecomp.android.domain.race;

import net.wilczak.freedivecomp.android.domain.rules.DisciplinesDtoRepository;
import net.wilczak.freedivecomp.android.domain.rules.RulesDtoRepository;
import net.wilczak.freedivecomp.android.domain.startinglanes.StartingLanesDtoRepository;
import net.wilczak.freedivecomp.android.remote.messages.RaceSettingsDto;
import net.wilczak.freedivecomp.android.remote.messages.RaceSetupDto;
import net.wilczak.freedivecomp.android.remote.messages.RulesDto;
import net.wilczak.freedivecomp.android.remote.remoteservice.RemoteService;
import net.wilczak.freedivecomp.android.remote.remoteservice.RemoteServiceProvider;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

public class SelectRaceUseCaseImpl implements SelectRaceUseCase {
    private final RemoteServiceProvider remoteServiceProvider;
    private final RaceRepository raceRepository;
    private final StartingLanesDtoRepository lanesRepository;
    private final RulesDtoRepository rulesRepository;
    private final DisciplinesDtoRepository disciplinesRepository;

    @Inject
    public SelectRaceUseCaseImpl(
            RemoteServiceProvider remoteServiceProvider,
            RaceRepository raceRepository,
            StartingLanesDtoRepository lanesRepository,
            RulesDtoRepository rulesRepository,
            DisciplinesDtoRepository disciplinesRepository) {
        this.remoteServiceProvider = remoteServiceProvider;
        this.raceRepository = raceRepository;
        this.lanesRepository = lanesRepository;
        this.rulesRepository = rulesRepository;
        this.disciplinesRepository = disciplinesRepository;
    }

    @Override
    public Single<Race> selectRace(Race searchRace) {
        return sync(searchRace);
    }

    @Override
    public Single<Race> autoSelect() {
        return raceRepository
                .getSavedRaces()
                .firstOrError()
                .flatMap(races -> {
                    for (Race race : races) {
                        if (race.isSelected()) {
                            return sync(race);
                        }
                    }
                    return Single.just(Race.MISSING);
                });
    }

    private Single<Race> sync(Race reference) {
        RemoteService remoteService = remoteServiceProvider.getService(reference.getUri());
        Single<Race> referenceSingle = Single.just(reference);
        Single<Race> savedRaceSingle = raceRepository.getSavedRace(reference.getRaceId()).firstOrError();
        Single<RaceSetupDto> raceSetupSingle = remoteService.getRaceSetup(reference.getRaceId());
        Single<List<RulesDto>> rulesSingle = remoteService.getRules();
        return Single
                .zip(referenceSingle, savedRaceSingle, raceSetupSingle, rulesSingle, SyncSources::new)
                .flatMap(this::saveSynchronizedData);
    }

    private Single<Race> saveSynchronizedData(SyncSources syncSources) {
        RaceSettingsDto raceSettingsDto = syncSources.getSetup().getRace();
        Race updatedRace = new Race(syncSources.getRace())
                .setSaved(true)
                .setSelected(true)
                .setName(raceSettingsDto.getName())
                .setSince(raceSettingsDto.getStart())
                .setUntil(raceSettingsDto.getEnd());

        Completable everythingSaved = Completable.concatArray(
                raceRepository.saveRace(updatedRace),
                lanesRepository.saveLanes(updatedRace, syncSources.getSetup().getStartingLanes()),
                rulesRepository.saveRules(updatedRace, syncSources.getRules()),
                disciplinesRepository.saveDisciplines(updatedRace, syncSources.getSetup().getDisciplines())
        );
        return everythingSaved.andThen(Single.just(updatedRace));
    }

    private class SyncSources {
        private final Race race;
        private final RaceSetupDto setup;
        private final List<RulesDto> rules;

        public SyncSources(Race reference, Race saved, RaceSetupDto setup, List<RulesDto> rules) {
            this.race = saved == Race.MISSING ? reference : saved;
            this.setup = setup;
            this.rules = rules;
        }

        public Race getRace() {
            return race;
        }

        public RaceSetupDto getSetup() {
            return setup;
        }

        public List<RulesDto> getRules() {
            return rules;
        }
    }
}
