package nz.co.afor.framework.api;

import org.apache.hc.core5.util.TimeValue;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.NoConnectionReuseStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.ProxyAuthenticationStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
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
public class HttpClassicClientFactory {
    private static final Logger log = LoggerFactory.getLogger(HttpClassicClientFactory.class);

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

    public HttpClassicClientFactory withHttpProxy(URI proxyAddress) {
        this.proxyUsername = null;
        this.proxyPassword = null;
        this.proxyDomain = null;
        this.proxyAddress = proxyAddress;
        return this;
    }

    public HttpClassicClientFactory withHttpProxy(String proxyUsername, String proxyPassword, URI proxyAddress) {
        this.proxyUsername = "@null".equals(proxyUsername) ? null : proxyUsername;
        this.proxyPassword = "@null".equals(proxyPassword) ? null : proxyPassword;
        this.proxyDomain = null;
        this.proxyAddress = proxyAddress;
        return this;
    }

    public HttpClassicClientFactory withHttpProxy(String proxyUsername, String proxyPassword, String proxyDomain, URI proxyAddress) {
        this.proxyUsername = "@null".equals(proxyUsername) ? null : proxyUsername;
        this.proxyPassword = "@null".equals(proxyPassword) ? null : proxyPassword;
        this.proxyDomain = "@null".equals(proxyDomain) ? null : proxyDomain;
        this.proxyAddress = proxyAddress;
        return this;
    }

    public HttpClassicClientFactory withMaxConnections(Integer maxTotalConnections) {
        this.maxConnections = maxTotalConnections;
        return this;
    }

    public HttpClassicClientFactory withMaxConnectionsPerRoute(Integer maxConnectionsPerRoute) {
        this.maxConnectionsPerRoute = maxConnectionsPerRoute;
        return this;
    }

    public HttpClassicClientFactory withValidateAfterInactivity(Integer validateAfterInactivityMilliseconds) {
        this.validateAfterInactivity = validateAfterInactivityMilliseconds;
        return this;
    }

    public HttpClassicClientFactory withTimeToLive(Integer timeToLiveMilliseconds) {
        this.timeToLiveMilliseconds = timeToLiveMilliseconds;
        return this;
    }

    public HttpClassicClientFactory withConnectTimeout(Integer connectTimeoutMilliseconds) {
        this.connectTimeoutMilliseconds = connectTimeoutMilliseconds;
        return this;
    }

    public HttpClassicClientFactory withSocketTimeoutMilliseconds(Integer socketTimeoutMilliseconds) {
        this.socketTimeoutMilliseconds = socketTimeoutMilliseconds;
        return this;
    }

    public HttpClassicClientFactory withSelfSignedSSLCertificates(Boolean acceptSelfSignedSSLCertificates) {
        this.acceptSelfSignedSSLCertificates = acceptSelfSignedSSLCertificates;
        return this;
    }

    public HttpClientBuilder getHttpClientBuilder() throws KeyManagementException, NoSuchAlgorithmException {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

        PoolingHttpClientConnectionManager connectionManager;
        if (null != timeToLiveMilliseconds) {
            connectionManager = new PoolingHttpClientConnectionManager(timeToLiveMilliseconds, TimeUnit.MILLISECONDS);
        } else {
            connectionManager = new PoolingHttpClientConnectionManager();
        }

        if (null != maxConnections)
            connectionManager.setMaxTotal(maxConnections);
        if (null != maxConnectionsPerRoute)
            connectionManager.setDefaultMaxPerRoute(maxConnectionsPerRoute);

        httpClientBuilder.setConnectionManager(connectionManager);

        if (null != proxyAddress && null != proxyUsername) {
            log.debug("Using the http client proxy address '{}', username '{}' and domain '{}", proxyAddress, proxyUsername, proxyDomain);
            AuthScope authScope = new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT);
            CredentialsProvider credential = new BasicCredentialsProvider();
            if (null == proxyDomain) {
                credential.setCredentials(authScope, new UsernamePasswordCredentials(proxyUsername, proxyPassword));
            } else {
                credential.setCredentials(authScope, new NTCredentials(proxyUsername, proxyPassword, System.getenv("COMPUTERNAME"), proxyDomain));
            }
            HttpHost proxy = new HttpHost(proxyAddress.getHost(), proxyAddress.getPort(), proxyAddress.getScheme());

            httpClientBuilder.setProxy(proxy).setProxyAuthenticationStrategy(new ProxyAuthenticationStrategy()).setDefaultCredentialsProvider(credential);
        }

        // Accept self signed certificates
        if (acceptSelfSignedSSLCertificates) {
            log.debug("Accepting self signed certificates for API calls");

            SSLContext sslcontext = SSLContexts.custom().build();
            sslcontext.init(null, new X509TrustManager[]{new SSLTrustManager()}, new SecureRandom());
            SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(sslcontext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            httpClientBuilder.setSSLSocketFactory(factory);
        }

        return httpClientBuilder;
    }
}
