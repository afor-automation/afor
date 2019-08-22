package nz.co.afor.reports;

import gherkin.ast.Feature;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 15/03/2016.
 */
public class FeatureResult {
    private final String name;
    private List<ScenarioResult> scenarios = new ArrayList<>();
    private List<ScenarioOutlineResult> scenarioOutlines = new ArrayList<>();

    FeatureResult(Feature feature) {
        this.name = feature.getName();
    }

    public String getName() {
        return name;
    }

    public List<ScenarioResult> getScenarios() {
        return scenarios;
    }

    public List<ScenarioOutlineResult> getScenarioOutlines() {
        return scenarioOutlines;
    }
}
