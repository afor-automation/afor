package nz.co.afor.reports;

import cucumber.api.PickleStepTestStep;
import cucumber.api.Result;
import cucumber.api.TestStep;
import gherkin.ast.Step;
import gherkin.pickles.PickleStep;

/**
 * Created by Matt on 15/03/2016.
 */
public class StepResult {
    PickleStep step;
    Result result;

    public StepResult(PickleStep testStep, Result result) {
        this.step = testStep;
        this.result = result;
    }

    public PickleStep getStep() {
        return step;
    }

    public void setStep(PickleStep step) {
        this.step = step;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
