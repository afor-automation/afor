package nz.co.afor.reports.results;

/**
 * Created by Matt on 11/04/2016.
 */
public class ResultValue {
    Integer passed = 0;
    Integer failed = 0;
    Integer undefined = 0;
    Integer pending = 0;
    Integer skipped = 0;

    public void addResult(String result) {
        if (null == result)
            return;
        switch (result.toLowerCase()) {
            case "passed":
                passed();
                break;
            case "failed":
                failed();
                break;
            case "pending":
                pending();
                break;
            case "undefined":
                undefined();
                break;
            case "skipped":
                skipped();
                break;
            default:
                throw new RuntimeException(String.format("ResultValue type '%s' is not defined in reporting", result));
        }
    }

    public void passed() {
        passed++;
    }

    public void failed() {
        failed++;
    }

    public void pending() {
        pending++;
    }

    public void undefined() {
        undefined++;
    }

    public void skipped() {
        skipped++;
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
}
