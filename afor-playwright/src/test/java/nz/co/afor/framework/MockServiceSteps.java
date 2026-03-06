package nz.co.afor.framework;

import com.microsoft.playwright.assertions.PlaywrightAssertions;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static nz.co.afor.framework.Browser.page;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;

/**
 * Created by Matt Belcher on 19/08/2015.
 */
public class MockServiceSteps {

    private final int serverPort = 16151;

    @Given("^I have a mock service running$")
    public void I_have_a_mock_service_running() {
        assertThat(serverPort, is(greaterThan(0)));
    }

    @When("^I open the \"([^\"]*)\" page$")
    public void I_open_the_page(String uri) {
        Browser.init();
        page.get().navigate(String.format("http://localhost:%s%s", serverPort, uri));
    }

    @Then("^the page should have the text \"([^\"]*)\"$")
    public void the_page_should_have_the_text(String pageText) {
        PlaywrightAssertions.assertThat(page.get().getByText(pageText)).containsText(pageText);
    }
}
