package nz.co.afor.framework.api.soap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.WebServiceIOException;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.HttpComponentsConnection;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by belcherm on 18/05/2017.
 */
public class SoapServiceInterceptor implements ClientInterceptor {
    Logger log = LoggerFactory.getLogger(this.getClass());

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
        for (Map.Entry<String, String> entry : headers.entrySet())
            try {
                ((HttpComponentsConnection) context.getConnection()).addRequestHeader(entry.getKey(), entry.getValue());
            } catch (IOException e) {
                throw new WebServiceIOException("Failed to add headers to the SOAP service request", e);
            }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            messageContext.getRequest().writeTo(os);
        } catch (IOException e) {
            throw new WebServiceIOException(e.getMessage(), e);
        }
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

        String request = new String(os.toByteArray());
        log.info("Client Request Message '{}'", request);
        return true;
    }

    @Override
    public boolean handleResponse(MessageContext messageContext) throws WebServiceClientException {
        TransportContext context = TransportContextHolder.getTransportContext();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            messageContext.getResponse().writeTo(os);
        } catch (IOException e) {
            throw new WebServiceIOException(e.getMessage(), e);
        }

        String response = new String(os.toByteArray());
        log.info("Client Response Message '{}'", response);
        try {
            if (((HttpComponentsConnection) context.getConnection()).hasFault()) {
                log.info("Client Response is a fault");
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