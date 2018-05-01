package net.wilczak.freedivecomp.android.remote.messages;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

public class RaceSettingsDto {
    @SerializedName("RaceId")
    private String raceId;
    @SerializedName("Name")
    private String name;
    @SerializedName("Start")
    private DateTime start;
    @SerializedName("Until")
    private DateTime end;

    public String getRaceId() {
        return raceId;
    }

    public RaceSettingsDto setRaceId(String raceId) {
        this.raceId = raceId;
        return this;
    }

    public String getName() {
        return name;
    }

    public RaceSettingsDto setName(String name) {
        this.name = name;
        return this;
    }

    public DateTime getStart() {
        return start;
    }

    public RaceSettingsDto setStart(DateTime start) {
        this.start = start;
        return this;
    }

    public DateTime getEnd() {
        return end;
    }

    public RaceSettingsDto setEnd(DateTime end) {
        this.end = end;
        return this;
    }
}
