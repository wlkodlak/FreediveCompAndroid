package net.wilczak.freedivecomp.android.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface StartingLanesDao {
    @Insert(onConflict = REPLACE)
    void insert(StartingLanesEntity entity);
    @Delete
    void delete(StartingLanesEntity entity);
    @Query("SELECT * FROM startinglanes WHERE raceId = :raceId LIMIT 1")
    StartingLanesEntity get(String raceId);
}
