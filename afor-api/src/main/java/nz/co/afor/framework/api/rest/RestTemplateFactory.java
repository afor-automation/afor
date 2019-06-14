package nz.co.afor.framework.api.rest;

import nz.co.afor.framework.api.HttpClientFactory;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Matt Belcher on 10/10/2015.
 */
@Configuration
public class RestTemplateFactory {

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

    @Value("${api.pool.enabled:false}")
    Boolean useConnectionPool;

    @Value("${api.pool.connections.max:10}")
    Integer maxTotalConnections;

    @Value("${api.pool.connections.route.max:5}")
    Integer defaultMaxPerRoute;

    @Value("${api.pool.inactivity.validate:2000}")
    Integer validateAfterInactivityMilliseconds;

    @Value("${api.pool.connections.timeToLive:300000}")
    Integer timeToLive;

    @Autowired
    CookieStore basicCookieStore;

    HttpClientBuilder httpClientBuilder;
    ClientHttpRequestFactory requestFactory;

    @PostConstruct
    public void setupClientConfig() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
        HttpClientFactory httpClientFactory = new HttpClientFactory();
        if (useConnectionPool)
            httpClientFactory = httpClientFactory.withConnectionPooling(maxTotalConnections, defaultMaxPerRoute, validateAfterInactivityMilliseconds, timeToLive);
        if (acceptSelfSignedSSLCertificates)
            httpClientFactory = httpClientFactory.withSelfSignedSSLCertificates();
        if (proxyUsername.compareTo("@null") != 0 && null != proxyAddress)
            httpClientFactory = httpClientFactory.withHttpProxy(proxyUsername, proxyPassword, proxyDomain, proxyAddress);
        httpClientBuilder = httpClientFactory.getHttpClientBuilder().setDefaultCookieStore(basicCookieStore);
        requestFactory = httpClientFactory.getClientHttpRequestFactory();
    }

    @Bean(name = "aforRestTemplate")
    public RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.setErrorHandler(new RestTemplateErrorHandler());
        return restTemplate;
    }

    public RestTemplate getRestTemplate(String username, String password, String domain) {
        AuthScope authScope = new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT);
        CredentialsProvider credential = new BasicCredentialsProvider();
        credential.setCredentials(authScope, new NTCredentials(username, password, System.getenv("COMPUTERNAME"), domain));
        HttpClientContext context = HttpClientContext.create();
        context.setCredentialsProvider(credential);
        httpClientBuilder.setDefaultCredentialsProvider(credential);
        return getRestTemplate();
    }
}
