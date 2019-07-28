package nz.co.afor.framework.mobile;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Matt on 28/02/2017.
 */
@Component
public class Appium {

    @Autowired
    private Configuration configuration;

    private AppiumDriver driver;

    @Value("${appium.remote.url:http://127.0.0.1:4723/wd/hub}")
    private URL remoteUrl;

    public Configuration getConfiguration() {
        return configuration;
    }

    private void setDriver() {
        if (null == driver) {
            if (configuration.getPlatformName().equalsIgnoreCase("android")) {
                driver = new AndroidDriver(configuration.getDesiredCapabilities());
            } else {
                driver = new IOSDriver(configuration.getDesiredCapabilities());
            }
        }
    }

    private void setRemoteDriver() {
        if (null == driver) {
            if (configuration.getPlatformName().equalsIgnoreCase("android")) {
                driver = new AndroidDriver(remoteUrl, configuration.getDesiredCapabilities());
            } else {
                driver = new IOSDriver(remoteUrl, configuration.getDesiredCapabilities());
            }
            WebDriverRunner.setWebDriver(driver);
        }
    }

    public AppiumDriver getDriver() {
        if (configuration.getRemote()) {
            setRemoteDriver();
        } else {
            setDriver();
        }
        return driver;
    }

    public void close() {
        try {
            driver.closeApp();
            driver.close();
        } finally {
            driver = null;
        }
    }
}
