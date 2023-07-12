package nz.co.afor.framework.api;

import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.NTCredentials;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.util.TimeValue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.apache.http.auth.AuthScope.ANY_HOST;
import static org.apache.http.auth.AuthScope.ANY_PORT;

/**
 * Created by Matt on 14/09/2017.
 */
public class HttpClientFactory {
    private static final Logger log = LoggerFactory.getLogger(HttpClientFactory.class);

    String proxyUsername;

    String proxyPassword;

    String proxyDomain;

    URI proxyAddress;

    Boolean acceptSelfSignedSSLCertificates = false;

    private Integer maxConnections;
    private Integer maxConnectionsPerRoute;
    private Integer validateAfterInactivity;
    private Integer timeToLiveMilliseconds;
    private Integer connectTimeoutMilliseconds;
    private Integer socketTimeoutMilliseconds;

    public HttpClientFactory withHttpProxy(URI proxyAddress) {
        this.proxyUsername = null;
        this.proxyPassword = null;
        this.proxyDomain = null;
        this.proxyAddress = proxyAddress;
        return this;
    }

    public HttpClientFactory withHttpProxy(String proxyUsername, String proxyPassword, URI proxyAddress) {
        this.proxyUsername = "@null".equals(proxyUsername) ? null : proxyUsername;
        this.proxyPassword = "@null".equals(proxyPassword) ? null : proxyPassword;
        this.proxyDomain = null;
        this.proxyAddress = proxyAddress;
        return this;
    }

    public HttpClientFactory withHttpProxy(String proxyUsername, String proxyPassword, String proxyDomain, URI proxyAddress) {
        this.proxyUsername = "@null".equals(proxyUsername) ? null : proxyUsername;
        this.proxyPassword = "@null".equals(proxyPassword) ? null : proxyPassword;
        this.proxyDomain = "@null".equals(proxyDomain) ? null : proxyDomain;
        this.proxyAddress = proxyAddress;
        return this;
    }

    public HttpClientFactory withMaxConnections(Integer maxTotalConnections) {
        this.maxConnections = maxTotalConnections;
        return this;
    }

    public HttpClientFactory withMaxConnectionsPerRoute(Integer maxConnectionsPerRoute) {
        this.maxConnectionsPerRoute = maxConnectionsPerRoute;
        return this;
    }

    public HttpClientFactory withValidateAfterInactivity(Integer validateAfterInactivityMilliseconds) {
        this.validateAfterInactivity = validateAfterInactivityMilliseconds;
        return this;
    }

    public HttpClientFactory withTimeToLive(Integer timeToLiveMilliseconds) {
        this.timeToLiveMilliseconds = timeToLiveMilliseconds;
        return this;
    }

    public HttpClientFactory withConnectTimeout(Integer connectTimeoutMilliseconds) {
        this.connectTimeoutMilliseconds = connectTimeoutMilliseconds;
        return this;
    }

    public HttpClientFactory withSocketTimeoutMilliseconds(Integer socketTimeoutMilliseconds) {
        this.socketTimeoutMilliseconds = socketTimeoutMilliseconds;
        return this;
    }

    public HttpClientFactory withSelfSignedSSLCertificates(Boolean acceptSelfSignedSSLCertificates) {
        this.acceptSelfSignedSSLCertificates = acceptSelfSignedSSLCertificates;
        return this;
    }

    public HttpClientBuilder getHttpClientBuilder() throws KeyManagementException, NoSuchAlgorithmException {
        org.apache.hc.client5.http.impl.classic.HttpClientBuilder httpClientBuilder = org.apache.hc.client5.http.impl.classic.HttpClientBuilder.create();

        ConnectionConfig.Builder connectionConfig = ConnectionConfig.custom();
        if (null != timeToLiveMilliseconds)
            connectionConfig.setTimeToLive(timeToLiveMilliseconds, TimeUnit.MILLISECONDS);
        if (null != connectTimeoutMilliseconds)
            connectionConfig.setConnectTimeout(connectTimeoutMilliseconds, TimeUnit.MILLISECONDS);
        if (null != socketTimeoutMilliseconds)
            connectionConfig.setSocketTimeout(socketTimeoutMilliseconds, TimeUnit.MILLISECONDS);
        if (null != validateAfterInactivity)
            connectionConfig.setValidateAfterInactivity(TimeValue.of(Duration.ofMillis(validateAfterInactivity)));

        PoolingHttpClientConnectionManagerBuilder connectionManager = PoolingHttpClientConnectionManagerBuilder.create();

        connectionManager.setDefaultConnectionConfig(connectionConfig.build());
        if (null != maxConnections)
            connectionManager.setMaxConnTotal(maxConnections);
        if (null != maxConnectionsPerRoute)
            connectionManager.setMaxConnPerRoute(maxConnectionsPerRoute);

        // Accept self signed certificates
        if (acceptSelfSignedSSLCertificates) {
            log.debug("Accepting self signed certificates for API calls");

            SSLContext sslcontext = SSLContexts.custom().build();
            sslcontext.init(null, new X509TrustManager[]{new SSLTrustManager()}, new SecureRandom());
            SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(sslcontext, new NoopHostnameVerifier());
            connectionManager.setSSLSocketFactory(factory);
        }
        httpClientBuilder.setConnectionManager(connectionManager.build());

        // Set up proxy authentication if we have all the required proxy details
        if (null != proxyAddress && null != proxyUsername) {
            log.debug("Using the http client proxy address '{}', username '{}' and domain '{}", proxyAddress, proxyUsername, proxyDomain);
            AuthScope authScope = new AuthScope(ANY_HOST, ANY_PORT);
            BasicCredentialsProvider credential = new BasicCredentialsProvider();
            if (null == proxyDomain) {
                credential.setCredentials(authScope, new UsernamePasswordCredentials(proxyUsername, null != proxyPassword ? proxyPassword.toCharArray() : null));
            } else {
                credential.setCredentials(authScope, new NTCredentials(proxyUsername, null != proxyPassword ? proxyPassword.toCharArray() : null, System.getenv("COMPUTERNAME"), proxyDomain));
            }
            HttpHost proxy = new HttpHost(proxyAddress.getScheme(), proxyAddress.getHost(), proxyAddress.getPort());

            httpClientBuilder.setProxy(proxy).setDefaultCredentialsProvider(credential);
        }

        return httpClientBuilder;
    }

    public ClientHttpRequestFactory getClientHttpRequestFactory() throws NoSuchAlgorithmException, KeyManagementException {
        return new HttpComponentsClientHttpRequestFactory(getHttpClientBuilder().build());
    }
}
