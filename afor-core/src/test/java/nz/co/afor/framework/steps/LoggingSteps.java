package nz.co.afor.framework.steps;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Copyright afor
 * Created by Matt Belcher on 3/08/2015.
 */
public class LoggingSteps {
    private Logger log = Logger.getLogger(this.getClass());

    private Appender appenderMock;

    @Before("@log")
    public void setupAppender() {
        appenderMock = mock(Appender.class);
        log.addAppender(appenderMock);
    }

    @After("@log")
    public void removeAppender() {
        log.removeAppender(appenderMock);
    }

    @Given("^I have a new log instance$")
    public void I_have_a_new_log_instance() throws Throwable {
        assertThat(log, is(not(nullValue())));
    }

    @When("^I log to (debug|info|error|trace)$")
    public void I_log_to(String logLevel) throws Throwable {
        switch (logLevel) {
            case "debug":
                log.debug("debug log level");
                break;
            case "info":
                log.info("info log level");
                break;
            case "error":
                log.error("error log level");
                break;
            case "trace":
                log.trace("trace log level");
                break;
            default:
                throw new RuntimeException(String.format("logLevel '%s' is not recognised as a valid log level", logLevel));

        }
    }

    @Then("^I my information should be logged$")
    public void I_my_information_should_be_logged() throws Throwable {
        verify(appenderMock).doAppend((LoggingEvent) anyObject());
    }

    @Then("^I my information should not be logged$")
    public void I_my_information_should_not_be_logged() throws Throwable {
        verify(appenderMock, never()).doAppend((LoggingEvent) anyObject());
    }
}
