package nz.co.afor.reports.results;

public class FeatureSummaryResult {
    private final String name;
    private final String href;
    private final ResultValue scenarioResults;

    public FeatureSummaryResult(String name, String href) {
        this.name = name;
        this.href = href;
        this.scenarioResults = new ResultValue();
    }

    public String getName() {
        return name;
    }

    public String getHref() {
        return href;
    }

    public ResultValue getScenarioResults() {
        return scenarioResults;
    }
}
