package nz.co.afor.framework.web;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Matt Belcher on 1/09/2015.
 */
public class SelenideEventListener extends AbstractWebDriverEventListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void beforeNavigateTo(String url, WebDriver driver) {
        logger.info(String.format("Navigating to the url '%s'", url));
    }
}
