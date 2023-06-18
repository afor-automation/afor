package nz.co.afor.framework.api.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import nz.co.afor.framework.api.SSLTrustManager;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.jaxrs.client.Client;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.transport.http.HTTPConduit;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import javax.net.ssl.TrustManager;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Created by Matt on 30/05/2017.
 */
public abstract class AbstractApiFactory {
    private static ApplicationContext applicationContext;

    @Value("${nz.co.afor.fixture.dateformat:yyyy-MM-dd'T'HH:mm:ss.SSSZ}")
    private String datePattern;

    @Value("${nz.co.afor.fixture.timezone:UTC}")
    private String timezone;

    @Value("${api.hostUrl}")
    private String apiBaseUrl;

    @Value("${api.ssl.selfsigned:true}")
    Boolean acceptSelfSignedSSLCertificates;

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        AbstractApiFactory.applicationContext = applicationContext;
    }

    public <T> T getBean(Class<T> tClass) {
        return getApplicationContext().getBean(tClass);
    }

    private static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Bean
    public ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
        dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
        objectMapper.setDateFormat(dateFormat);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper;
    }

    protected void setupWebClient(Client client) {
        // Disable SSL certificate validation
        if (acceptSelfSignedSSLCertificates) {
            HTTPConduit conduit = WebClient.getConfig(client).getHttpConduit();
            TLSClientParameters params = conduit.getTlsClientParameters();
            if (params == null) {
                params = new TLSClientParameters();
                conduit.setTlsClientParameters(params);
            }
            params.setTrustManagers(new TrustManager[]{new SSLTrustManager()});
            params.setDisableCNCheck(true);
        }
    }
}
