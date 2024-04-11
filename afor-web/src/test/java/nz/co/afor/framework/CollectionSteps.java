package nz.co.afor.framework;

import com.codeborne.selenide.ElementsCollection;
import io.cucumber.java8.En;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Selenide.$$;
import static nz.co.afor.framework.web.Condition.*;

/**
 * Created by Matt on 31/08/2017.
 */
public class CollectionSteps implements En {
    private ElementsCollection filteredElements;
    private ElementsCollection divElements;

    public CollectionSteps() {
        And("^I find a collection of div elements$", () -> divElements = $$("div"));
        When("^I filter the div elements by the css \"([^\"]*)\"$", (String css) -> filteredElements = divElements.filterBy(css(css)));
        Then("^the css filter should have one element with the class \"([^\"]*)\"$", (String cssClass) -> filteredElements.shouldHave(size(1)).first().shouldHave(cssClass(cssClass)));
        When("^I filter the div elements by the css \"([^\"]*)\" and regex \"([^\"]*)\"$", (String css, String regex) -> filteredElements = divElements.filterBy(cssAndMatches(css, regex)));
        When("^I filter the div elements by the css \"([^\"]*)\" and text \"([^\"]*)\"$", (String css, String text) -> filteredElements = divElements.filterBy(cssWithText(css, text)));
        When("^I filter the div elements by the css \"([^\"]*)\" and text case sensitive \"([^\"]*)\"$", (String css, String text) -> filteredElements = divElements.filterBy(cssWithTextCaseSensitive(css, text)));
        When("^I filter the div elements by the css \"([^\"]*)\" and exact text \"([^\"]*)\"$", (String css, String text) -> filteredElements = divElements.filterBy(cssWithExactText(css, text)));
        When("^I filter the div elements by the css \"([^\"]*)\" and exact text case sensitive \"([^\"]*)\"$", (String css, String text) -> filteredElements = divElements.filterBy(cssWithExactTextCaseSensitive(css, text)));
    }
}
