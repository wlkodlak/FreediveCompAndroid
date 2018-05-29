package net.wilczak.freedivecomp.android.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public abstract class StartDao {
    @Insert(onConflict = REPLACE)
    public abstract long insert(StartEntity entity);
    @Delete
    public abstract void delete(StartEntity entity);
    @Query("SELECT * FROM start WHERE raceId = :raceId")
    public abstract List<StartEntity> getAllForRace(String raceId);
    @Query("SELECT * FROM start WHERE id = :id LIMIT 1")
    public abstract StartEntity getById(long id);
    @Query("SELECT * FROM start WHERE raceId = :raceId AND athleteId = :athleteId AND disciplineId = :disciplineId LIMIT 1")
    public abstract StartEntity getByAthlete(String raceId, String athleteId, String disciplineId);

}
