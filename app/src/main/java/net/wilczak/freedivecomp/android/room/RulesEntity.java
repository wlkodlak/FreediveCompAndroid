package net.wilczak.freedivecomp.android.room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "rules")
public class RulesEntity {
    @PrimaryKey
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

    public RulesEntity setRulesJson(String rulesJson) {
        this.rulesJson = rulesJson;
        return this;
    }
}
