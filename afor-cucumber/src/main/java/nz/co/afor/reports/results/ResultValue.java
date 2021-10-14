package nz.co.afor.reports.results;

import io.cucumber.plugin.event.Status;

/**
 * Created by Matt on 11/04/2016.
 */
public class ResultValue {
    private Integer passed = 0;
    private Integer failed = 0;
    private Integer undefined = 0;
    private Integer pending = 0;
    private Integer skipped = 0;
    private Integer ambiguous = 0;

    public void addResult(Status result) {
        if (null == result)
            return;
        switch (result) {
            case PASSED:
                passed();
                break;
            case FAILED:
                failed();
                break;
            case PENDING:
                pending();
                break;
            case UNDEFINED:
                undefined();
                break;
            case SKIPPED:
                skipped();
                break;
            case AMBIGUOUS:
                ambiguous();
                break;
            case UNUSED:
            default:
                throw new RuntimeException(String.format("ResultValue type '%s' is not defined in reporting", result));
        }
    }

    private void passed() {
        passed++;
    }

    private void failed() {
        failed++;
    }

    private void pending() {
        pending++;
    }

    private void undefined() {
        undefined++;
    }

    private void skipped() {
        skipped++;
    }

    private void ambiguous() {
        ambiguous++;
    }

    public Integer getPassed() {
        return passed;
    }

    public Integer getFailed() {
        return failed;
    }

    public Integer getPending() {
        return pending;
    }

    public Integer getUndefined() {
        return undefined;
    }

    public Integer getSkipped() {
        return skipped;
    }

    public Integer getAmbiguous() {
        return ambiguous;
    }
}
