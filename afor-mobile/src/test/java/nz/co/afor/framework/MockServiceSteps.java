package nz.co.afor.framework;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import nz.co.afor.framework.mobile.Appium;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Created by Matt Belcher on 19/08/2015.
 */
public class MockServiceSteps {

    public static final String FIELD_TEXT = "test@afor.co.nz";
    public static final By EMAIL = By.id("email");

    @Autowired
    Appium appium;

    @Given("^I have an android device running$")
    public void iHaveAnAndroidDeviceRunning() throws Throwable {
        appium.getDriver().launchApp();
    }

    @When("^I fill in a field$")
    public void iFillInAField() throws Throwable {
        appium.getDriver().findElement(EMAIL).sendKeys(FIELD_TEXT);
    }

    @Then("^the field should be filled in$")
    public void theFieldShouldBeFilledIn() throws Throwable {
        assertThat(appium.getDriver().findElement(EMAIL).getText(), is(equalTo(FIELD_TEXT)));
    }
}
