package nz.co.afor.framework.api.rest;

import nz.co.afor.framework.api.HttpClientFactory;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.NTCredentials;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.CookieSpecSupport;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.cookie.IgnoreCookieSpecFactory;
import org.apache.http.client.config.CookieSpecs;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import static org.apache.http.auth.AuthScope.ANY_HOST;
import static org.apache.http.auth.AuthScope.ANY_PORT;

/**
 * Created by Matt Belcher on 10/10/2015.
 */
@Scope("thread")
@Configuration
public class RestTemplateFactory implements InitializingBean {

    @Value("${proxy.username:@null}")
    String proxyUsername;

    @Value("${proxy.password:@null}")
    String proxyPassword;

    @Value("${proxy.domain:@null}")
    String proxyDomain;

    @Value("${proxy.address:}")
    URI proxyAddress;

    @Value("${api.ssl.selfsigned:true}")
    Boolean acceptSelfSignedSSLCertificates;

    @Value("${api.pool.connections.max:}")
    Integer maxTotalConnections;

    @Value("${api.pool.connections.route.max:}")
    Integer maxConnectionsPerRoute;

    @Value("${api.pool.inactivity.validate:}")
    Integer validateAfterInactivityMilliseconds;

    @Value("${api.pool.connections.timeToLive:}")
    Integer timeToLive;

    @Value("${api.pool.connect.timeout:}")
    Integer connectTimeout;

    @Value("${api.pool.socket.timeout:}")
    Integer socketTimeout;

    @Autowired
    CookieStore basicCookieStore;

    HttpClientBuilder httpClientBuilder;
    ClientHttpRequestFactory requestFactory;


    @Override
    public void afterPropertiesSet() throws Exception {
        setupClientConfig();
    }

    public void setupClientConfig() throws KeyManagementException, NoSuchAlgorithmException {
        HttpClientFactory httpClientFactory = new HttpClientFactory()
                .withSelfSignedSSLCertificates(acceptSelfSignedSSLCertificates)
                .withMaxConnections(maxTotalConnections)
                .withMaxConnectionsPerRoute(maxConnectionsPerRoute)
                .withValidateAfterInactivity(validateAfterInactivityMilliseconds)
                .withTimeToLive(timeToLive)
                .withConnectTimeout(connectTimeout)
                .withSocketTimeoutMilliseconds(socketTimeout);
        if (null != proxyAddress && null != proxyUsername)
            httpClientFactory = httpClientFactory.withHttpProxy(proxyUsername, proxyPassword, proxyDomain, proxyAddress);
        httpClientBuilder = httpClientFactory.getHttpClientBuilder()
                .setDefaultCookieSpecRegistry(CookieSpecSupport.createDefaultBuilder().register("ignore", new IgnoreCookieSpecFactory()).build())
                .setDefaultRequestConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build())
                .setDefaultCookieStore(basicCookieStore);
        requestFactory = httpClientFactory.getClientHttpRequestFactory();
    }

    @Bean(name = "aforRestTemplate")
    public RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.setErrorHandler(new RestTemplateErrorHandler());
        return restTemplate;
    }

    public RestTemplate getRestTemplate(String username, String password, String domain) {
        AuthScope authScope = new AuthScope(ANY_HOST, ANY_PORT);
        NTCredentials credentials = new NTCredentials(username, password.toCharArray(), System.getenv("COMPUTERNAME"), domain);
        BasicCredentialsProvider credential = new BasicCredentialsProvider();
        credential.setCredentials(authScope, credentials);
        httpClientBuilder.setDefaultCredentialsProvider(credential);
        return getRestTemplate();
    }
}
