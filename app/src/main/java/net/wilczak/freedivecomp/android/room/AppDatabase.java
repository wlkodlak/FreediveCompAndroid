package net.wilczak.freedivecomp.android.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {RaceEntity.class, RulesEntity.class, StartEntity.class, StartingLanesEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract RaceDao raceDao();

    public abstract RulesDao rulesDao();

    public abstract StartDao startDao();

    public abstract StartingLanesDao startingLanesDao();
}
