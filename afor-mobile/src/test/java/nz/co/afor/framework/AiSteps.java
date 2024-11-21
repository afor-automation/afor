package nz.co.afor.framework;

import io.cucumber.java8.En;

import static com.codeborne.selenide.Condition.attribute;
import static nz.co.afor.framework.mobile.ai.MobileAi.ai;

public class AiSteps implements En {

    public AiSteps() {
        Then("the AI query {string} should find the field with the attribute {string} value {string}", (String query, String attribute, String expectedId) ->
                ai(query).shouldHave(attribute(attribute, expectedId)));
    }
}
