package net.wilczak.freedivecomp.android.retrofit;

import com.google.gson.annotations.SerializedName;

import org.joda.time.Duration;

public class PerformanceDto {
    @SerializedName("Duration")
    private Duration duration;
    @SerializedName("Depth")
    private Double depth;
    @SerializedName("Distance")
    private Double distance;
    @SerializedName("Points")
    private Double points;

    public Duration getDuration() {
        return duration;
    }

    public PerformanceDto setDuration(Duration duration) {
        this.duration = duration;
        return this;
    }

    public Double getDepth() {
        return depth;
    }

    public PerformanceDto setDepth(Double depth) {
        this.depth = depth;
        return this;
    }

    public Double getDistance() {
        return distance;
    }

    public PerformanceDto setDistance(Double distance) {
        this.distance = distance;
        return this;
    }

    public Double getPoints() {
        return points;
    }

    public PerformanceDto setPoints(Double points) {
        this.points = points;
        return this;
    }
}
