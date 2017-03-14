package nz.co.afor.framework.mobile;

import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Matt on 28/02/2017.
 */
@Component
public class Configuration {
    private DesiredCapabilities desiredCapabilities = new DesiredCapabilities();

    @Value("${appium.platformName:Android}")
    private String platformName;

    @Value("${appium.platformVersion:7.0}")
    private String platformVersion;

    @Value("${appium.deviceName:Android Emulator}")
    private String deviceName;

    @Value("${appium.app:}")
    private String app;

    @Value("${appium.appPackage:}")
    private String appPackage;

    @Value("${appium.browser:}")
    private String browser;

    @Value("${appium.browserName:}")
    private String browserName;

    @Value("${appium.newCommandTimeout:60}")
    private Integer newCommandTimeout;

    @Value("${appium.orientation:PORTRAIT}")
    private Orientation orientation;

    @Value("${appium.autoWebview:false}")
    private Boolean autoWebview;

    @Value("${appium.noReset:false}")
    private Boolean noReset;

    @Value("${appium.fullReset:false}")
    private Boolean fullReset;

    @Value("${appium.autoGrantPermissions:true}")
    private Boolean autoGrantPermissions;

    @PostConstruct
    public void setConfiguration() throws IOException {
        desiredCapabilities.setCapability("platformName", platformName);
        desiredCapabilities.setCapability("platformVersion", platformVersion);
        desiredCapabilities.setCapability("deviceName", deviceName);
        if (null != app && !app.isEmpty())
            desiredCapabilities.setCapability("app", getPath(app));
        if (null != appPackage && !appPackage.isEmpty())
            desiredCapabilities.setCapability("appPackage", getPath(appPackage));
        if (null != browser && !browser.isEmpty())
            desiredCapabilities.setCapability("browser", browser);
        desiredCapabilities.setCapability("browserName", browserName);
        desiredCapabilities.setCapability("newCommandTimeout", newCommandTimeout);
        desiredCapabilities.setCapability("orientation", orientation);
        desiredCapabilities.setCapability("autoWebview", autoWebview);
        desiredCapabilities.setCapability("noReset", noReset);
        desiredCapabilities.setCapability("autoGrantPermissions", autoGrantPermissions);
        desiredCapabilities.setCapability("fullReset", fullReset);
    }

    private String getPath(String path) throws IOException {
        if (!path.contains("classpath:"))
            return path;
        return new ClassPathResource(path.replace("classpath:","")).getFile().getAbsolutePath();
    }

    public Configuration setCapability(String capabilityName, String value) {
        desiredCapabilities.setCapability(capabilityName, value);
        return this;
    }

    public Configuration setCapability(String capabilityName, boolean value) {
        desiredCapabilities.setCapability(capabilityName, value);
        return this;
    }

    public Configuration setCapability(String capabilityName, Platform value) {
        desiredCapabilities.setCapability(capabilityName, value);
        return this;
    }

    public DesiredCapabilities getDesiredCapabilities() {
        return desiredCapabilities;
    }

    public String getPlatformName() {
        return platformName;
    }

    public String getPlatformVersion() {
        return platformVersion;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getApp() {
        return app;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public String getBrowser() {
        return browser;
    }

    public String getBrowserName() {
        return browserName;
    }

    public Integer getNewCommandTimeout() {
        return newCommandTimeout;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public Boolean getAutoWebview() {
        return autoWebview;
    }

    public Boolean getNoReset() {
        return noReset;
    }

    public Boolean getFullReset() {
        return fullReset;
    }
}
