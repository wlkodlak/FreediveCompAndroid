package net.wilczak.freedivecomp.android.remote.messages;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReportActualResultDto {
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

    public PerformanceDto getPerformance() {
        return performance;
    }

    public ReportActualResultDto setPerformance(PerformanceDto performance) {
        this.performance = performance;
        return this;
    }

    public List<PenalizationDto> getPenalizations() {
        return penalizations;
    }

    public ReportActualResultDto setPenalizations(List<PenalizationDto> penalizations) {
        this.penalizations = penalizations;
        return this;
    }

    public PerformanceDto getFinalPerformance() {
        return finalPerformance;
    }

    public ReportActualResultDto setFinalPerformance(PerformanceDto finalPerformance) {
        this.finalPerformance = finalPerformance;
        return this;
    }

    public String getCardResult() {
        return cardResult;
    }

    public ReportActualResultDto setCardResult(String cardResult) {
        this.cardResult = cardResult;
        return this;
    }

    public String getJudgeId() {
        return judgeId;
    }

    public ReportActualResultDto setJudgeId(String judgeId) {
        this.judgeId = judgeId;
        return this;
    }

    public String getJudgeComment() {
        return judgeComment;
    }

    public ReportActualResultDto setJudgeComment(String judgeComment) {
        this.judgeComment = judgeComment;
        return this;
    }
}
