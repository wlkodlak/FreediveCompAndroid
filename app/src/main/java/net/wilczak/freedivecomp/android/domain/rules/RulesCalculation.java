package net.wilczak.freedivecomp.android.domain.rules;

import net.wilczak.freedivecomp.android.remote.messages.CalculationDto;

public abstract class RulesCalculation {
    abstract Double evaluate(RulesCalculationVariables variables);

    public static RulesCalculation parse(final CalculationDto dto) {
        if (dto == null) {
            return null;
        } else {
            switch (dto.getOperation()) {
                case "Constant":
                    return new RulesCalculationConstant(dto.getConstant());
                case "Variable":
                    return new RulesCalculationVariable(dto.getVariable());
                case "Ceiling":
                    return new RulesCalculationCeiling(parse(dto.getArgumentA()));
                case "Plus":
                case "Minus":
                case "Multiply":
                case "Divide":
                    return new RulesCalculationOperator(dto.getOperation(), parse(dto.getArgumentA()), parse(dto.getArgumentB()));
                default:
                    return null;
            }
        }
    }

    private static class RulesCalculationConstant extends RulesCalculation {
        private final Double constant;

        public RulesCalculationConstant(Double constant) {
            super();
            this.constant = constant;
        }

        @Override
        Double evaluate(RulesCalculationVariables variables) {
            return constant;
        }
    }

    private static class RulesCalculationVariable extends RulesCalculation {
        private final String variable;

        public RulesCalculationVariable(String variable) {
            super();
            this.variable = variable;
        }

        @Override
        Double evaluate(RulesCalculationVariables variables) {
            return variables.get(variable);
        }
    }

    private static class RulesCalculationCeiling extends RulesCalculation {
        private final RulesCalculation argument;

        public RulesCalculationCeiling(RulesCalculation argument) {
            super();
            this.argument = argument != null ? argument : new RulesCalculationConstant(null);
        }

        @Override
        Double evaluate(RulesCalculationVariables variables) {
            Double argumentValue = argument.evaluate(variables);
            if (argumentValue == null) return null;
            return Math.ceil(argumentValue);
        }
    }

    private static class RulesCalculationOperator extends RulesCalculation {
        private final String operation;
        private final RulesCalculation argumentA;
        private final RulesCalculation argumentB;

        public RulesCalculationOperator(String operation, RulesCalculation argumentA, RulesCalculation argumentB) {
            super();
            this.operation = operation;
            this.argumentA = argumentA;
            this.argumentB = argumentB;
        }

        @Override
        Double evaluate(RulesCalculationVariables variables) {
            Double valueA = argumentA.evaluate(variables);
            Double valueB = argumentB.evaluate(variables);
            if (valueA == null || valueB == null) return null;
            switch (operation) {
                case "Plus":
                    return valueA + valueB;
                case "Minus":
                    return valueA - valueB;
                case "Multiply":
                    return valueA * valueB;
                case "Divide":
                    return valueA / valueB;
                default:
                    return null;
            }
        }
    }
}

