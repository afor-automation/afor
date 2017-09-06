package nz.co.afor.framework.api.soap;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapMessage;

import javax.xml.namespace.QName;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by belcherm on 18/05/2017.
 */
public class SoapActionCallback implements WebServiceMessageCallback {
    private final String soapAction;
    private Map<String, String> soapHeaders = new HashMap<>();

    public SoapActionCallback(String soapAction) {
        if (!StringUtils.hasText(soapAction)) {
            soapAction = "\"\"";
        }

        this.soapAction = soapAction;
    }

    public void setSoapHeaders(Map<String, String> soapHeaders) {
        this.soapHeaders = soapHeaders;
    }

    public void addSoapHeader(String header, String value) {
        soapHeaders.put(header, value);
    }

    public Map<String, String> getSoapHeaders() {
        return soapHeaders;
    }

    public void doWithMessage(WebServiceMessage message) throws IOException, TransformerException {
        Assert.isInstanceOf(SoapMessage.class, message);
        SoapMessage soapMessage = (SoapMessage) message;
        SoapHeader header = soapMessage.getSoapHeader();
        soapHeaders.forEach((name, value) -> header.addAttribute(new QName(name), value));
        soapMessage.setSoapAction(this.soapAction);
    }
}
