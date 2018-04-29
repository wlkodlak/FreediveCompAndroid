package net.wilczak.freedivecomp.android.retrofit;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

public class RaceSearchResultDto {
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

    public RaceSearchResultDto setRaceId(String raceId) {
        this.raceId = raceId;
        return this;
    }

    public String getName() {
        return name;
    }

    public RaceSearchResultDto setName(String name) {
        this.name = name;
        return this;
    }

    public DateTime getStart() {
        return start;
    }

    public RaceSearchResultDto setStart(DateTime start) {
        this.start = start;
        return this;
    }

    public DateTime getEnd() {
        return end;
    }

    public RaceSearchResultDto setEnd(DateTime end) {
        this.end = end;
        return this;
    }
}
