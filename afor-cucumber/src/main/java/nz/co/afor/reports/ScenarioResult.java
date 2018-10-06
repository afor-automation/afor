package nz.co.afor.reports;

import gherkin.ast.Scenario;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 15/03/2016.
 */
public class ScenarioResult {
    private final ZonedDateTime startTime;
    Scenario scenario;
    List<StepResult> steps = new ArrayList<>();

    public ScenarioResult(Scenario scenario) {
        this.scenario = scenario;
        this.startTime = ZonedDateTime.now(ZoneId.of("UTC"));
    }

    public Scenario getScenario() {
        return scenario;
    }

    public void setScenario(Scenario scenario) {
        this.scenario = scenario;
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
}
