package net.wilczak.freedivecomp.android.retrofit;

import com.google.gson.annotations.SerializedName;

public class DisciplineDto {
    @SerializedName("DisciplineId")
    private String disciplineId;
    @SerializedName("ShortName")
    private String shortName;
    @SerializedName("LongName")
    private String longName;
    @SerializedName("Rules")
    private String rules;
    @SerializedName("AnnouncementsClosed")
    private Boolean announcementsClosed;

    public String getDisciplineId() {
        return disciplineId;
    }

    public DisciplineDto setDisciplineId(String disciplineId) {
        this.disciplineId = disciplineId;
        return this;
    }

    public String getShortName() {
        return shortName;
    }

    public DisciplineDto setShortName(String shortName) {
        this.shortName = shortName;
        return this;
    }

    public String getLongName() {
        return longName;
    }

    public DisciplineDto setLongName(String longName) {
        this.longName = longName;
        return this;
    }

    public String getRules() {
        return rules;
    }

    public DisciplineDto setRules(String rules) {
        this.rules = rules;
        return this;
    }

    public Boolean getAnnouncementsClosed() {
        return announcementsClosed;
    }

    public DisciplineDto setAnnouncementsClosed(Boolean announcementsClosed) {
        this.announcementsClosed = announcementsClosed;
        return this;
    }
}
