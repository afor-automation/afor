package nz.co.afor.reports;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cucumber.api.Result;

import java.util.List;

public interface GenericScenarioResult {
    String getName();
    List<StepResult> getSteps();
    long getDuration();

    @JsonIgnore
    default Result.Type getScenarioResult() {
        Result.Type scenarioResult = null;
        for (StepResult stepResult : getSteps()) {
            switch (stepResult.getResult()) {
                case PASSED:
                    if (scenarioResult == null) {
                        scenarioResult = Result.Type.PASSED;
                    }
                    break;
                case FAILED:
                    scenarioResult = Result.Type.FAILED;
                    break;
                case UNDEFINED:
                    if (scenarioResult == null || scenarioResult == Result.Type.PASSED) {
                        scenarioResult = Result.Type.UNDEFINED;
                    }
                    break;
                case PENDING:
                    if (scenarioResult == null || scenarioResult == Result.Type.PASSED) {
                        scenarioResult = Result.Type.PENDING;
                    }
                    break;
                case SKIPPED:
                    if (scenarioResult == null || scenarioResult == Result.Type.PASSED) {
                        scenarioResult = Result.Type.SKIPPED;
                    }
                    break;
                case AMBIGUOUS:
                    if (scenarioResult == null || scenarioResult == Result.Type.PASSED) {
                        scenarioResult = Result.Type.AMBIGUOUS;
                    }
                    break;
            }
        }
        return scenarioResult;
    }
}
