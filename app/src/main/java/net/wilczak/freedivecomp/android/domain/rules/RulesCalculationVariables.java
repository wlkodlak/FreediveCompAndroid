package net.wilczak.freedivecomp.android.domain.rules;

import net.wilczak.freedivecomp.android.remote.messages.PerformanceDto;

public class RulesCalculationVariables {
    private PerformanceDto realized;
    private PerformanceDto announced;
    private Double input;

    public RulesCalculationVariables withRealized(PerformanceDto performance) {
        this.realized = performance;
        return this;
    }

    public RulesCalculationVariables withAnnounced(PerformanceDto performance) {
        this.announced = performance;
        return this;
    }

    public RulesCalculationVariables withInput(double input) {
        this.input = input;
        return this;
    }

    public Double get(String name) {
        switch (name) {
            case "AnnouncedDuration":
                return PerformanceComponent.Duration.get(announced);
            case "AnnouncedDistance":
                return PerformanceComponent.Distance.get(announced);
            case "AnnouncedDepth":
                return PerformanceComponent.Depth.get(announced);
            case "RealizedDuration":
                return PerformanceComponent.Duration.get(realized);
            case "RealizedDistance":
                return PerformanceComponent.Distance.get(realized);
            case "RealizedDepth":
                return PerformanceComponent.Depth.get(realized);
            case "Input":
                return input;
            default:
                return null;
        }
    }
}
