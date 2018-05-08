package net.wilczak.freedivecomp.android.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface DisciplinesDao {
    @Insert(onConflict = REPLACE)
    void insert(DisciplinesEntity entity);
    @Delete
    void delete(DisciplinesEntity entity);
    @Query("SELECT * FROM disciplines WHERE raceId = :raceId LIMIT 1")
    DisciplinesEntity get(String raceId);
}
