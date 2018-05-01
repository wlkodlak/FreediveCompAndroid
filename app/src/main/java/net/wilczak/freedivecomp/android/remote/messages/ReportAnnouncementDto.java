package net.wilczak.freedivecomp.android.remote.messages;

import com.google.gson.annotations.SerializedName;

public class ReportAnnouncementDto {
    @SerializedName("Performance")
    private PerformanceDto performance;
    @SerializedName("ModeratorNotes")
    private String moderatorNotes;

    public PerformanceDto getPerformance() {
        return performance;
    }

    public ReportAnnouncementDto setPerformance(PerformanceDto performance) {
        this.performance = performance;
        return this;
    }

    public String getModeratorNotes() {
        return moderatorNotes;
    }

    public ReportAnnouncementDto setModeratorNotes(String moderatorNotes) {
        this.moderatorNotes = moderatorNotes;
        return this;
    }
}
