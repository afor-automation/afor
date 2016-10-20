package nz.co.afor.example.steps;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import nz.co.afor.view.GoogleView;
import org.springframework.beans.factory.annotation.Autowired;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.open;

/**
 * Created by Matt Belcher on 10/01/2016.
 */
public class GoogleSteps {

    @Autowired
    GoogleView googleView;

    @Given("^I am on the home page$")
    public void I_am_on_the_home_page() throws Throwable {
        open("/");
    }

    @When("^I search for \"([^\"]*)\"$")
    public void I_search_for(String searchString) throws Throwable {
        googleView.search(searchString);
    }

    @Then("^I should see some search results$")
    public void I_should_see_some_search_results() throws Throwable {
        googleView.getResultStats().shouldBe(visible);
    }

    @And("^the search results should contain a link with the text \"([^\"]*)\"$")
    public void theSearchResultsShouldHaveALinkToASiteContainingTheText(String expectedLinkText) throws Throwable {
        googleView.getResultLinks().findBy(Condition.hasText(expectedLinkText)).should(exist.because(String.format("The link text '%s' should exist in the results", expectedLinkText)));
    }
}
