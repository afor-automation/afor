package nz.co.afor.reports;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gherkin.ast.Scenario;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 15/03/2016.
 */
public class ScenarioResult {
    @JsonIgnore
    private final ZonedDateTime startTime;
    @JsonIgnore
    private final Scenario scenario;
    private List<StepResult> steps = new ArrayList<>();

    ScenarioResult(Scenario scenario) {
        this.scenario = scenario;
        this.startTime = ZonedDateTime.now(ZoneId.of("UTC"));
    }

    @JsonIgnore
    public Scenario getScenario() {
        return scenario;
    }

    public String getName() {
        return scenario.getName();
    }

    public List<StepResult> getSteps() {
        return steps;
    }

    @JsonIgnore
    ZonedDateTime getStartTime() {
        return startTime;
    }

    public long getDuration() {
        long duration = 0L;
        for (StepResult result : steps)
            duration += result.getDuration();
        return duration;
    }
}
