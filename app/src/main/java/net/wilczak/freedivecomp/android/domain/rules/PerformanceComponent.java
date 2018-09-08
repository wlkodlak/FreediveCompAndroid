package net.wilczak.freedivecomp.android.domain.rules;

import net.wilczak.freedivecomp.android.remote.messages.PerformanceDto;

public class PerformanceComponent {
    public static final PerformanceComponent Unknown = new PerformanceComponent(0);
    public static final PerformanceComponent Distance = new PerformanceComponent(1);
    public static final PerformanceComponent Depth = new PerformanceComponent(2);
    public static final PerformanceComponent Duration = new PerformanceComponent(3);
    public static final PerformanceComponent Points = new PerformanceComponent(4);

    private final int index;

    private PerformanceComponent(int index) {
        this.index = index;
    }

    public static PerformanceComponent parse(String name) {
        switch (name) {
            case "Distance":
                return Distance;
            case "Depth":
                return Depth;
            case "Duration":
                return Duration;
            case "Points":
                return Points;
            default:
                return Unknown;
        }
    }

    @Override
    public String toString() {
        switch (index) {
            case 1:
                return "Distance";
            case 2:
                return "Depth";
            case 3:
                return "Duration";
            case 4:
                return "Points";
            default:
                return "???";
        }
    }

    public Double get(PerformanceDto performanceDto) {
        if (performanceDto == null) return null;
        switch (index) {
            case 1:
                return performanceDto.getDistance();
            case 2:
                return performanceDto.getDepth();
            case 3:
                return (double) performanceDto.getDuration().getStandardSeconds();
            case 4:
                return performanceDto.getPoints();
            default:
                return null;
        }
    }

    public void set(PerformanceDto performanceDto, Double value) {
        if (performanceDto != null) {
            switch (index) {
                case 1:
                    performanceDto.setDistance(value);
                    break;
                case 2:
                    performanceDto.setDepth(value);
                    break;
                case 3:
                    if (value == null) {
                        performanceDto.setDuration(null);
                    } else {
                        performanceDto.setDuration(org.joda.time.Duration.standardSeconds((long) (double) value));
                    }
                    break;
                case 4:
                    performanceDto.setPoints(value);
                    break;
            }
        }
    }

    public static PerformanceComponent detect(PerformanceDto performanceDto) {
        if (performanceDto == null) return null;
        if (performanceDto.getDistance() != null) return Distance;
        if (performanceDto.getDepth() != null) return Depth;
        if (performanceDto.getDuration() != null) return Duration;
        return Unknown;
    }
}
