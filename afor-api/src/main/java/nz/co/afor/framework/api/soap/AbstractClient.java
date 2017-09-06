package nz.co.afor.framework.api.soap;

import nz.co.afor.framework.api.SSLTrustManager;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.soap.client.SoapFaultClientException;
import org.springframework.ws.transport.http.HttpsUrlConnectionMessageSender;

import javax.annotation.PostConstruct;
import javax.net.ssl.TrustManager;
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
    private final String contextPath;
    private final SoapActionCallback soapActionCallback;
    private String url;

    @Value("${api.ssl.selfsigned:true}")
    Boolean acceptSelfSignedSSLCertificates;

    protected AbstractClient(String contextPath, String soapActionCallback) {
        this.contextPath = contextPath;
        this.soapActionCallback = new SoapActionCallback(soapActionCallback);
    }

    public String getUrl() {
        return url;
    }

    @Required
    protected void setUrl(String url) {
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

    public Map<String, String> getSoapHeaders() {
        return soapActionCallback.getSoapHeaders();
    }

    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPaths(contextPath);
        return marshaller;
    }

    @PostConstruct
    protected void initialiseWebServiceTemplate() {
        WebServiceTemplate webServiceTemplate = getWebServiceTemplate();
        webServiceTemplate.setMarshaller(marshaller());
        webServiceTemplate.setUnmarshaller(marshaller());
        ClientInterceptor[] interceptors = ArrayUtils.add(webServiceTemplate.getInterceptors(), soapServiceInterceptor);
        webServiceTemplate.setInterceptors(interceptors);

        if (acceptSelfSignedSSLCertificates) {
            HttpsUrlConnectionMessageSender sender = new HttpsUrlConnectionMessageSender();
            sender.setTrustManagers(new TrustManager[]{new SSLTrustManager()});
            webServiceTemplate.setMessageSender(sender);
        }
        setWebServiceTemplate(webServiceTemplate);
    }

    protected Object send(Object requestPayload) {
        assertThat("The URL must be specified when calling the service", url, is(not(nullValue())));
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
}
