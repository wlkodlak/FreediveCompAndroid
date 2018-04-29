package net.wilczak.freedivecomp.android.room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "race")
public class RaceEntity {
    @PrimaryKey
    private String raceId;
    private String name;
    private long since;
    private long until;
    private String uri;
    private String connectCode;
    private String authenticationToken;
    private boolean selected;

    public RaceEntity(String raceId) {
        this.raceId = raceId;
    }

    public String getRaceId() {
        return raceId;
    }

    public String getName() {
        return name;
    }

    public RaceEntity setName(String name) {
        this.name = name;
        return this;
    }

    public long getSince() {
        return since;
    }

    public RaceEntity setSince(long since) {
        this.since = since;
        return this;
    }

    public long getUntil() {
        return until;
    }

    public RaceEntity setUntil(long until) {
        this.until = until;
        return this;
    }

    public String getUri() {
        return uri;
    }

    public RaceEntity setUri(String uri) {
        this.uri = uri;
        return this;
    }

    public String getConnectCode() {
        return connectCode;
    }

    public RaceEntity setConnectCode(String connectCode) {
        this.connectCode = connectCode;
        return this;
    }

    public String getAuthenticationToken() {
        return authenticationToken;
    }

    public RaceEntity setAuthenticationToken(String authenticationToken) {
        this.authenticationToken = authenticationToken;
        return this;
    }

    public boolean isSelected() {
        return selected;
    }

    public RaceEntity setSelected(boolean selected) {
        this.selected = selected;
        return this;
    }
}
