package nz.co.afor.framework;

import cucumber.api.java8.En;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Matt on 6/03/2018.
 */
public class DummySteps implements En {
    Logger log = LoggerFactory.getLogger(DummySteps.class);

    public DummySteps() {
        Given("^I am in a state$", () -> log.info("I am in a state"));
        When("^I perform (?:an|another) action$", () -> log.info("I perform an action"));
        Then("^I should receive a result$", () -> log.info("I should receive a result"));
    }
}
