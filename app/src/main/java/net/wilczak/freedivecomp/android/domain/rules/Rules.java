package net.wilczak.freedivecomp.android.domain.rules;

import net.wilczak.freedivecomp.android.remote.messages.PenalizationDto;
import net.wilczak.freedivecomp.android.remote.messages.PerformanceDto;
import net.wilczak.freedivecomp.android.remote.messages.RulesDto;
import net.wilczak.freedivecomp.android.remote.messages.RulesPenalizationDto;

import java.util.ArrayList;
import java.util.List;

public class Rules {
    private String name;
    private boolean hasDistance, hasDuration, hasDepth, hasPoints;
    private List<RulesPenalization> penalizations;
    private PerformanceComponent primaryComponent, penalizationComponent;
    private RulesCalculation shortCalculation, pointsCalculation;

    public Rules(RulesDto dto) {
        this.name = dto.getName();
        this.hasDepth = dto.getHasDepth();
        this.hasDistance = dto.getHasDistance();
        this.hasDuration = dto.getHasDuration();
        this.hasPoints = dto.getHasPoints();
        this.primaryComponent = PerformanceComponent.parse(dto.getPrimaryComponent());
        this.penalizationComponent = PerformanceComponent.parse(dto.getPenalizationsTarget());
        this.pointsCalculation = RulesCalculation.parse(dto.getPointsCalculation());
        this.shortCalculation = RulesCalculation.parse(dto.getShortCalculation());
        this.penalizations = new ArrayList<>();
        for (RulesPenalizationDto penalizationDto : dto.getPenalizations()) {
            this.penalizations.add(new RulesPenalization(penalizationDto, penalizationComponent));
        }
    }

    public String getName() {
        return name;
    }

    public boolean getHasDistance() {
        return hasDistance;
    }

    public boolean getHasDuration() {
        return hasDuration;
    }

    public boolean getHasDepth() {
        return hasDepth;
    }

    public boolean getHasPoints() {
        return hasPoints;
    }

    public List<RulesPenalization> getPenalizations() {
        return penalizations;
    }

    public PerformanceComponent getPrimaryComponent() {
        return primaryComponent;
    }

    public PerformanceComponent getPenalizationComponent() {
        return penalizationComponent;
    }

    public PenalizationDto buildShortPenalization(PerformanceDto announced, PerformanceDto realized) {
        Double announcedValue = primaryComponent.get(announced);
        Double realizedValue = primaryComponent.get(realized);
        if (announcedValue == null || realizedValue == null) return null;
        if (announcedValue <= realizedValue) return null;
        PenalizationDto penalizationDto = new PenalizationDto()
                .setPenalizationId("Short")
                .setShortReason("")
                .setShortPerformance(true)
                .setReason("Short performance")
                .setPerformance(new PerformanceDto());
        RulesCalculationVariables calculationVariables = new RulesCalculationVariables().withAnnounced(announced).withRealized(realized);
        double penalty = shortCalculation.evaluate(calculationVariables);
        penalizationComponent.set(penalizationDto.getPerformance(), penalty);
        return penalizationDto;
    }

    public double calculatePoints(PerformanceDto realized) {
        RulesCalculationVariables calculationVariables = new RulesCalculationVariables().withRealized(realized);
        return pointsCalculation.evaluate(calculationVariables);
    }
}


