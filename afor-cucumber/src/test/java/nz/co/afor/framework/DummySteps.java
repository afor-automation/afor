package nz.co.afor.framework;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.PendingException;
import io.cucumber.java8.En;
import io.cucumber.java8.StepDefinitionBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Matt on 6/03/2018.
 */
public class DummySteps implements En {
    Logger log = LoggerFactory.getLogger(DummySteps.class);

    public DummySteps() {
        Given("^I am in a state$", () -> log.info("I am in a state"));
        When("^I perform (?:an|another) action$", () -> log.info("I perform an action"));
        When("^I wait for (\\d+) milliseconds$", (Integer seconds) -> Thread.sleep(seconds));
        Then("^I should receive a \"([^\"]*)\"$", (String value) -> log.info("I should receive a result '{}'", value));
        Then("^I should receive a \"ambigious\" result$", (String value) -> {
        });
        Then("^I should receive a \"(pass|fail|pending|not implemented|ambigious|assert)\" result$", (String value) -> {
            switch (value.toLowerCase()) {
                case "pass":
                    return;
                case "fail":
                    throw new RuntimeException("fail");
                case "assert":
                    assertThat("An assertion message", "something", is("something else"));
                case "pending":
                    throw new PendingException();
            }
            throw new RuntimeException("Unexpected step definition value " + value);
        });
        And("^I perform a block action:$", (DataTable dataTable) -> log.info("I perform a block action '{}'", dataTable));
    }
}
