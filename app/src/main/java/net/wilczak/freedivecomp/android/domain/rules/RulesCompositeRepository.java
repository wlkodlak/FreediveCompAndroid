package net.wilczak.freedivecomp.android.domain.rules;

import net.wilczak.freedivecomp.android.domain.race.Race;
import net.wilczak.freedivecomp.android.remote.remoteservice.RemoteServiceProvider;
import net.wilczak.freedivecomp.android.remote.messages.RulesDto;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Completable;
import io.reactivex.Single;

public class RulesCompositeRepository implements RulesRepository {
    private static final Object NOTHING = new Object();

    private final RulesRepository localRepository;
    private final RemoteServiceProvider remoteServiceProvider;
    private final ConcurrentHashMap<String, Object> refreshingRaces;

    public RulesCompositeRepository(RulesRepository localRepository, RemoteServiceProvider remoteServiceProvider) {
        this.localRepository = localRepository;
        this.remoteServiceProvider = remoteServiceProvider;
        this.refreshingRaces = new ConcurrentHashMap<>();
    }

    @Override
    public Single<List<RulesDto>> getRules(Race race) {
        boolean needsRefresh = this.refreshingRaces.contains(race.getRaceId());
        if (needsRefresh) {
            return getRemoteRulesAndSave(race);
        } else {
            return getLocalRules(race)
                    .flatMap(list -> list.size() > 0 ? Single.just(list) : getRemoteRulesAndSave(race));
        }
    }

    private Single<List<RulesDto>> getRemoteRulesAndSave(Race race) {
        return getRemoteRules(race)
                .flatMap(list -> saveRulesAndPassThem(race, list));
    }

    private Single<List<RulesDto>> getRemoteRules(Race race) {
        return remoteServiceProvider.getService(race.getUri()).getRules();
    }

    private Single<List<RulesDto>> getLocalRules(Race race) {
        return localRepository.getRules(race);
    }

    @Override
    public Completable saveRules(Race race, List<RulesDto> rules) {
        return localRepository
                .saveRules(race, rules)
                .doOnComplete(() -> refreshingRaces.remove(race.getRaceId()));
    }

    private Single<List<RulesDto>> saveRulesAndPassThem(Race race, List<RulesDto> rules) {
        return saveRules(race, rules).andThen(Single.just(rules));
    }

    @Override
    public void requestRefresh(Race race) {
        refreshingRaces.put(race.getRaceId(), NOTHING);
    }
}
