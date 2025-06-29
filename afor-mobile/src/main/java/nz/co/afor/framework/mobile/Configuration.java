package nz.co.afor.framework.mobile;

import com.google.common.base.Strings;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by Matt on 28/02/2017.
 */
@Component
public class Configuration implements InitializingBean {
    private final DesiredCapabilities desiredCapabilities = new DesiredCapabilities();

    @Value("${appium.platformName:Android}")
    private String platformName;

    @Value("${appium.platformVersion:16}")
    private String platformVersion;

    @Value("${appium.deviceName:Android Emulator}")
    private String deviceName;

    @Value("${appium.udid:}")
    private String udid;

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

    @Value("${appium.automationName:}")
    private String automationName;

    @Value("${appium.nativeScreenshot:true}")
    private Boolean nativeScreenshot;

    @Value("${appium.recreateChromeDriverSessions:true}")
    private Boolean recreateChromeDriverSessions;

    @Value("${appium.appiumVersion:}")
    private String appiumVersion;

    @Value("${appium.deviceOrientation:}")
    private String deviceOrientation;

    @Value("${appium.ignoreUnimportantViews:true}")
    private Boolean ignoreUnimportantViews;

    @Value("${appium.phoneOnly:}")
    private Boolean phoneOnly;

    @Value("${appium.testobjectApiKey:}")
    private String testobjectApiKey;

    @Value("${appium.testobjectSessionCreateTimeout:}")
    private Integer testobjectSessionCreateTimeout;

    @Value("${appium.idleTimeout:}")
    private Integer idleTimeout;

    @Value("${appium.maxDuration:}")
    private Integer maxDuration;

    @Value("${appium.uiautomator2ServerLaunchTimeout:}")
    private Integer uiautomator2ServerLaunchTimeout;

    @Value("${appium.remote:false}")
    private Boolean remote;

    @Value("${appium.bundleId:}")
    private String bundleId;


    @Override
    public void afterPropertiesSet() throws Exception {
        setConfiguration();
    }

    public void setConfiguration() throws IOException {
        desiredCapabilities.setCapability("platformName", platformName);
        desiredCapabilities.setCapability("appium:platformVersion", platformVersion);
        desiredCapabilities.setCapability("appium:deviceName", deviceName);
        if (!Strings.isNullOrEmpty(udid))
            desiredCapabilities.setCapability("appium:udid", udid);
        if (!Strings.isNullOrEmpty(automationName)) {
            desiredCapabilities.setCapability("appium:automationName", automationName);
        } else {
            desiredCapabilities.setCapability("appium:automationName", getPlatformName().equalsIgnoreCase("android") ? "uiautomator2" : "xcuitest");
        }
        if (!Strings.isNullOrEmpty(app))
            desiredCapabilities.setCapability("appium:app", getPath(app));
        if (!Strings.isNullOrEmpty(appPackage))
            desiredCapabilities.setCapability("appium:appPackage", getPath(appPackage));
        if (!Strings.isNullOrEmpty(bundleId))
            desiredCapabilities.setCapability("appium:bundleId", bundleId);
        if (!Strings.isNullOrEmpty(browser))
            desiredCapabilities.setCapability("appium:browser", browser);
        desiredCapabilities.setCapability("browserName", browserName);
        desiredCapabilities.setCapability("appium:newCommandTimeout", newCommandTimeout);
        desiredCapabilities.setCapability("appium:orientation", orientation);
        desiredCapabilities.setCapability("appium:autoWebview", autoWebview);
        desiredCapabilities.setCapability("appium:noReset", noReset);
        desiredCapabilities.setCapability("appium:autoGrantPermissions", autoGrantPermissions);
        desiredCapabilities.setCapability("appium:fullReset", fullReset);
        desiredCapabilities.setCapability("appium:nativeWebScreenshot", nativeScreenshot);
        desiredCapabilities.setCapability("appium:recreateChromeDriverSessions", recreateChromeDriverSessions);
        if (!Strings.isNullOrEmpty(appiumVersion))
            desiredCapabilities.setCapability("appium:appiumVersion", appiumVersion);
        if (!Strings.isNullOrEmpty(deviceOrientation))
            desiredCapabilities.setCapability("appium:deviceOrientation", deviceOrientation);
        if (!Strings.isNullOrEmpty(testobjectApiKey))
            desiredCapabilities.setCapability("appium:testobject_api_key", testobjectApiKey);
        if (null != testobjectSessionCreateTimeout)
            desiredCapabilities.setCapability("appium:testobject_session_creation_timeout", String.valueOf(testobjectSessionCreateTimeout));
        if (null != ignoreUnimportantViews)
            desiredCapabilities.setCapability("appium:ignoreUnimportantViews", ignoreUnimportantViews);
        if (null != phoneOnly)
            desiredCapabilities.setCapability("appium:phoneOnly", phoneOnly);
        if (null != idleTimeout)
            desiredCapabilities.setCapability("appium:idleTimeout", String.valueOf(idleTimeout));
        if (null != maxDuration)
            desiredCapabilities.setCapability("appium:maxDuration", String.valueOf(maxDuration));
        if (null != uiautomator2ServerLaunchTimeout)
            desiredCapabilities.setCapability("appium:uiautomator2ServerLaunchTimeout", String.valueOf(uiautomator2ServerLaunchTimeout));

    }

    private String getPath(String path) throws IOException {
        if (!path.contains("classpath:"))
            return path;
        return new ClassPathResource(path.replace("classpath:", "")).getFile().getAbsolutePath();
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

    public Boolean getAutoGrantPermissions() {
        return autoGrantPermissions;
    }

    public String getAutomationName() {
        return automationName;
    }

    public Boolean getNativeScreenshot() {
        return nativeScreenshot;
    }

    public Boolean getRecreateChromeDriverSessions() {
        return recreateChromeDriverSessions;
    }

    public String getAppiumVersion() {
        return appiumVersion;
    }

    public String getDeviceOrientation() {
        return deviceOrientation;
    }

    public Boolean getIgnoreUnimportantViews() {
        return ignoreUnimportantViews;
    }

    public Boolean getPhoneOnly() {
        return phoneOnly;
    }

    public String getTestobjectApiKey() {
        return testobjectApiKey;
    }

    public Integer getTestobjectSessionCreateTimeout() {
        return testobjectSessionCreateTimeout;
    }

    public Integer getIdleTimeout() {
        return idleTimeout;
    }

    public Integer getMaxDuration() {
        return maxDuration;
    }

    public Integer getUiautomator2ServerLaunchTimeout() {
        return uiautomator2ServerLaunchTimeout;
    }

    public Boolean getRemote() {
        return remote;
    }

    public String getUdid() {
        return udid;
    }

    public String getBundleId() {
        return bundleId;
    }
}
