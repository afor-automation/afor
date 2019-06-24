package nz.co.afor.framework.api.soap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.WebServiceIOException;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.HttpComponentsConnection;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by belcherm on 18/05/2017.
 */
public class SoapServiceInterceptor implements ClientInterceptor {
    private static final Logger log = LoggerFactory.getLogger(SoapServiceInterceptor.class);

    private Map<String, String> headers = new HashMap<>();

    public void setHttpHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getHttpHeaders() {
        return headers;
    }

    public void addHttpHeader(String header, String value) {
        headers.put(header, value);
    }

    @Override
    public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {
        TransportContext context = TransportContextHolder.getTransportContext();
        try {
            log.info("Client Request URI '{}'", context.getConnection().getUri());
        } catch (URISyntaxException e) {
            log.error("Failed to log Client Request URI", e);
        }
        if (headers.size() > 0)
            log.info("Client Request Custom Headers '{}'", headers);

        String[] soapActions = ((SaajSoapMessage) messageContext.getRequest()).getSaajMessage().getMimeHeaders().getHeader("SOAPAction");
        if (soapActions.length == 1)
            log.info("Client Request SOAPAction '{}'", soapActions[0]);
        for (Map.Entry<String, String> entry : headers.entrySet())
            try {
                ((HttpComponentsConnection) context.getConnection()).addRequestHeader(entry.getKey(), entry.getValue());
            } catch (IOException e) {
                throw new WebServiceIOException("Failed to add headers to the SOAP service request", e);
            }

        if (log.isInfoEnabled()) {
            try {
                FastByteArrayOutputStream os = new FastByteArrayOutputStream(8192);
                messageContext.getRequest().writeTo(os);
                log.info("Client Request Message '{}'", os.toString());
            } catch (IOException e) {
                throw new WebServiceIOException(e.getMessage(), e);
            }
        }
        return true;
    }

    @Override
    public boolean handleResponse(MessageContext messageContext) throws WebServiceClientException {
        TransportContext context = TransportContextHolder.getTransportContext();
        if (log.isInfoEnabled()) {
            try {
                FastByteArrayOutputStream os = new FastByteArrayOutputStream();
                messageContext.getResponse().writeTo(os);
                log.info("Client Response Message '{}'", os.toByteArray());
            } catch (IOException e) {
                throw new WebServiceIOException(e.getMessage(), e);
            }
        }

        try {
            if (((HttpComponentsConnection) context.getConnection()).hasFault()) {
                log.warn("Client Response is a fault");
                return false;
            }
        } catch (IOException e) {
            log.error("Failed to log Client fault", e);
        }
        return true;
    }

    @Override
    public boolean handleFault(MessageContext messageContext) throws WebServiceClientException {
        return true;
    }

    @Override
    public void afterCompletion(MessageContext messageContext, Exception e) throws WebServiceClientException {
    }
}