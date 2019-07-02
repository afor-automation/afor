package nz.co.afor.framework.mock;

import nz.co.afor.soap.mock.GetMockRequest;
import nz.co.afor.soap.mock.GetMockResponse;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class SoapMock {

    @PayloadRoot(namespace = "http://www.afor.co.nz/soap/mock", localPart = "getMockRequest")
    @ResponsePayload
    public GetMockResponse getSoapMock(@RequestPayload GetMockRequest request) {
        GetMockResponse response = new GetMockResponse();
        response.setMock(request.getMock());
        return response;
    }
}
