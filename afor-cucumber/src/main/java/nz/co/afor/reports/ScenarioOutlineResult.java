package nz.co.afor.reports;

import gherkin.ast.ScenarioOutline;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 18/04/2016.
 */
public class ScenarioOutlineResult {
    private final ZonedDateTime startTime;
    ScenarioOutline scenarioOutline;
    List<StepResult> steps = new ArrayList<>();

    public ScenarioOutlineResult(ScenarioOutline scenario) {
        this.scenarioOutline = scenario;
        this.startTime = ZonedDateTime.now(ZoneId.of("UTC"));
    }

    public ScenarioOutline getScenario() {
        return scenarioOutline;
    }

    public void setScenario(ScenarioOutline scenario) {
        this.scenarioOutline = scenario;
    }

    public List<StepResult> getSteps() {
        return steps;
    }

    public void setSteps(List<StepResult> steps) {
        this.steps = steps;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public Long getDuration() {
        Long duration = 0L;
        for(StepResult result : steps)
            duration += result.getResult().getDuration();
        return duration;
    }
}
