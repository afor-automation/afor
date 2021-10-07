package nz.co.afor.reports;

import io.cucumber.core.gherkin.Feature;
import io.cucumber.core.gherkin.messages.FeatureMapping;
import io.cucumber.plugin.EventListener;
import io.cucumber.plugin.event.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HTML implements EventListener {

    private final ReportWriter reportWriter;
    private static final Map<URI, FeatureMapping> featureMap = new HashMap<>();
    private final List<EmbedEvent> attachments = new ArrayList<>();
    private final File path;

    public HTML(File path) throws IOException {
        this.path = path;
        this.reportWriter = new ReportWriter(new HtmlWriter(path));
    }

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestSourceParsed.class, this::handleTestSourceParsed);
        publisher.registerHandlerFor(TestCaseFinished.class, this::handleTestCaseFinished);
        publisher.registerHandlerFor(TestRunFinished.class, this::handleTestRunFinished);
        publisher.registerHandlerFor(TestStepFinished.class, this::handleTestStepFinished);
        publisher.registerHandlerFor(TestCaseStarted.class, this::handleTestCaseStarted);
        publisher.registerHandlerFor(EmbedEvent.class, this::handleEmbedEvent);
    }

    private void handleEmbedEvent(EmbedEvent embedEvent) {
        attachments.add(embedEvent);
    }

    private void handleTestStepFinished(TestStepFinished testStepFinished) {
        reportWriter.writeStep(testStepFinished);
        attachments.forEach(reportWriter::writeAttachment);
        attachments.clear();
    }

    private void handleTestCaseStarted(TestCaseStarted testCaseStarted) {
        attachments.clear();
        FeatureMapping featureMapping = featureMap.get(testCaseStarted.getTestCase().getUri());
        reportWriter.writeFeature(featureMapping);
        reportWriter.writeScenario();
    }

    private void handleTestSourceParsed(TestSourceParsed testSourceParsed) {
        testSourceParsed.getNodes().stream().filter(node -> Node.Feature.class.isAssignableFrom(node.getClass())).findFirst().ifPresent(node ->
        {
            FeatureMapping value = new FeatureMapping(node);
            value.setHash(((Feature) node).getUri().hashCode());
            featureMap.put(((Feature) node).getUri(), value);
        });
    }

    private void handleTestRunFinished(TestRunFinished testRunFinished) {
        reportWriter.close();
        reportWriter.writeSummaries(path);
        reportWriter.writeCharts(path);
    }

    private void handleTestCaseFinished(TestCaseFinished testCaseFinished) {
        reportWriter.writeScenario(testCaseFinished);
    }
}
