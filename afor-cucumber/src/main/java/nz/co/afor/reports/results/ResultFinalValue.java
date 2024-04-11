package nz.co.afor.reports.results;

import io.cucumber.plugin.event.Status;

import static io.cucumber.plugin.event.Status.*;

/**
 * Created by Matt on 11/04/2016.
 */
public class ResultFinalValue {

    Status resultValue;

    public void addStatus(Status status) {
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
                if (resultValue == null || resultValue.equals(PASSED)) {
                    resultValue = UNDEFINED;
                }
                break;
            case PENDING:
                if (resultValue == null || resultValue.equals(PASSED)) {
                    resultValue = PENDING;
                }
                break;
            case SKIPPED:
                if (resultValue == null || resultValue.equals(PASSED)) {
                    resultValue = SKIPPED;
                }
                break;
            case AMBIGUOUS:
                if (resultValue == null || resultValue.equals(PASSED)) {
                    resultValue = AMBIGUOUS;
                }
                break;
        }
    }

    public Status getResultValue() {
        return resultValue;
    }
}
