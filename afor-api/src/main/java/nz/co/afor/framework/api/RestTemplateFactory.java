package nz.co.afor.framework.api;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.ProxyAuthenticationStrategy;
import org.apache.http.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by Matt Belcher on 10/10/2015.
 */
@SuppressWarnings("deprecation")
@Component
public class RestTemplateFactory {
    private static Log log = LogFactory.getLog(RestTemplateFactory.class);

    @Value("${proxy.username:@null}")
    String proxyUsername;

    @Value("${proxy.password:@null}")
    String proxyPassword;

    @Value("${proxy.domain:@null}")
    String proxyDomain;

    @Value("${proxy.address:}")
    URI proxyAddress;

    @Value("${api.ssl.selfsigned:false}")
    Boolean acceptSelfSignedSSLCertificates;

    HttpClientBuilder httpClientBuilder;
    ClientHttpRequestFactory requestFactory;

    @PostConstruct
    public void setupClientConfig() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
        httpClientBuilder = HttpClientBuilder.create();

        // Set up proxy authentication if we have all the required proxy details
        if (proxyUsername.compareTo("@null") != 0 && proxyPassword.compareTo("@null") != 0 && proxyDomain.compareTo("@null") != 0 && null != proxyAddress) {
            log.info(String.format("Creating a new Rest Template connection using the proxy username '%s' and domain '%s'", proxyUsername, proxyDomain));
            AuthScope authScope = new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT);
            CredentialsProvider credential = new BasicCredentialsProvider();
            credential.setCredentials(authScope, new NTCredentials(proxyUsername, proxyPassword, System.getenv("COMPUTERNAME"), proxyDomain));
            HttpHost proxy = new HttpHost(proxyAddress.getHost(), proxyAddress.getPort(), proxyAddress.getScheme());

            httpClientBuilder.setProxy(proxy).setProxyAuthenticationStrategy(new ProxyAuthenticationStrategy()).setDefaultCredentialsProvider(credential);
        }

        // Accept self signed certificates
        if (acceptSelfSignedSSLCertificates) {
            log.info("Accepting self signed certificates for API calls");

            SSLContext sslcontext = SSLContexts.custom().build();
            sslcontext.init(null, new X509TrustManager[]{new SSLTrustManager()}, new SecureRandom());
            SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(sslcontext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            httpClientBuilder.setSSLSocketFactory(factory);
        }
        requestFactory = new HttpComponentsClientHttpRequestFactory(httpClientBuilder.build());
    }

    @Bean
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
