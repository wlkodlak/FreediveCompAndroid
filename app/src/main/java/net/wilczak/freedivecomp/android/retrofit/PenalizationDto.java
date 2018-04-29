package net.wilczak.freedivecomp.android.retrofit;

import com.google.gson.annotations.SerializedName;

public class PenalizationDto {
    @SerializedName("Reason")
    private String reason;
    @SerializedName("ShortReason")
    private String shortReason;
    @SerializedName("PenalizationId")
    private String penalizationId;
    @SerializedName("IsShortPerformance")
    private Boolean isShortPerformance;
    @SerializedName("Performance")
    private PerformanceDto performance;
    @SerializedName("RuleInput")
    private Double ruleInput;

    public String getReason() {
        return reason;
    }

    public PenalizationDto setReason(String reason) {
        this.reason = reason;
        return this;
    }

    public String getShortReason() {
        return shortReason;
    }

    public PenalizationDto setShortReason(String shortReason) {
        this.shortReason = shortReason;
        return this;
    }

    public String getPenalizationId() {
        return penalizationId;
    }

    public PenalizationDto setPenalizationId(String penalizationId) {
        this.penalizationId = penalizationId;
        return this;
    }

    public Boolean getShortPerformance() {
        return isShortPerformance;
    }

    public PenalizationDto setShortPerformance(Boolean shortPerformance) {
        isShortPerformance = shortPerformance;
        return this;
    }

    public PerformanceDto getPerformance() {
        return performance;
    }

    public PenalizationDto setPerformance(PerformanceDto performance) {
        this.performance = performance;
        return this;
    }

    public Double getRuleInput() {
        return ruleInput;
    }

    public PenalizationDto setRuleInput(Double ruleInput) {
        this.ruleInput = ruleInput;
        return this;
    }
}
