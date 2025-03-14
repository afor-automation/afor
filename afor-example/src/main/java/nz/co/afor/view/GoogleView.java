package nz.co.afor.view;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.springframework.stereotype.Component;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Created by Matt Belcher on 10/01/2016.
 */
@Component
public class GoogleView {
    public SelenideElement getSearchField() {
        return $(By.name("q"));
    }

    public SelenideElement getSearchButton() {
        return $$("button[value='Search'],input[value='Google Search']").findBy(visible);
    }

    public void search(String searchString) {
        getSearchButton().should(exist);
        getSearchField().click();
        getSearchField().should(be(focused));
        getSearchField().setValue(searchString);
        getSearchButton().click();
    }

    public SelenideElement getResults() {
        return $("div[role='main'] div[data-hveid][data-ved]");
    }

    public ElementsCollection getResultLinks() {
        return $$("div a h3");
    }
}
