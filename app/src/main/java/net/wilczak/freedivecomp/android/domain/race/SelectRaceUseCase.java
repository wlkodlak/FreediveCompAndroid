package net.wilczak.freedivecomp.android.domain.race;

import net.wilczak.freedivecomp.android.domain.startinglanes.StartingLanesRepository;
import net.wilczak.freedivecomp.android.remote.messages.RaceSettingsDto;
import net.wilczak.freedivecomp.android.remote.messages.RaceSetupDto;
import net.wilczak.freedivecomp.android.remote.messages.StartingLaneDto;
import net.wilczak.freedivecomp.android.remote.remoteservice.RemoteServiceProvider;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public class SelectRaceUseCase {
    private final RemoteServiceProvider remoteServiceProvider;
    private final RaceRepository raceRepository;
    private final StartingLanesRepository lanesRepository;

    public SelectRaceUseCase(RemoteServiceProvider remoteServiceProvider, RaceRepository raceRepository, StartingLanesRepository lanesRepository) {
        this.remoteServiceProvider = remoteServiceProvider;
        this.raceRepository = raceRepository;
        this.lanesRepository = lanesRepository;
    }

    public Single<Race> selectRace(Race searchRace) {
        return sync(searchRace);
    }

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

        return remoteServiceProvider
                .getService(reference.getUri())
                .getRaceSetup(reference.getRaceId())
                .zipWith(
                        raceRepository.getSavedRace(reference.getRaceId()).firstOrError(),
                        (setup, savedRace) -> new SyncSources(setup, savedRace == Race.MISSING ? reference : savedRace))
                .flatMap(syncSources -> {
                    RaceSettingsDto raceSettingsDto = syncSources.getSetup().getRace();
                    List<StartingLaneDto> startingLaneDtos = syncSources.getSetup().getStartingLanes();

                    Race updatedRace = new Race(syncSources.getRace())
                            .setSaved(true)
                            .setSelected(true)
                            .setName(raceSettingsDto.getName())
                            .setSince(raceSettingsDto.getStart())
                            .setUntil(raceSettingsDto.getEnd());

                    Completable raceSaving = raceRepository.saveRace(updatedRace);
                    Completable lanesSaving = lanesRepository.saveLanes(updatedRace, startingLaneDtos);
                    return raceSaving.andThen(lanesSaving).andThen(Single.just(updatedRace));
                });
    }

    private static class SyncSources {
        private final RaceSetupDto setup;
        private final Race race;

        public SyncSources(RaceSetupDto setup, Race savedRace) {
            this.setup = setup;
            this.race = savedRace;
        }

        public RaceSetupDto getSetup() {
            return setup;
        }

        public Race getRace() {
            return race;
        }
    }
}
