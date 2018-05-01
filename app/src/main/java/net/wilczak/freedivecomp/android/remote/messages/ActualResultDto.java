package net.wilczak.freedivecomp.android.remote.messages;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ActualResultDto {
    @SerializedName("DisciplineId")
    private String disciplineId;
    @SerializedName("Performance")
    private PerformanceDto performance;
    @SerializedName("Penalizations")
    private List<PenalizationDto> penalizations;
    @SerializedName("FinalPerformance")
    private PerformanceDto finalPerformance;
    @SerializedName("CardResult")
    private String cardResult;
    @SerializedName("JudgeId")
    private String judgeId;
    @SerializedName("JudgeComment")
    private String judgeComment;
    @SerializedName("JudgeOverride")
    private Boolean judgeOverride;

    public String getDisciplineId() {
        return disciplineId;
    }

    public ActualResultDto setDisciplineId(String disciplineId) {
        this.disciplineId = disciplineId;
        return this;
    }

    public PerformanceDto getPerformance() {
        return performance;
    }

    public ActualResultDto setPerformance(PerformanceDto performance) {
        this.performance = performance;
        return this;
    }

    public List<PenalizationDto> getPenalizations() {
        return penalizations;
    }

    public ActualResultDto setPenalizations(List<PenalizationDto> penalizations) {
        this.penalizations = penalizations;
        return this;
    }

    public PerformanceDto getFinalPerformance() {
        return finalPerformance;
    }

    public ActualResultDto setFinalPerformance(PerformanceDto finalPerformance) {
        this.finalPerformance = finalPerformance;
        return this;
    }

    public String getCardResult() {
        return cardResult;
    }

    public ActualResultDto setCardResult(String cardResult) {
        this.cardResult = cardResult;
        return this;
    }

    public String getJudgeId() {
        return judgeId;
    }

    public ActualResultDto setJudgeId(String judgeId) {
        this.judgeId = judgeId;
        return this;
    }

    public String getJudgeComment() {
        return judgeComment;
    }

    public ActualResultDto setJudgeComment(String judgeComment) {
        this.judgeComment = judgeComment;
        return this;
    }

    public Boolean getJudgeOverride() {
        return judgeOverride;
    }

    public ActualResultDto setJudgeOverride(Boolean judgeOverride) {
        this.judgeOverride = judgeOverride;
        return this;
    }
}
