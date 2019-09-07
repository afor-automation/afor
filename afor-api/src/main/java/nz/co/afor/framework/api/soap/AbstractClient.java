package nz.co.afor.framework.api.soap;

import nz.co.afor.framework.api.HttpClientFactory;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.soap.client.SoapFaultClientException;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * Created by Matt on 6/09/2017.
 */
public class AbstractClient extends WebServiceGatewaySupport {
    private SoapServiceInterceptor soapServiceInterceptor = new SoapServiceInterceptor();
    private String contextPath;
    private final SoapActionCallback soapActionCallback;
    private String url;

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

    protected AbstractClient(String contextPath, String soapActionCallback, String url) {
        this.contextPath = contextPath;
        this.soapActionCallback = new SoapActionCallback(soapActionCallback);
        this.url = url;
    }

    protected AbstractClient(String soapActionCallback, String url) {
        this.soapActionCallback = new SoapActionCallback(soapActionCallback);
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setHttpHeaders(Map<String, String> headers) {
        soapServiceInterceptor.setHttpHeaders(headers);
    }

    public void addHttpHeader(String header, String value) {
        soapServiceInterceptor.addHttpHeader(header, value);
    }

    public Map<String, String> getHttpHeaders() {
        return soapServiceInterceptor.getHttpHeaders();
    }

    public void setSoapHeaders(Map<String, String> soapHeaders) {
        soapActionCallback.setSoapHeaders(soapHeaders);
    }

    public void addSoapHeader(String header, String value) {
        soapActionCallback.addSoapHeader(header, value);
    }

    public void addSoapHeader(Object header) {
        soapActionCallback.setSoapHeader(header);
    }

    public Map<String, String> getSoapHeaders() {
        return soapActionCallback.getSoapHeaders();
    }

    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPaths(contextPath);
        return marshaller;
    }

    protected void setContextPath(Class tClass) throws KeyManagementException, NoSuchAlgorithmException {
        contextPath = tClass.getPackage().getName();
        initialiseWebServiceTemplate();
    }

    @PostConstruct
    protected void initialiseWebServiceTemplate() throws NoSuchAlgorithmException, KeyManagementException {
        if (null != contextPath) {
            WebServiceTemplate webServiceTemplate = getWebServiceTemplate();
            webServiceTemplate.setMarshaller(marshaller());
            webServiceTemplate.setUnmarshaller(marshaller());
            ClientInterceptor[] interceptors = ArrayUtils.add(webServiceTemplate.getInterceptors(), soapServiceInterceptor);
            webServiceTemplate.setInterceptors(interceptors);

            HttpClientFactory httpClientFactory = new HttpClientFactory();
            if (acceptSelfSignedSSLCertificates)
                httpClientFactory = httpClientFactory.withSelfSignedSSLCertificates();
            if (proxyUsername.compareTo("@null") != 0 && null != proxyAddress)
                httpClientFactory = httpClientFactory.withHttpProxy(proxyUsername, proxyPassword, proxyDomain, proxyAddress);
            HttpComponentsMessageSender sender = new HttpComponentsMessageSender();
            sender.setHttpClient(httpClientFactory.getHttpClientBuilder().addInterceptorFirst(new HttpComponentsMessageSender.RemoveSoapHeadersInterceptor()).build());
            webServiceTemplate.setMessageSender(sender);
            setWebServiceTemplate(webServiceTemplate);
        }
    }

    protected Object send(Object requestPayload) {
        if (null == url)
            throw new WebServiceException("The URL must be specified when calling the service");
        if (null == contextPath)
            throw new WebServiceException("The contextPath must be specified when calling the service");
        try {
            return getWebServiceTemplate()
                    .marshalSendAndReceive(url,
                            requestPayload,
                            soapActionCallback);
        } catch (SoapFaultClientException soapFaultClientException) {
            logger.warn("SOAP Fault thrown when unmarshalling response", soapFaultClientException);
            throw soapFaultClientException;
        }
    }

    protected <T> T send(Object requestPayload, Class<T> tClass) throws NoSuchAlgorithmException, KeyManagementException {
        if (null == url)
            throw new WebServiceException("The URL must be specified when calling the service");
        setContextPath(tClass);
        try {
            return tClass.cast(getWebServiceTemplate()
                    .marshalSendAndReceive(url,
                            requestPayload,
                            soapActionCallback));
        } catch (SoapFaultClientException soapFaultClientException) {
            logger.warn("SOAP Fault thrown when unmarshalling response", soapFaultClientException);
            throw soapFaultClientException;
        }
    }

    private static class WebServiceException extends RuntimeException {
        WebServiceException(String message) {
            super(message);
        }
    }
}
