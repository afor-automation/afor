package nz.co.afor.reports.results;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.cucumber.plugin.event.Status;

import java.text.DecimalFormat;

import static java.lang.String.format;

public class ScenarioTimelineResult {
    public static final double MILLI_TO_SECONDS_MULTIPLIER = 0.001;
    private final String name;
    private final double durationInSeconds;
    private final Status status;
    private final long steps;

    public ScenarioTimelineResult(String name, Status status, long steps, long durationInMilliseconds) {
        this.name = format("%s (%d steps)", name, steps);
        this.status = status;
        this.steps = steps;
        this.durationInSeconds = Double.parseDouble(new DecimalFormat("0.000").format(durationInMilliseconds * MILLI_TO_SECONDS_MULTIPLIER));
    }

    public String getName() {
        return name;
    }

    public double getDurationInSeconds() {
        return durationInSeconds;
    }

    @JsonIgnore
    public Status getStatus() {
        return status;
    }

    @JsonIgnore
    public long getSteps() {
        return steps;
    }
}
