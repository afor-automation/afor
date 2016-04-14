package nz.co.afor.reports.results;

import java.util.Objects;

/**
 * Created by Matt on 11/04/2016.
 */
public class ResultFinalValue {

    String resultValue;

    public void addStatus(String status) {
        switch (status) {
            case "passed":
                if (resultValue == null) {
                    resultValue = "passed";
                }
                break;
            case "failed":
                resultValue = "failed";
                break;
            case "undefined":
                if (resultValue == null || !Objects.equals(resultValue, "failed")) {
                    resultValue = "undefined";
                }
                break;
            case "pending":
                if (resultValue == null || !Objects.equals(resultValue, "failed")) {
                    resultValue = "pending";
                }
                break;
            case "skipped":
                if (resultValue == null || Objects.equals(resultValue, "passed")) {
                    resultValue = "skipped";
                }
        }
    }

    public String getResultValue() {
        return resultValue;
    }
}
