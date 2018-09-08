package net.wilczak.freedivecomp.android.remote.messages;

import com.google.gson.annotations.SerializedName;

public class ReportDisciplineDto {
    @SerializedName("DisciplineId")
    private String disciplineId;
    @SerializedName("Name")
    private String name;
    @SerializedName("Rules")
    private String rules;

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

    public String getRules() {
        return rules;
    }

    public ReportDisciplineDto setRules(String rules) {
        this.rules = rules;
        return this;
    }
}
