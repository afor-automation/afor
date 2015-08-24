package nz.co.afor.framework.steps;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import nz.co.afor.framework.test.AspectOrientedExample;
import org.apache.log4j.Logger;
import org.hamcrest.MatcherAssert;

import static org.hamcrest.core.Is.is;

/**
 * Copyright afor
 * Created by Matt Belcher on 10/08/2015.
 */
public class AspectOrientedSteps {
    private Logger log = Logger.getLogger(this.getClass());
    private AspectOrientedExample aspectOrientedExample = new AspectOrientedExample();
    private Boolean timeoutException = false;

    @When("^I call a method which times out$")
    public void I_call_a_method_which_times_out() throws Throwable {
        timeoutException = false;
        try {
            aspectOrientedExample.timeout();
        } catch (java.lang.InterruptedException e) {
            timeoutException = true;
        }
    }

    @Then("^the method should time out$")
    public void the_method_should_time_out() throws Throwable {
        MatcherAssert.assertThat(timeoutException, is(true));
    }

    @When("^I call a method which retries 3 times$")
    public void I_call_a_method_which_retries_times() throws Throwable {
        try {
            AspectOrientedExample.setCounter(0);
            aspectOrientedExample.retryIncrementCounter3Times();
        } catch (java.lang.RuntimeException e) {
            log.debug("Caught an java.lang.InterruptedException", e);
        }
    }

    @Then("^the method should retry 3 times$")
    public void the_method_should_retry_times() throws Throwable {
        MatcherAssert.assertThat(AspectOrientedExample.getCounter(), is(3));
    }
}
