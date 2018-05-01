package net.wilczak.freedivecomp.android.remote.messages;

import com.google.gson.annotations.SerializedName;

public class CalculationDto {
    @SerializedName("Operation")
    private String operation;
    @SerializedName("Constant")
    private Double constant;
    @SerializedName("Variable")
    private String variable;
    @SerializedName("ArgumentA")
    private CalculationDto argumentA;
    @SerializedName("ArgumentB")
    private CalculationDto argumentB;

    public String getOperation() {
        return operation;
    }

    public CalculationDto setOperation(String operation) {
        this.operation = operation;
        return this;
    }

    public Double getConstant() {
        return constant;
    }

    public CalculationDto setConstant(Double constant) {
        this.constant = constant;
        return this;
    }

    public String getVariable() {
        return variable;
    }

    public CalculationDto setVariable(String variable) {
        this.variable = variable;
        return this;
    }

    public CalculationDto getArgumentA() {
        return argumentA;
    }

    public CalculationDto setArgumentA(CalculationDto argumentA) {
        this.argumentA = argumentA;
        return this;
    }

    public CalculationDto getArgumentB() {
        return argumentB;
    }

    public CalculationDto setArgumentB(CalculationDto argumentB) {
        this.argumentB = argumentB;
        return this;
    }
}
