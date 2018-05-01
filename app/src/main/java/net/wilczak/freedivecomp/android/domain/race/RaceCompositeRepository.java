package net.wilczak.freedivecomp.android.domain.race;

import com.annimon.stream.Stream;

import net.wilczak.freedivecomp.android.remote.discovery.HostDiscovery;
import net.wilczak.freedivecomp.android.remote.discovery.HostDiscoveryGlobal;
import net.wilczak.freedivecomp.android.remote.remoteservice.RemoteServiceProvider;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class RaceCompositeRepository implements RaceRepository {
    private final HostDiscovery hostsSource;
    private final RemoteServiceProvider remoteServiceProvider;
    private final RaceRepository localRepository;

    public RaceCompositeRepository(HostDiscovery hostsSource, RemoteServiceProvider remoteServiceProvider, RaceRepository localRepository) {
        this.hostsSource = hostsSource;
        this.remoteServiceProvider = remoteServiceProvider;
        this.localRepository = localRepository;
    }

    @Override
    public Observable<List<Race>> search(String query) {
        if (query == null || query.length() == 0) {
            return discoverRaces();
        } else {
            return searchGlobally(query);
        }
    }

    private Observable<List<Race>> discoverRaces() {
        return hostsSource
                .getHosts()
                .map(uri -> new RaceRemoteRepository(remoteServiceProvider.getService(uri)))
                .flatMap(repo -> repo.search(null))
                .scan(Collections.emptyMap(), this::indexMoreRaces)
                .withLatestFrom(
                        localRepository.getSavedRaces().map(this::indexRaces),
                        (inputRaces, savedRaces) -> mergeWithSaved(inputRaces, savedRaces, true))
                .map(map -> Stream.of(map.values()).sortBy(Race::getName).toList());
    }

    private Observable<List<Race>> searchGlobally(String query) {
        return Observable.just(HostDiscoveryGlobal.DEFAULT_URI)
                .map(uri -> new RaceRemoteRepository(remoteServiceProvider.getService(uri)))
                .flatMap(repo -> repo.search(query))
                .scan(Collections.emptyMap(), this::indexMoreRaces)
                .withLatestFrom(
                        localRepository.getSavedRaces().map(this::indexRaces),
                        (inputRaces, savedRaces) -> mergeWithSaved(inputRaces, savedRaces, false))
                .map(map -> Stream.of(map.values()).toList());
    }

    private Map<String, Race> indexMoreRaces(Map<String, Race> previousMap, List<Race> newRaces) {
        Map<String, Race> result = new HashMap<>(previousMap);
        for (Race race : newRaces) {
            result.put(race.getRaceId(), race);
        }
        return result;
    }

    private Map<String, Race> indexRaces(List<Race> newRaces) {
        return indexMoreRaces(Collections.emptyMap(), newRaces);
    }

    private Map<String, Race> mergeWithSaved(Map<String, Race> inputRaces, Map<String, Race> savedRaces, boolean includeAllSaved) {
        Map<String, Race> result = new HashMap<>(inputRaces.size());
        for (Race inputRace : inputRaces.values()) {
            Race savedRace = savedRaces.get(inputRace.getRaceId());
            Race mergedRace = mergeRaces(inputRace, savedRace);
            result.put(mergedRace.getRaceId(), mergedRace);
        }
        if (includeAllSaved) {
            for (Race inputRace : savedRaces.values()) {
                if (result.containsKey(inputRace.getRaceId())) continue;
                result.put(inputRace.getRaceId(), inputRace);
            }
        }
        return result;
    }

    private Race mergeRaces(Race found, Race saved) {
        if (saved == null) return found;
        return new Race(saved)
                .setName(found.getName())
                .setSince(found.getSince())
                .setUntil(found.getUntil());
    }

    @Override
    public Observable<List<Race>> getSavedRaces() {
        return localRepository.getSavedRaces();
    }

    @Override
    public Observable<Race> getSavedRace(String raceId) {
        return localRepository.getSavedRace(raceId);
    }

    @Override
    public Completable saveRace(Race race) {
        return localRepository.saveRace(race);
    }

    @Override
    public Completable forgetRace(String raceId) {
        return localRepository.forgetRace(raceId);
    }
}
