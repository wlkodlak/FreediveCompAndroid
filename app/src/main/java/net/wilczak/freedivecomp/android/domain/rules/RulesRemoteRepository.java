package net.wilczak.freedivecomp.android.domain.rules;

import net.wilczak.freedivecomp.android.domain.race.Race;
import net.wilczak.freedivecomp.android.remote.messages.RulesDto;
import net.wilczak.freedivecomp.android.remote.remoteservice.RemoteServiceProvider;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public class RulesRemoteRepository implements RulesRepository {
    private final RemoteServiceProvider remoteService;

    public RulesRemoteRepository(RemoteServiceProvider remoteService) {
        this.remoteService = remoteService;
    }

    @Override
    public Single<List<RulesDto>> getRules(Race race) {
        return remoteService.getService(race.getUri()).getRules();
    }

    @Override
    public Completable saveRules(Race race, List<RulesDto> rules) {
        return Completable.error(new UnsupportedOperationException("Saving races not supported on remote interface"));
    }

    @Override
    public void requestRefresh(Race race) {
    }
}
