package net.wilczak.freedivecomp.android.room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

@Entity(tableName = "start")
public class StartEntity {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long id;
    @NonNull
    private String raceId;
    @NonNull
    private String athleteId;
    @NonNull
    private String disciplineId;
    @NonNull
    private String resultStatus;
    @NonNull
    private String startJson;
    @Nullable
    private String resultJson;

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

    @Nullable
    public String getResultJson() {
        return resultJson;
    }

    public void setResultJson(@Nullable String resultJson) {
        this.resultJson = resultJson;
    }
}
