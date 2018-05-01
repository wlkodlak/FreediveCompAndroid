package net.wilczak.freedivecomp.android.remote.messages;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StartingLaneDto {
    @SerializedName("StartingLaneId")
    private String startingLaneId;
    @SerializedName("ShortName")
    private String shortName;
    @SerializedName("SubLanes")
    private List<StartingLaneDto> subLanes;

    public String getStartingLaneId() {
        return startingLaneId;
    }

    public StartingLaneDto setStartingLaneId(String startingLaneId) {
        this.startingLaneId = startingLaneId;
        return this;
    }

    public String getShortName() {
        return shortName;
    }

    public StartingLaneDto setShortName(String shortName) {
        this.shortName = shortName;
        return this;
    }

    public List<StartingLaneDto> getSubLanes() {
        return subLanes;
    }

    public StartingLaneDto setSubLanes(List<StartingLaneDto> subLanes) {
        this.subLanes = subLanes;
        return this;
    }
}
