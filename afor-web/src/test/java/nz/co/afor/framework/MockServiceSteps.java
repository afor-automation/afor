package nz.co.afor.framework;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Value;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;

/**
 * Created by Matt Belcher on 19/08/2015.
 */
public class MockServiceSteps {

    private int serverPort = 16151;

    @Value("${browser:chrome}")
    private String browser;

    @Given("^I have a mock service running$")
    public void I_have_a_mock_service_running() {
        assertThat(serverPort, is(greaterThan(0)));
    }

    @When("^I open the \"([^\"]*)\" page$")
    public void I_open_the_page(String page) {
        com.codeborne.selenide.Configuration.browser = browser;
        open(String.format("http://localhost:%s%s", serverPort, page));
    }

    @Then("^the page should have the text \"([^\"]*)\"$")
    public void the_page_should_have_the_text(String pageText) {
        $("body").shouldHave(text(pageText));
    }
}
