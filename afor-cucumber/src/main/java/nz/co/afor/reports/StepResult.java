package nz.co.afor.reports;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cucumber.api.Result;
import gherkin.pickles.PickleStep;

/**
 * Created by Matt on 15/03/2016.
 */
public class StepResult {
    @JsonIgnore
    private final PickleStep step;
    private final Result result;

    StepResult(PickleStep testStep, Result result) {
        this.step = testStep;
        this.result = result;
    }

    @JsonIgnore
    public PickleStep getStep() {
        return step;
    }

    public Result.Type getResult() {
        return result.getStatus();
    }

    /**
     * Get the duration of the step in seconds
     * @return The duration of the scenario in seconds
     */
    @JsonIgnore
    long getDuration() {
        return result.getDuration();
    }
}
