package net.wilczak.freedivecomp.android.room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "disciplines")
public class DisciplinesEntity {
    @PrimaryKey
    @NonNull
    private String raceId;
    private String disciplinesJson;

    public DisciplinesEntity(@NonNull String raceId) {
        this.raceId = raceId;
    }

    public void setDisciplinesJson(String disciplinesJson) {
        this.disciplinesJson = disciplinesJson;
    }

    @NonNull
    public String getRaceId() {
        return raceId;
    }

    public String getDisciplinesJson() {
        return disciplinesJson;
    }
}
