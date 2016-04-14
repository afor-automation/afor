package nz.co.afor.reports.results;

/**
 * Created by Matt on 11/04/2016.
 */
public class ResultSummary {
    ResultValue features = new ResultValue();
    ResultValue scenarios = new ResultValue();
    ResultValue steps = new ResultValue();

    public ResultValue getFeatures() {
        return features;
    }

    public ResultValue getScenarios() {
        return scenarios;
    }

    public ResultValue getSteps() {
        return steps;
    }
}
