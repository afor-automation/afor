package nz.co.afor.framework.web;

import org.openqa.selenium.WebDriver;

/**
 * Copyright afor
 * Created by Matt Belcher on 6/07/2015.
 */
public interface WebDriverFactory {
    WebDriver getInstance();

    Boolean hasInstance();

    WebDriver createInstance();
}
