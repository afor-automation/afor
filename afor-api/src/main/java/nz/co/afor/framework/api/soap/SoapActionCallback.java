package nz.co.afor.framework.api.soap;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapMessage;
import org.w3c.dom.Document;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by belcherm on 18/05/2017.
 */
public class SoapActionCallback implements WebServiceMessageCallback {
    private final String soapAction;
    private Map<String, String> soapHeaders = new HashMap<>();
    private Object soapComplexHeader;

    public SoapActionCallback(String soapAction) {
        if (!StringUtils.hasText(soapAction)) {
            soapAction = "\"\"";
        }

        this.soapAction = soapAction;
    }

    public void setSoapHeaders(Map<String, String> soapHeaders) {
        this.soapHeaders = soapHeaders;
    }

    public void setSoapHeader(Object header) {
        soapComplexHeader = header;
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
        SoapHeader soapHeader = soapMessage.getSoapHeader();
        if (null != soapComplexHeader) {
            try {
                JAXBContext context = JAXBContext.newInstance(soapComplexHeader.getClass());

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();

                Document headerDocument = builder.newDocument();

                javax.xml.bind.Marshaller marshaller = context.createMarshaller();
                marshaller.marshal(soapComplexHeader, headerDocument);

                Transformer t = TransformerFactory.newInstance().newTransformer();

                DOMSource headerSource = new DOMSource(headerDocument);

                t.transform(headerSource, soapHeader.getResult());
            } catch (JAXBException | ParserConfigurationException e) {
                throw new TransformerException("Failed to add soap headers", e);
            }
        }
        soapHeaders.forEach((name, value) -> soapHeader.addAttribute(new QName(name), value));
        soapMessage.setSoapAction(this.soapAction);
    }
}
