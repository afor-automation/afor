package nz.co.afor.framework.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import nz.co.afor.framework.ObjectMapperInstance;
import nz.co.afor.framework.model.Customer;
import org.exparity.hamcrest.date.ZonedDateTimeMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by Matt on 20/10/2016.
 */
public class DateSteps {

    @Autowired
    private ObjectMapperInstance objectMapperInstance;

    @Value("${nz.co.afor.fixture.dateformat}")
    private String dateFormat;

    private String json;

    private Customer parsedJson;

    @Value("${nz.co.afor.fixture.timezone:UTC}")
    private String timezone;

    private ZonedDateTime currentDate;

    @Given("^I have a new JSON parser instance$")
    public void iHaveANewGsonInstance() {
        assertThat(objectMapperInstance.getObjectMapper(), is(not(nullValue())));
    }

    @And("^I have JSON which matches the configuration date format$")
    public void iHaveJSONWhichMatchesTheConfigurationDateFormat() {
        currentDate = ZonedDateTime.now(ZoneId.of(timezone));
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat);
        json = "{\"dateOfBirth\":\"" + currentDate.format(dateTimeFormatter) + "\"}";
    }

    @When("^I parse the JSON$")
    public void iParseTheJSON() throws JsonProcessingException {
        parsedJson = objectMapperInstance.getObjectMapper().readValue(json, Customer.class);
    }

    @Then("^the JSON date format should match the configuration$")
    public void theGsonDateFormatShouldMatchTheConfiguration() {
        assertThat(parsedJson.getDateOfBirth(), ZonedDateTimeMatchers.sameYear(currentDate));
        assertThat(parsedJson.getDateOfBirth(), ZonedDateTimeMatchers.sameMonthOfYear(currentDate));
        assertThat(parsedJson.getDateOfBirth(), ZonedDateTimeMatchers.sameDay(currentDate));
        assertThat(parsedJson.getDateOfBirth(), ZonedDateTimeMatchers.sameHourOfDay(currentDate));
        assertThat(parsedJson.getDateOfBirth(), ZonedDateTimeMatchers.sameMinuteOfHour(currentDate));
        assertThat(parsedJson.getDateOfBirth(), ZonedDateTimeMatchers.sameSecondOfMinute(currentDate));
    }
}
