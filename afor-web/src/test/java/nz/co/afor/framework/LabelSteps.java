package nz.co.afor.framework;

import cucumber.api.java8.En;
import nz.co.afor.framework.web.By;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Selenide.$;

/**
 * Created by Matt on 31/08/2017.
 */
public class LabelSteps implements En {
    public LabelSteps() {
        Then("^the page should have the label with (exact text|text) \"([^\"]*)\" and field id \"([^\"]*)\" by reference$", (String type, String labelText, String fieldId) -> {
            if (type.equals("exact text")) {
                $(By.exactLabelText(labelText)).shouldHave(attribute("id", fieldId));
            } else {
                $(By.labelText(labelText)).shouldHave(attribute("id", fieldId));
            }
        });

        Then("^the page should have the label with (exact text|text) \"([^\"]*)\" and field id \"([^\"]*)\" by association$", (String type, String labelText, String fieldId) -> {
            if (type.equals("exact text")) {
                $(By.exactLabelAssociation(labelText)).shouldHave(attribute("id", fieldId));
            } else {
                $(By.labelAssociation(labelText)).shouldHave(attribute("id", fieldId));
            }
        });
    }
}
