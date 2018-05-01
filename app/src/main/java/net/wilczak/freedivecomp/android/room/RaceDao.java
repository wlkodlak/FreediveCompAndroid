package net.wilczak.freedivecomp.android.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface RaceDao {
    @Insert(onConflict = REPLACE)
    void insert(RaceEntity entity);
    @Delete
    void delete(RaceEntity entity);
    @Query("SELECT * FROM race")
    List<RaceEntity> getAll();
    @Query("SELECT * FROM race WHERE raceId = :raceId LIMIT 1")
    RaceEntity get(String raceId);
}
