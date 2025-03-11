package nz.co.afor.example.steps;

import io.cucumber.java8.En;
import nz.co.afor.view.AiGoogleView;
import org.springframework.beans.factory.annotation.Autowired;

import static com.codeborne.selenide.Condition.exist;
import static java.lang.String.format;

import com.codeborne.selenide.Selenide;

public class AiSteps implements En {

    @Autowired
    private AiGoogleView aiGoogleView;

    public AiSteps() {
        When("I search for {string} using ai", (String search) -> {
            aiGoogleView.search(search);
            Selenide.sleep((long) (Math.random() * 3000 + 1000)); // Add delay
        });
        Then("I should see some search results using ai", () -> {
            aiGoogleView.solveCaptcha();
            aiGoogleView.getResults().should(exist.because("The search results should exist"));
        });
        And("the search results should contain a link with the text {string} using ai", (String linkText) -> {
                aiGoogleView.getResultLink(linkText)
                        .should(exist.because(format("The link text '%s' should exist in the results", linkText)));
        });
    }
}
