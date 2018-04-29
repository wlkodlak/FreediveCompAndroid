package net.wilczak.freedivecomp.android.retrofit;

import com.google.gson.annotations.SerializedName;

public class RulesPenalizationDto {
    @SerializedName("Id")
    private String id;
    @SerializedName("Reason")
    private String reason;
    @SerializedName("ShortReason")
    private String shortReason;
    @SerializedName("HasInput")
    private Boolean hasInput;
    @SerializedName("InputName")
    private String inputName;
    @SerializedName("InputUnit")
    private String inputUnit;
    @SerializedName("CardResult")
    private String cardResult;
    @SerializedName("Calculation")
    private CalculationDto calculation;

    public String getId() {
        return id;
    }

    public RulesPenalizationDto setId(String id) {
        this.id = id;
        return this;
    }

    public String getReason() {
        return reason;
    }

    public RulesPenalizationDto setReason(String reason) {
        this.reason = reason;
        return this;
    }

    public String getShortReason() {
        return shortReason;
    }

    public RulesPenalizationDto setShortReason(String shortReason) {
        this.shortReason = shortReason;
        return this;
    }

    public Boolean getHasInput() {
        return hasInput;
    }

    public RulesPenalizationDto setHasInput(Boolean hasInput) {
        this.hasInput = hasInput;
        return this;
    }

    public String getInputName() {
        return inputName;
    }

    public RulesPenalizationDto setInputName(String inputName) {
        this.inputName = inputName;
        return this;
    }

    public String getInputUnit() {
        return inputUnit;
    }

    public RulesPenalizationDto setInputUnit(String inputUnit) {
        this.inputUnit = inputUnit;
        return this;
    }

    public String getCardResult() {
        return cardResult;
    }

    public RulesPenalizationDto setCardResult(String cardResult) {
        this.cardResult = cardResult;
        return this;
    }

    public CalculationDto getCalculation() {
        return calculation;
    }

    public RulesPenalizationDto setCalculation(CalculationDto calculation) {
        this.calculation = calculation;
        return this;
    }
}
