package net.wilczak.freedivecomp.android.domain.race;

import com.annimon.stream.Stream;

import net.wilczak.freedivecomp.android.remote.messages.RaceSearchResultDto;
import net.wilczak.freedivecomp.android.remote.remoteservice.RemoteService;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class RaceRemoteRepository implements RaceRepository {
    private final RemoteService remoteService;

    public RaceRemoteRepository(RemoteService remoteService) {
        this.remoteService = remoteService;
    }

    @Override
    public Observable<List<Race>> search(String query) {
        return remoteService
                .getGlobalSearch(query)
                .toObservable()
                .map(raceDtos -> Stream.of(raceDtos).map(this::toRace).toList());
    }

    private Race toRace(RaceSearchResultDto dto) {
        return new Race(dto.getRaceId())
                .setUri(remoteService.getUri())
                .setName(dto.getName())
                .setSince(dto.getStart())
                .setUntil(dto.getEnd());
    }

    @Override
    public Observable<List<Race>> getSavedRaces() {
        return Observable.error(new UnsupportedOperationException("Cannot get saved races on network repository"));
    }

    @Override
    public Observable<Race> getSavedRace(String raceId) {
        return Observable.error(new UnsupportedOperationException("Cannot get saved races on network repository"));
    }

    @Override
    public Completable saveRace(Race race) {
        return Completable.error(new UnsupportedOperationException("Cannot saved race on network repository"));
    }

    @Override
    public Completable forgetRace(String raceId) {
        return Completable.error(new UnsupportedOperationException("Cannot saved race on network repository"));
    }
}
