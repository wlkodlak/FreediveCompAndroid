package net.wilczak.freedivecomp.android.room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "start")
public class StartEntity {
    @PrimaryKey(autoGenerate = true)
    @NonNull
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

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getRaceId() {
        return raceId;
    }

    public void setRaceId(@NonNull String raceId) {
        this.raceId = raceId;
    }

    @NonNull
    public String getStartingLaneId() {
        return startingLaneId;
    }

    public void setStartingLaneId(@NonNull String startingLaneId) {
        this.startingLaneId = startingLaneId;
    }

    @NonNull
    public String getAthleteId() {
        return athleteId;
    }

    public void setAthleteId(@NonNull String athleteId) {
        this.athleteId = athleteId;
    }

    @NonNull
    public String getDisciplineId() {
        return disciplineId;
    }

    public void setDisciplineId(@NonNull String disciplineId) {
        this.disciplineId = disciplineId;
    }

    @NonNull
    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(@NonNull String resultStatus) {
        this.resultStatus = resultStatus;
    }

    @NonNull
    public String getStartJson() {
        return startJson;
    }

    public void setStartJson(@NonNull String startJson) {
        this.startJson = startJson;
    }
}
