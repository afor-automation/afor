package nz.co.afor.framework;

import com.codeborne.selenide.WebDriverRunner;
import cucumber.api.Scenario;
import cucumber.api.java.After;

import static com.codeborne.selenide.Selenide.close;

/**
 * Created by Matt on 12/04/2016.
 */
public class CucumberHooks {
    @After
    public void afterScenario(Scenario scenario) {
        // Close our browser between scenarios
        if (WebDriverRunner.hasWebDriverStarted())
            close();
    }
}
