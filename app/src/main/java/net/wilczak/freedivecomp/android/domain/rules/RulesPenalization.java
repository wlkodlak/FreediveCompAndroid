package net.wilczak.freedivecomp.android.domain.rules;

import net.wilczak.freedivecomp.android.remote.messages.PenalizationDto;
import net.wilczak.freedivecomp.android.remote.messages.PerformanceDto;
import net.wilczak.freedivecomp.android.remote.messages.RulesPenalizationDto;

public class RulesPenalization {
    private String id, reason, shortReason, inputName, inputUnit, cardResult;
    private RulesCalculation penaltyCalculation;
    private PerformanceComponent penaltyComponent;

    public RulesPenalization(RulesPenalizationDto dto, PerformanceComponent penaltyComponent) {
        this.id = dto.getId();
        this.reason = dto.getReason();
        this.shortReason = dto.getShortReason();
        this.inputName = dto.getInputName();
        this.inputUnit = dto.getInputUnit();
        this.cardResult = dto.getCardResult();
        this.penaltyCalculation = RulesCalculation.parse(dto.getCalculation());
        this.penaltyComponent = penaltyComponent;
    }

    public String getId() {
        return id;
    }

    public String getReason() {
        return reason;
    }

    public String getShortReason() {
        return shortReason;
    }

    public String getInputName() {
        return inputName;
    }

    public boolean getHasInput() {
        return inputName != null;
    }

    public String getInputUnit() {
        return inputUnit;
    }

    public String getCardResult() {
        return cardResult;
    }

    public PenalizationDto buildPenalization(double input, PerformanceDto realized) {
        PenalizationDto penalizationDto = new PenalizationDto()
                .setPenalizationId(id)
                .setReason(reason)
                .setShortReason(shortReason)
                .setShortPerformance(false)
                .setRuleInput(input)
                .setPerformance(new PerformanceDto());
        RulesCalculationVariables calculationVariables = new RulesCalculationVariables().withInput(input).withRealized(realized);
        Double penaltyValue = penaltyCalculation.evaluate(calculationVariables);
        penaltyComponent.set(penalizationDto.getPerformance(), penaltyValue);
        return penalizationDto;
    }
}
