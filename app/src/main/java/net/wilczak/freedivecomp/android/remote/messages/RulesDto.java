package net.wilczak.freedivecomp.android.remote.messages;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RulesDto {
    @SerializedName("Name")
    private String name;
    @SerializedName("HasDuration")
    private Boolean hasDuration;
    @SerializedName("HasDistance")
    private Boolean hasDistance;
    @SerializedName("HasDepth")
    private Boolean hasDepth;
    @SerializedName("HasPoints")
    private Boolean hasPoints;
    @SerializedName("PrimaryComponent")
    private String primaryComponent;
    @SerializedName("PenalizationsTarget")
    private String penalizationsTarget;
    @SerializedName("Penalizations")
    private List<RulesPenalizationDto> penalizations;
    @SerializedName("PointsCalculation")
    private CalculationDto PointsCalculation;
    @SerializedName("ShortCalculation")
    private CalculationDto ShortCalculation;

    public String getName() {
        return name;
    }

    public RulesDto setName(String name) {
        this.name = name;
        return this;
    }

    public Boolean getHasDuration() {
        return hasDuration;
    }

    public RulesDto setHasDuration(Boolean hasDuration) {
        this.hasDuration = hasDuration;
        return this;
    }

    public Boolean getHasDistance() {
        return hasDistance;
    }

    public RulesDto setHasDistance(Boolean hasDistance) {
        this.hasDistance = hasDistance;
        return this;
    }

    public Boolean getHasDepth() {
        return hasDepth;
    }

    public RulesDto setHasDepth(Boolean hasDepth) {
        this.hasDepth = hasDepth;
        return this;
    }

    public Boolean getHasPoints() {
        return hasPoints;
    }

    public RulesDto setHasPoints(Boolean hasPoints) {
        this.hasPoints = hasPoints;
        return this;
    }

    public String getPrimaryComponent() {
        return primaryComponent;
    }

    public RulesDto setPrimaryComponent(String primaryComponent) {
        this.primaryComponent = primaryComponent;
        return this;
    }

    public String getPenalizationsTarget() {
        return penalizationsTarget;
    }

    public RulesDto setPenalizationsTarget(String penalizationsTarget) {
        this.penalizationsTarget = penalizationsTarget;
        return this;
    }

    public List<RulesPenalizationDto> getPenalizations() {
        return penalizations;
    }

    public RulesDto setPenalizations(List<RulesPenalizationDto> penalizations) {
        this.penalizations = penalizations;
        return this;
    }

    public CalculationDto getPointsCalculation() {
        return PointsCalculation;
    }

    public RulesDto setPointsCalculation(CalculationDto pointsCalculation) {
        PointsCalculation = pointsCalculation;
        return this;
    }

    public CalculationDto getShortCalculation() {
        return ShortCalculation;
    }

    public RulesDto setShortCalculation(CalculationDto shortCalculation) {
        ShortCalculation = shortCalculation;
        return this;
    }
}
