package nz.co.afor.framework.mobile;

import com.codeborne.selenide.WebDriverRunner;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.appmanagement.AndroidInstallApplicationOptions;
import io.appium.java_client.ios.IOSDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

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
            if (androidDriver.isAppInstalled(configuration.getBundleId())) {
                androidDriver.removeApp(configuration.getBundleId());
            }
            androidDriver.installApp(getAppPath(), new AndroidInstallApplicationOptions().withGrantPermissionsEnabled());
            androidDriver.activateApp(configuration.getBundleId());
        } else {
            IOSDriver iosDriver = (IOSDriver) getDriver();
            if (iosDriver.isAppInstalled(configuration.getBundleId())) {
                iosDriver.removeApp(configuration.getBundleId());
            }
            iosDriver.installApp(getAppPath());
            iosDriver.activateApp(configuration.getBundleId());
        }
        return driver;
    }

    private String getAppPath() {
        URL resource = this.getClass().getClassLoader().getResource(configuration.getApp().replaceFirst("^classpath:", ""));
        if (null != resource) {
            try {
                return Path.of(resource.toURI()).toAbsolutePath().toString();
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException(e);
            }
        }
        return configuration.getApp();
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
            if (!androidDriver.isAppInstalled(configuration.getBundleId())) {
                androidDriver.installApp(getAppPath(), new AndroidInstallApplicationOptions().withGrantPermissionsEnabled());
            }
            androidDriver.activateApp(configuration.getBundleId());
        } else {
            IOSDriver iosDriver = (IOSDriver) getDriver();
            if (!iosDriver.isAppInstalled(configuration.getBundleId())) {
                iosDriver.installApp(getAppPath());
            }
            iosDriver.activateApp(configuration.getBundleId());
        }
        return driver;
    }

    private void validate() {
        if (null == configuration.getApp() || configuration.getApp().isEmpty())
            throw new IllegalArgumentException("The app location 'appium.app' should be specified when installing and launching the app");
        if (null == getConfiguration().getBundleId() || getConfiguration().getBundleId().isEmpty())
            throw new IllegalArgumentException("The app bundle 'appium.bundleId' should be specified when installing and launching the app");
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
