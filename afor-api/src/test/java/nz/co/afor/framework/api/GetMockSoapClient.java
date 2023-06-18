package nz.co.afor.framework.api;

import nz.co.afor.framework.api.soap.AbstractClient;
import nz.co.afor.framework.mock.soap.GetMockRequest;
import nz.co.afor.framework.mock.soap.GetMockResponse;
import nz.co.afor.framework.mock.soap.Mock;
import nz.co.afor.framework.mock.soap.ObjectFactory;
import org.springframework.stereotype.Component;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@Component
public class GetMockSoapClient extends AbstractClient {

    public GetMockSoapClient() {
        super("http://www.afor.co.nz/framework/mock/soap", "", "http://127.0.0.1:16151/ws");
    }

    protected GetMockSoapClient(String contextPath, String soapActionCallback) {
        super(contextPath, soapActionCallback, "http://127.0.0.1:16151/ws");
    }

    protected GetMockSoapClient(String soapActionCallback) {
        super(soapActionCallback, "http://127.0.0.1:16151/ws");
    }

    public GetMockResponse getMockRequest(Mock mock) throws KeyManagementException, NoSuchAlgorithmException {
        GetMockRequest getMockRequest = new ObjectFactory().createGetMockRequest();
        getMockRequest.setMock(mock);
        return send(getMockRequest, GetMockResponse.class);
    }
}
