package net.wilczak.freedivecomp.android.room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "startinglanes")
public class StartingLanesEntity {
    @PrimaryKey
    private final String raceId;
    private String lanesJson;

    public StartingLanesEntity(String raceId) {
        this.raceId = raceId;
    }

    public String getRaceId() {
        return raceId;
    }

    public String getLanesJson() {
        return lanesJson;
    }

    public StartingLanesEntity setLanesJson(String lanesJson) {
        this.lanesJson = lanesJson;
        return this;
    }
}
