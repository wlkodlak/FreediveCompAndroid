package net.wilczak.freedivecomp.android.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface RulesDao {
    @Insert(onConflict = REPLACE)
    void insert(RulesEntity entity);
    @Delete
    void delete(RulesEntity entity);
    @Query("SELECT * FROM rules WHERE raceId = :raceId LIMIT 1")
    RulesEntity get(String raceId);
}


