package net.wilczak.freedivecomp.android.room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "rules")
public class RulesEntity {
    @PrimaryKey
    @NonNull
    private final String raceId;
    private String rulesJson;

    public RulesEntity(String raceId) {
        this.raceId = raceId;
    }

    public String getRaceId() {
        return raceId;
    }

    public String getRulesJson() {
        return rulesJson;
    }

    public void setRulesJson(String rulesJson) {
        this.rulesJson = rulesJson;
    }
}
