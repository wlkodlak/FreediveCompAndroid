package net.wilczak.freedivecomp.android.domain.rules;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.wilczak.freedivecomp.android.domain.race.Race;
import net.wilczak.freedivecomp.android.remote.messages.RulesDto;
import net.wilczak.freedivecomp.android.room.AppDatabase;
import net.wilczak.freedivecomp.android.room.RulesEntity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public class RulesLocalRepository implements RulesRepository {
    private final AppDatabase database;
    private final Gson gson;

    public RulesLocalRepository(AppDatabase database) {
        this.database = database;
        this.gson = new Gson();
    }

    @Override
    public Single<List<RulesDto>> getRules(Race race) {
        return Single.fromCallable(() -> {
            RulesEntity entity = database.rulesDao().get(race.getRaceId());
            if (entity == null) return new ArrayList<>();
            return gson.fromJson(entity.getRulesJson(), getListType());
        });
    }

    @Override
    public Completable saveRules(Race race, List<RulesDto> rules) {
        return Completable.fromAction(() -> {
            RulesEntity entity = new RulesEntity(race.getRaceId());
            entity.setRulesJson(gson.toJson(rules, getListType()));
            database.rulesDao().insert(entity);
        });
    }

    @Override
    public void requestRefresh(Race race) {
    }

    private Type getListType() {
        return new TypeToken<ArrayList<RulesDto>>() {
        }.getType();
    }
}
