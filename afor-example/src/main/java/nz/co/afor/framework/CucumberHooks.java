package nz.co.afor.framework;

import com.codeborne.selenide.WebDriverRunner;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import io.cucumber.spring.CucumberContextConfiguration;
import nz.co.afor.ai.AiClient;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.springframework.test.context.ContextConfiguration;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

/**
 * Created by Matt on 12/04/2016.
 */
// Below annotations are required to run within IDE,
// but are causing issues when run via maven
@CucumberContextConfiguration
@ContextConfiguration("classpath:cucumber.xml")
public class CucumberHooks {

    @After
    public void afterScenario(Scenario scenario) {
        // Close our browser between scenarios
        if (WebDriverRunner.hasWebDriverStarted()) {
            if (scenario.isFailed()) {
                byte[] screenshot = ((TakesScreenshot) getWebDriver()).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", scenario.getId());
            }

            // Add the option to clean up and close browsers after each scenario
            WebDriverRunner.clearBrowserCache();
            WebDriverRunner.getWebDriver().manage().deleteAllCookies();
            closeWebDriver();
        }

        AiClient.logUsage();
    }
}
