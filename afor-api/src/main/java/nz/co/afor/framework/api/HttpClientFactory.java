package nz.co.afor.framework.api;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.ProxyAuthenticationStrategy;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
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

/**
 * Created by Matt on 14/09/2017.
 */
@SuppressWarnings("deprecation")
public class HttpClientFactory {
    private static Logger log = LoggerFactory.getLogger(HttpClientFactory.class);

    Boolean proxy = false;

    Boolean useConnectionPool = false;

    String proxyUsername;

    String proxyPassword;

    String proxyDomain;

    URI proxyAddress;

    Boolean acceptSelfSignedSSLCertificates = false;
    private int maxTotal;
    private int defaultMaxPerRoute;
    private int validateAfterInactivity;

    public HttpClientFactory withHttpProxy(String proxyUsername, String proxyPassword, String proxyDomain, URI proxyAddress) {
        this.proxy = true;
        this.proxyUsername = "@null".equals(proxyUsername) ? null : proxyUsername;
        this.proxyPassword = "@null".equals(proxyPassword) ? null : proxyPassword;
        this.proxyDomain = "@null".equals(proxyDomain) ? null : proxyDomain;
        this.proxyAddress = proxyAddress;
        return this;
    }

    public HttpClientFactory withConnectionPooling(int maxTotalConnections, int defaultMaxPerRoute, int validateAfterInactivityMilliseconds) {
        this.useConnectionPool = true;
        this.maxTotal = maxTotalConnections;
        this.defaultMaxPerRoute = defaultMaxPerRoute;
        this.validateAfterInactivity = validateAfterInactivityMilliseconds;
        return this;
    }

    public HttpClientFactory withSelfSignedSSLCertificates() {
        acceptSelfSignedSSLCertificates = true;
        return this;
    }

    public HttpClientBuilder getHttpClientBuilder() throws KeyManagementException, NoSuchAlgorithmException {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

        if (useConnectionPool) {
            PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
            connectionManager.setMaxTotal(maxTotal);
            connectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
            connectionManager.setValidateAfterInactivity(validateAfterInactivity);
            httpClientBuilder.setConnectionManager(connectionManager);
        } else {
            httpClientBuilder.setConnectionManager(new BasicHttpClientConnectionManager());
        }

        // Set up proxy authentication if we have all the required proxy details
        if (proxy) {
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

    public ClientHttpRequestFactory getClientHttpRequestFactory() throws NoSuchAlgorithmException, KeyManagementException {
        return new HttpComponentsClientHttpRequestFactory(getHttpClientBuilder().build());
    }
}
