package net.wilczak.freedivecomp.android.remote.messages;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

public class ReportStartTimesDto {
    @SerializedName("StartingLaneId")
    private String startingLaneId;
    @SerializedName("StartingLaneLongName")
    private String startingLaneLongName;
    @SerializedName("WarmUpTime")
    private DateTime warmUpTime;
    @SerializedName("OfficialTop")
    private DateTime officialTop;

    public String getStartingLaneId() {
        return startingLaneId;
    }

    public ReportStartTimesDto setStartingLaneId(String startingLaneId) {
        this.startingLaneId = startingLaneId;
        return this;
    }

    public String getStartingLaneLongName() {
        return startingLaneLongName;
    }

    public ReportStartTimesDto setStartingLaneLongName(String startingLaneLongName) {
        this.startingLaneLongName = startingLaneLongName;
        return this;
    }

    public DateTime getWarmUpTime() {
        return warmUpTime;
    }

    public ReportStartTimesDto setWarmUpTime(DateTime warmUpTime) {
        this.warmUpTime = warmUpTime;
        return this;
    }

    public DateTime getOfficialTop() {
        return officialTop;
    }

    public ReportStartTimesDto setOfficialTop(DateTime officialTop) {
        this.officialTop = officialTop;
        return this;
    }
}
