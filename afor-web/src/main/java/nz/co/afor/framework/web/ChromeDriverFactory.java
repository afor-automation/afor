package nz.co.afor.framework.web;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Copyright afor
 * Created by Matt Belcher on 6/07/2015.
 */
public class ChromeDriverFactory implements WebDriverFactory {
    WebDriver webDriver;

    public Boolean hasInstance() {
        return (null != webDriver);
    }

    public WebDriver createInstance() {
        webDriver = new ChromeDriver();
        return webDriver;
    }

    public WebDriver getInstance() {
        if (!hasInstance())
            createInstance();
        return webDriver;
    }
}
