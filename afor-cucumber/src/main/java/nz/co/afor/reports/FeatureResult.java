package nz.co.afor.reports;

import gherkin.formatter.model.Feature;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 15/03/2016.
 */
public class FeatureResult {
    Feature feature;
    List<ScenarioResult> scenarios = new ArrayList<>();

    public FeatureResult(Feature feature) {
        this.feature = feature;
    }

    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    public List<ScenarioResult> getScenarios() {
        return scenarios;
    }

    public void setScenarios(List<ScenarioResult> scenarios) {
        this.scenarios = scenarios;
    }
}
