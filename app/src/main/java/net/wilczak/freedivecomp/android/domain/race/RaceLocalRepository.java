package net.wilczak.freedivecomp.android.domain.race;

import com.annimon.stream.Stream;

import net.wilczak.freedivecomp.android.room.AppDatabase;
import net.wilczak.freedivecomp.android.room.RaceEntity;
import net.wilczak.freedivecomp.android.rxutils.ValueCacheObservable;

import org.joda.time.DateTime;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class RaceLocalRepository implements RaceRepository {
    private final AppDatabase database;
    private final PublishSubject<String> onChange;
    private final Observable<List<Race>> observableSavedRaces;

    @Inject
    public RaceLocalRepository(AppDatabase database) {
        this.database = database;
        this.onChange = PublishSubject.create();
        this.observableSavedRaces = Observable
                .fromCallable(() -> fromEntities(database.raceDao().getAll()))
                .compose(ValueCacheObservable.transform(onChange));
    }

    @Override
    public Observable<List<Race>> search(String query) {
        return Observable.error(new UnsupportedOperationException("Cannot search on RaceLocalRepository"));
    }

    @Override
    public Observable<List<Race>> getSavedRaces() {
        return observableSavedRaces;
    }

    @Override
    public Observable<Race> getSavedRace(final String raceId) {
        return getSavedRaces().map(l -> extractRace(l, raceId));
    }

    private Race extractRace(List<Race> list, String raceId) {
        if (list.size() == 0) return Race.MISSING;
        for (Race race : list) {
            if (raceId.equals(race.getRaceId())) return race;
        }
        return Race.MISSING;
    }

    @Override
    public Completable saveRace(final Race race) {
        return Completable.create(emitter -> {
            try {
                database.raceDao().insert(toEntity(race));
                onChange.onNext(race.getRaceId());
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    @Override
    public Completable forgetRace(String raceId) {
        return Completable.create(emitter -> {
            try {
                database.raceDao().delete(new RaceEntity(raceId));
                onChange.onNext(raceId);
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    private RaceEntity toEntity(Race race) {
        return new RaceEntity(race.getRaceId())
                .setAuthenticationToken(race.getAuthenticationToken())
                .setConnectCode(race.getConnectCode())
                .setName(race.getName())
                .setSelected(race.isSelected())
                .setSince(race.getSince().getMillis())
                .setUntil(race.getUntil().getMillis())
                .setUri(race.getUri());
    }

    private Race fromEntity(RaceEntity race) {
        return new Race(race.getRaceId())
                .setAuthenticationToken(race.getAuthenticationToken())
                .setConnectCode(race.getConnectCode())
                .setName(race.getName())
                .setSelected(race.isSelected())
                .setSince(new DateTime(race.getSince()))
                .setUntil(new DateTime(race.getUntil()))
                .setUri(race.getUri());
    }

    private List<Race> fromEntities(List<RaceEntity> entities) {
        return Stream.of(entities).map(this::fromEntity).toList();
    }
}
