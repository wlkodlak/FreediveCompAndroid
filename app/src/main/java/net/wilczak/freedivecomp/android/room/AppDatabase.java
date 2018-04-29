package net.wilczak.freedivecomp.android.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import net.wilczak.freedivecomp.android.domain.race.Race;

@Database(entities = {Race.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract RaceDao raceDao();
}
