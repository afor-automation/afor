package nz.co.afor.example.steps;

import com.codeborne.selenide.Condition;
import io.cucumber.java8.En;
import nz.co.afor.view.GoogleView;
import org.springframework.beans.factory.annotation.Autowired;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.open;

public class GoogleSteps implements En {

    @Autowired
    GoogleView googleView;

    public GoogleSteps() {
        Given("^I am on the home page$", () -> open("/"));
        When("^I search for \"([^\"]*)\"$", (String searchString) -> googleView.search(searchString));
        Then("^I should see some search results$", () -> googleView.getResultStats().shouldBe(visible));
        And("^the search results should contain a link with the text \"([^\"]*)\"$", (String expectedLinkText) -> googleView.getResultLinks().findBy(Condition.text(expectedLinkText)).should(exist.because(String.format("The link text '%s' should exist in the results", expectedLinkText))));
    }
}
