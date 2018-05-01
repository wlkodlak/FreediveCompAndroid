package net.wilczak.freedivecomp.android.remote.messages;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StartingListReportDto {
    @SerializedName("Title")
    private String title;
    @SerializedName("Entries")
    private List<StartingListReportEntryDto> entries;

    public String getTitle() {
        return title;
    }

    public StartingListReportDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public List<StartingListReportEntryDto> getEntries() {
        return entries;
    }

    public StartingListReportDto setEntries(List<StartingListReportEntryDto> entries) {
        this.entries = entries;
        return this;
    }
}
