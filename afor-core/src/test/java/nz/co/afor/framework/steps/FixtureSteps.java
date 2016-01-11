package nz.co.afor.framework.steps;

import com.google.gson.reflect.TypeToken;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import nz.co.afor.framework.Fixture;
import nz.co.afor.framework.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

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

    Map<String, Customer> model;

    @Given("^I have a new fixture instance$")
    public void I_have_a_new_fixture_instance() throws Throwable {
        assertThat("nz.co.afor.framework.Fixture should not be null", fixture, is(not(nullValue())));
    }

    @When("^I read the fixture data$")
    public void I_read_the_fixture_data() throws Throwable {
        model = fixture.getFixture(new TypeToken<Map<String, Customer>>() {
        }.getType());
    }

    @Then("^I should have fixture data$")
    public void I_should_have_fixture_data() throws Throwable {
        assertThat(model, is(not(nullValue())));
        assertThat(model.get("valid"), is(not(nullValue())));
        assertThat(model.get("valid").getUsername(), is(not(nullValue())));
        assertThat(model.get("valid").getPassword(), is(not(nullValue())));
    }
}
