package nz.co.afor.view;

import com.codeborne.selenide.SelenideElement;
import org.springframework.stereotype.Component;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static java.lang.String.format;
import static nz.co.afor.framework.web.ai.BrowserAi.ai;

/**
 * Created by Matt Belcher on 10/01/2016.
 */
@Component
public class AiGoogleView {
    public SelenideElement getSearchField() {
        return ai("find the search field");
    }

    public SelenideElement getSearchButton() {
        return ai("find the search button");
    }

    public void search(String searchString) {
        getSearchButton().should(exist);
        getSearchField().click();
        getSearchField().should(be(focused));
        getSearchField().setValue(searchString);
        getSearchButton().click();
    }

    public SelenideElement getResults() {
        return ai($("div[role='main']"), "find the search results main area");
    }

    public SelenideElement getResultLink(String title) {
        return ai(getResults(), format("find the search result with the title '%s'", title));
    }
}
