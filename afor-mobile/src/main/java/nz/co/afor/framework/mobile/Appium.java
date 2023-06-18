package nz.co.afor.framework.mobile;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.appmanagement.AndroidInstallApplicationOptions;
import io.appium.java_client.ios.IOSDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Created by Matt on 28/02/2017.
 */
@Component
public class Appium {

    @Autowired
    private Configuration configuration;

    private AppiumDriver driver;

    @Value("${appium.remote.url:http://127.0.0.1:4723/}")
    private URL remoteUrl;

    @Value("${appium.appPackage:}")
    private String appPackage;

    @Value("${appium.app:}")
    private String app;


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

    /**
     * Performs a clean install and launch of the app
     *
     * @return The appium driver
     */
    public AppiumDriver cleanLaunch() {
        validate();
        if (configuration.getPlatformName().equalsIgnoreCase("android")) {
            AndroidDriver androidDriver = (AndroidDriver) getDriver();
            if (androidDriver.isAppInstalled(appPackage)) {
                androidDriver.removeApp(appPackage);
            }
            androidDriver.installApp(getAppPath(), new AndroidInstallApplicationOptions().withGrantPermissionsEnabled());
            androidDriver.activateApp(appPackage);
        } else {
            IOSDriver iosDriver = (IOSDriver) getDriver();
            if (iosDriver.isAppInstalled(appPackage)) {
                iosDriver.removeApp(appPackage);
            }
            iosDriver.installApp(getAppPath());
            iosDriver.activateApp(appPackage);
        }
        return driver;
    }

    private String getAppPath() {
        URL resource = this.getClass().getClassLoader().getResource(app.replaceFirst("^classpath:", ""));
        if (null != resource) {
            try {
                return Path.of(resource.toURI()).toAbsolutePath().toString();
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException(e);
            }
        }
        return app;
    }

    /**
     * Performs an install and launch of the app, if it does not already exist on the device
     *
     * @return The appium driver
     */
    public AppiumDriver launch() {
        validate();
        if (configuration.getPlatformName().equalsIgnoreCase("android")) {
            AndroidDriver androidDriver = (AndroidDriver) getDriver();
            if (!androidDriver.isAppInstalled(appPackage)) {
                androidDriver.installApp(getAppPath(), new AndroidInstallApplicationOptions().withGrantPermissionsEnabled());
            }
            androidDriver.activateApp(appPackage);
        } else {
            IOSDriver iosDriver = (IOSDriver) getDriver();
            if (!iosDriver.isAppInstalled(appPackage)) {
                iosDriver.installApp(getAppPath());
            }
            iosDriver.activateApp(appPackage);
        }
        return driver;
    }

    private void validate() {
        if (null == appPackage || appPackage.isEmpty())
            throw new IllegalArgumentException("The app package 'appium.appPackage' must be specified when installing and launching the app");
        if (null == app || app.isEmpty())
            throw new IllegalArgumentException("The app location 'appium.app' must be specified when installing and launching the app");
        if (null == getConfiguration().getAutomationName() || getConfiguration().getAutomationName().isEmpty()) {
            if (configuration.getPlatformName().equalsIgnoreCase("android")) {
                configuration.setCapability("automationName", "uiautomator2");
            } else {
                configuration.setCapability("automationName", "xcuitest");
            }
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
            driver.close();
        } finally {
            driver = null;
        }
    }
}
