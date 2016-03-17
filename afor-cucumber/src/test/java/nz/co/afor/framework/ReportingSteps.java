package nz.co.afor.framework;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.util.List;

/**
 * Created by Matt on 12/03/2016.
 */
public class ReportingSteps {
    @Given("^I have a passing step$")
    public void iHaveAPassingStep() throws Throwable {
    }

    @When("^I continue to have a passing step$")
    public void iContinueToHaveAPassingStep() throws Throwable {
    }

    @Then("^I should see a report with a \"([^\"]*)\" result$")
    public void iShouldSeeAReportWithAResult(String arg0) throws Throwable {
    }

    @When("^I continue to have a failing step$")
    public void iContinueToHaveAFailingStep() throws Throwable {
        throw new RuntimeException("Failing step");
    }

    @When("^I continue to have a passing step with a table:$")
    public void iContinueToHaveAPassingStepWithATable(List<String> tableData) throws Throwable {
    }

    @Given("^I have a (\\d+) second sleep$")
    public void iHaveASecondSleep(int seconds) throws Throwable {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException ignore) {
        }
    }
}
