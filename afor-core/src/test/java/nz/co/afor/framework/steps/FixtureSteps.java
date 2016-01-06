package nz.co.afor.framework.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import nz.co.afor.framework.Fixture;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * Created by Matt Belcher on 12/10/2015.
 */
public class FixtureSteps {
    @Autowired
    Fixture fixture;

    Object model;

    @Given("^I have a new fixture instance$")
    public void I_have_a_new_fixture_instance() throws Throwable {
        assertThat("nz.co.afor.framework.Fixture should not be null", fixture, is(not(nullValue())));
    }

    @When("^I read the fixture data$")
    public void I_read_the_fixture_data() throws Throwable {
        model = fixture.getFixture(Object.class);
    }

    @Then("^I should have fixture data$")
    public void I_should_have_fixture_data() throws Throwable {
        assertThat(model, is(not(nullValue())));
    }
}
