package nz.co.afor.framework;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import nz.co.afor.framework.mock.Application;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;

/**
 * Created by Matt Belcher on 19/08/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class, loader = SpringApplicationContextLoader.class)
@WebAppConfiguration
@IntegrationTest
public class MockServiceSteps {

    @Value("${local.server.port}")
    private int serverPort;

    @Value("${browser:chrome}")
    private String browser;

    @Given("^I have a mock service running$")
    public void I_have_a_mock_service_running() throws Throwable {
        assertThat(serverPort, is(greaterThan(0)));
    }

    @When("^I open the \"([^\"]*)\" page$")
    public void I_open_the_page(String page) throws Throwable {
        com.codeborne.selenide.Configuration.browser = browser;
        open(String.format("http://localhost:%s/%s", serverPort, page));
    }

    @Then("^I the page should have the text \"([^\"]*)\"$")
    public void I_the_page_should_have_the_text(String pageText) throws Throwable {
        $("body").shouldHave(text(pageText));
    }
}
