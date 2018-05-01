package net.wilczak.freedivecomp.android.remote.messages;

import com.google.gson.annotations.SerializedName;

public class ReportDisciplineDto {
    @SerializedName("DisciplineId")
    private String disciplineId;
    @SerializedName("Name")
    private String name;

    public String getDisciplineId() {
        return disciplineId;
    }

    public ReportDisciplineDto setDisciplineId(String disciplineId) {
        this.disciplineId = disciplineId;
        return this;
    }

    public String getName() {
        return name;
    }

    public ReportDisciplineDto setName(String name) {
        this.name = name;
        return this;
    }
}
