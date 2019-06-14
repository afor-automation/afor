package nz.co.afor.framework.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by Matt Belcher on 1/09/2015.
 */
@Component
public class Configuration {
    @Value("${selenide.baseUrl:http://localhost:8080}")
    private String baseUrl;

    @Value("${selenide.timeout:4000}")
    private Integer timeout;

    @Value("${selenide.pollingInterval:100}")
    private Integer pollingInterval;

    @PostConstruct
    public void setConfiguration() {
        com.codeborne.selenide.Configuration.baseUrl = getBaseUrl();
        com.codeborne.selenide.Configuration.timeout = getTimeout();
        com.codeborne.selenide.Configuration.pollingInterval = getPollingInterval();
    }

    public Integer getTimeout() {
        return timeout;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public Integer getPollingInterval() {
        return pollingInterval;
    }
}
