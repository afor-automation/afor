package nz.co.afor.framework.steps;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import nz.co.afor.framework.GsonFactory;
import nz.co.afor.framework.model.Customer;
import org.exparity.hamcrest.date.DateMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by Matt on 20/10/2016.
 */
public class DateSteps {

    @Autowired
    GsonFactory gsonFactory;

    @Value("${nz.co.afor.fixture.dateformat}")
    String dateFormat;

    String json;

    Customer parsedJson;
    private Date currentDate = new Date();

    @Given("^I have a new JSON parser instance$")
    public void iHaveANewGsonInstance() throws Throwable {
        assertThat(gsonFactory, is(not(nullValue())));
    }

    @And("^I have JSON which matches the configuration date format$")
    public void iHaveJSONWhichMatchesTheConfigurationDateFormat() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        json = "{\"dateOfBirth\":\"" + simpleDateFormat.format(currentDate) + "\"}";
    }

    @When("^I parse the JSON$")
    public void iParseTheJSON() {
        parsedJson = gsonFactory.getGson().fromJson(json, Customer.class);
    }

    @Then("^the JSON date format should match the configuration$")
    public void theGsonDateFormatShouldMatchTheConfiguration() {
        assertThat(parsedJson.getDateOfBirth(), DateMatchers.sameYear(currentDate));
        assertThat(parsedJson.getDateOfBirth(), DateMatchers.sameMonth(currentDate));
        assertThat(parsedJson.getDateOfBirth(), DateMatchers.sameDay(currentDate));
        assertThat(parsedJson.getDateOfBirth(), DateMatchers.sameHour(currentDate));
        assertThat(parsedJson.getDateOfBirth(), DateMatchers.sameMinute(currentDate));
        assertThat(parsedJson.getDateOfBirth(), DateMatchers.sameSecond(currentDate));
    }
}
