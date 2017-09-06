package nz.co.afor.framework;

import com.codeborne.selenide.ElementsCollection;
import cucumber.api.java8.En;
import nz.co.afor.framework.web.Condition;

import static com.codeborne.selenide.Selenide.$$;

/**
 * Created by Matt on 31/08/2017.
 */
public class CollectionSteps implements En {
    private ElementsCollection filteredElements;
    private ElementsCollection divElements;

    public CollectionSteps() {
        And("^I find a collection of div elements$", () -> divElements = $$("div"));
        When("^I filter the div elements by the css \"([^\"]*)\"$", (String css) -> filteredElements = divElements.filterBy(Condition.css(css)));
        Then("^the css filter should have one element with the class \"([^\"]*)\"$", (String cssClass) -> filteredElements.shouldHaveSize(1).first().shouldHave(com.codeborne.selenide.Condition.cssClass(cssClass)));
        When("^I filter the div elements by the css \"([^\"]*)\" and regex \"([^\"]*)\"$", (String css, String regex) -> filteredElements = divElements.filterBy(Condition.css(css, regex)));
    }
}
