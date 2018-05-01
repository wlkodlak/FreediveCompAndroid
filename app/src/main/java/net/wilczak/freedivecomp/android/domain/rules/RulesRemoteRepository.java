package net.wilczak.freedivecomp.android.domain.rules;

import net.wilczak.freedivecomp.android.domain.race.Race;
import net.wilczak.freedivecomp.android.remote.remoteservice.RemoteService;
import net.wilczak.freedivecomp.android.remote.messages.RulesDto;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public class RulesRemoteRepository implements RulesRepository {
    private final RemoteService remoteService;

    public RulesRemoteRepository(RemoteService remoteService) {
        this.remoteService = remoteService;
    }

    @Override
    public Single<List<RulesDto>> getRules(Race race) {
        return remoteService.getRules();
    }

    @Override
    public Completable saveRules(Race race, List<RulesDto> rules) {
        return Completable.error(new UnsupportedOperationException("Saving races not supported on remote interface"));
    }

    @Override
    public void requestRefresh(Race race) {
    }
}
