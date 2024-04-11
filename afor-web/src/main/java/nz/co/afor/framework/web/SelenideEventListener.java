package nz.co.afor.framework.web;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.events.WebDriverListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

/**
 * Created by Matt Belcher on 1/09/2015.
 */
public class SelenideEventListener implements WebDriverListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public WebDriver createWebDriver(WebDriver webDriver) {
        EventFiringDecorator<WebDriver> decorator = new EventFiringDecorator<>(this);
        return decorator.decorate(webDriver);
    }

    @Override
    public void beforeGet(WebDriver driver, String url) {
        WebDriverListener.super.beforeGet(driver, url);
        logger.info("Navigating to the url '{}'", url);
    }

    @Override
    public void beforeSendKeys(WebElement element, CharSequence... keysToSend) {
        WebDriverListener.super.beforeSendKeys(element, keysToSend);
        logger.debug("Changing the element '{}' value to '{}'", element, keysToSend);
    }

    @Override
    public void beforeTo(WebDriver.Navigation navigation, String url) {
        WebDriverListener.super.beforeTo(navigation, url);
        logger.info("Navigating to the url '{}'", url);
    }

    @Override
    public void beforeTo(WebDriver.Navigation navigation, URL url) {
        WebDriverListener.super.beforeTo(navigation, url);
        logger.info("Navigating to the url '{}'", url);
    }
}
