package nz.co.afor.reports;

import gherkin.ast.ScenarioOutline;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 18/04/2016.
 */
public class ScenarioOutlineResult {
    ScenarioOutline scenarioOutline;
    List<StepResult> steps = new ArrayList<>();

    public ScenarioOutlineResult(ScenarioOutline scenario) {
        this.scenarioOutline = scenario;
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
}
