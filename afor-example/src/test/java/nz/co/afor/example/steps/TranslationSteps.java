package nz.co.afor.example.steps;

import static com.codeborne.selenide.Condition.focused;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byClassName;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;

import org.springframework.beans.factory.annotation.Value;
import com.codeborne.selenide.Selenide;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class TranslationSteps {

    @Value("${slovnik.seznam.uri}")
    public String slovnikSeznamUri;

    @Given("I am on the slovnik landing page")
    public void iAmOnTheSlovnikLandingPage() {
        // Code to navigate to the Slovnik.cz landing page
        open(slovnikSeznamUri);
        Selenide.sleep((long) (Math.random() * 3000 + 1000)); // Add delay

        System.out.println("Navigated to the Slovnik.cz landing page.");
    }

    @When("I type {string} for translation")
    public void iTypeForTranslation(String word) {
        // Code to enter the provided word into the translation field
        $(byName("search")).shouldBe(focused).setValue(word).pressEnter();
        System.out.println("Entered word: " + word);
    }

@Then("I should obtain a list of possible English words with at least {int} elements in the result")
    public void iShouldObtainAListOfPossibleEnglishWords(int number) {
        $(byClassName("TranslatePage-results"));
        $(byXpath("//*[@id=\"__next\"]/div/main/div/div[2]/div/article[1]/section/ol"))
                .shouldBe(visible)
                .$$("li")
                .shouldHave(sizeGreaterThanOrEqual(number));

        System.out.println("Verified the list of English words is displayed with at least " + number + " elements.");
    }
}