package net.wilczak.freedivecomp.android.room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "start")
public class StartEntity {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @NonNull
    private String raceId;
    @NonNull
    private String startingLaneId;
    @NonNull
    private String athleteId;
    @NonNull
    private String disciplineId;
    @NonNull
    private String resultStatus;
    @NonNull
    private String startJson;

    public long getId() {
        return id;
    }

    public StartEntity setId(long id) {
        this.id = id;
        return this;
    }

    @NonNull
    public String getRaceId() {
        return raceId;
    }

    public StartEntity setRaceId(@NonNull String raceId) {
        this.raceId = raceId;
        return this;
    }

    @NonNull
    public String getStartingLaneId() {
        return startingLaneId;
    }

    public StartEntity setStartingLaneId(@NonNull String startingLaneId) {
        this.startingLaneId = startingLaneId;
        return this;
    }

    @NonNull
    public String getAthleteId() {
        return athleteId;
    }

    public StartEntity setAthleteId(@NonNull String athleteId) {
        this.athleteId = athleteId;
        return this;
    }

    @NonNull
    public String getDisciplineId() {
        return disciplineId;
    }

    public StartEntity setDisciplineId(@NonNull String disciplineId) {
        this.disciplineId = disciplineId;
        return this;
    }

    @NonNull
    public String getResultStatus() {
        return resultStatus;
    }

    public StartEntity setResultStatus(@NonNull String resultStatus) {
        this.resultStatus = resultStatus;
        return this;
    }

    @NonNull
    public String getStartJson() {
        return startJson;
    }

    public StartEntity setStartJson(@NonNull String startJson) {
        this.startJson = startJson;
        return this;
    }
}
