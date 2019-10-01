package nz.co.afor.reports;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gherkin.ast.ScenarioOutline;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 18/04/2016.
 */
public class ScenarioOutlineResult implements GenericScenarioResult {
    @JsonIgnore
    private final ZonedDateTime startTime;
    private final String name;
    private List<StepResult> steps = new ArrayList<>();

    ScenarioOutlineResult(ScenarioOutline scenario) {
        this.name = scenario.getName();
        this.startTime = ZonedDateTime.now(ZoneId.of("UTC"));
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<StepResult> getSteps() {
        return steps;
    }

    public void setSteps(List<StepResult> steps) {
        this.steps = steps;
    }

    @JsonIgnore
    ZonedDateTime getStartTime() {
        return startTime;
    }

    @Override
    public long getDuration() {
        long duration = 0L;
        for (StepResult result : steps)
            duration += result.getDuration();
        return duration;
    }
}
