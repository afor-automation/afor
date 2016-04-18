package nz.co.afor.example.steps;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import nz.co.afor.view.GoogleView;
import org.springframework.beans.factory.annotation.Autowired;

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
}
