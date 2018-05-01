package net.wilczak.freedivecomp.android.remote.messages;

import com.google.gson.annotations.SerializedName;

public class StartingListReportEntryDto {
    @SerializedName("Athlete")
    private AthleteProfileDto athlete;
    @SerializedName("Discipline")
    private ReportDisciplineDto discipline;
    @SerializedName("Announcement")
    private ReportAnnouncementDto announcement;
    @SerializedName("Start")
    private ReportStartTimesDto start;
    @SerializedName("CurrentResult")
    private ReportActualResultDto currentResult;

    public AthleteProfileDto getAthlete() {
        return athlete;
    }

    public StartingListReportEntryDto setAthlete(AthleteProfileDto athlete) {
        this.athlete = athlete;
        return this;
    }

    public ReportDisciplineDto getDiscipline() {
        return discipline;
    }

    public StartingListReportEntryDto setDiscipline(ReportDisciplineDto discipline) {
        this.discipline = discipline;
        return this;
    }

    public ReportAnnouncementDto getAnnouncement() {
        return announcement;
    }

    public StartingListReportEntryDto setAnnouncement(ReportAnnouncementDto announcement) {
        this.announcement = announcement;
        return this;
    }

    public ReportStartTimesDto getStart() {
        return start;
    }

    public StartingListReportEntryDto setStart(ReportStartTimesDto start) {
        this.start = start;
        return this;
    }

    public ReportActualResultDto getCurrentResult() {
        return currentResult;
    }

    public StartingListReportEntryDto setCurrentResult(ReportActualResultDto currentResult) {
        this.currentResult = currentResult;
        return this;
    }
}

