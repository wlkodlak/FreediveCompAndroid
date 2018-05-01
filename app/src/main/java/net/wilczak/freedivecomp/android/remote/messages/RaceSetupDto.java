package net.wilczak.freedivecomp.android.remote.messages;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RaceSetupDto {
    @SerializedName("Race")
    private RaceSettingsDto race;
    @SerializedName("StartingLanes")
    private List<StartingLaneDto> startingLanes;
    @SerializedName("Disciplines")
    private List<DisciplineDto> disciplines;

    public RaceSettingsDto getRace() {
        return race;
    }

    public RaceSetupDto setRace(RaceSettingsDto race) {
        this.race = race;
        return this;
    }

    public List<StartingLaneDto> getStartingLanes() {
        return startingLanes;
    }

    public RaceSetupDto setStartingLanes(List<StartingLaneDto> startingLanes) {
        this.startingLanes = startingLanes;
        return this;
    }

    public List<DisciplineDto> getDisciplines() {
        return disciplines;
    }

    public RaceSetupDto setDisciplines(List<DisciplineDto> disciplines) {
        this.disciplines = disciplines;
        return this;
    }
}
