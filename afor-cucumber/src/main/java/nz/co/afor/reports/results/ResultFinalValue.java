package nz.co.afor.reports.results;

import cucumber.api.Result;

import java.util.Objects;

import static cucumber.api.Result.Type.*;

/**
 * Created by Matt on 11/04/2016.
 */
public class ResultFinalValue {

    Result.Type resultValue;

    public void addStatus(Result.Type status) {
        switch (null == status ? UNDEFINED : status) {
            case PASSED:
                if (resultValue == null) {
                    resultValue = PASSED;
                }
                break;
            case FAILED:
                resultValue = FAILED;
                break;
            case UNDEFINED:
                if (resultValue == null || Objects.equals(resultValue.lowerCaseName(), "passed")) {
                    resultValue = UNDEFINED;
                }
                break;
            case PENDING:
                if (resultValue == null || Objects.equals(resultValue.lowerCaseName(), "passed")) {
                    resultValue = PENDING;
                }
                break;
            case SKIPPED:
                if (resultValue == null || Objects.equals(resultValue.lowerCaseName(), "passed")) {
                    resultValue = SKIPPED;
                }
                break;
            case AMBIGUOUS:
                if (resultValue == null || Objects.equals(resultValue.lowerCaseName(), "passed")) {
                    resultValue = AMBIGUOUS;
                }
                break;
        }
    }

    public Result.Type getResultValue() {
        return resultValue;
    }
}
