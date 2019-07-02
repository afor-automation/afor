package nz.co.afor.framework.api;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import nz.co.afor.framework.api.rest.Get;
import nz.co.afor.framework.api.rest.Post;
import nz.co.afor.soap.mock.Enum;
import nz.co.afor.soap.mock.GetMockResponse;
import nz.co.afor.soap.mock.Mock;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;

/**
 * Created by Matt Belcher on 19/08/2015.
 */
public class MockServiceSteps {

    @Autowired
    Get get;

    @Autowired
    Post post;

    @Autowired
    GetMockSoapClient getMockSoapClient;

    ResponseEntity<String> response;

    private final int serverPort = 16151;
    private GetMockResponse soapResponse;
    private Mock mockRequest;

    @Given("^I have a mock service running$")
    public void I_have_a_mock_service_running() throws Throwable {
        assertThat(serverPort, is(greaterThan(0)));
    }

    @When("^I send a GET request to the \"([^\"]*)\" endpoint$")
    public void I_send_a_GET_request_to_the_endpoint(String path) throws Throwable {
        response = get.request(String.format("http://127.0.0.1:%s/%s", serverPort, path), String.class);
    }

    @When("^I send a GET request to the \"([^\"]*)\" endpoint with the headers$")
    public void I_send_a_GET_request_to_the_endpoint_with_the_headers(String path, Map<String, String> headers) throws Throwable {
        get.getHeaders().setAll(headers);
        response = get.request(String.format("http://127.0.0.1:%s/%s", serverPort, path), String.class);
    }

    @When("^I send a POST request to the \"([^\"]*)\" endpoint$")
    public void I_send_a_POST_request_to_the_endpoint(String path) throws Throwable {
        response = post.request(String.format("http://127.0.0.1:%s/%s", serverPort, path), "test", String.class);
    }

    @When("^I send a POST request to the \"([^\"]*)\" endpoint with the headers$")
    public void I_send_a_POST_request_to_the_endpoint_with_the_headers(String path, Map<String, String> headers) throws Throwable {
        post.getHeaders().setAll(headers);
        response = post.request(String.format("http://127.0.0.1:%s/%s", serverPort, path), "test", String.class);
    }

    @Then("^I should get an HTTP (\\d+) response$")
    public void I_should_get_an_HTTP_response(int responseCode) throws Throwable {
        assertThat(response.getStatusCode().value(), is(equalTo(responseCode)));
    }

    @And("^the response should have the headers$")
    public void the_response_should_have_the_headers(Map<String, String> headers) throws Throwable {
        for (Map.Entry<String, String> header : headers.entrySet())
            assertThat(String.valueOf(response.getHeaders().get(header.getKey())), is(containsString(header.getValue())));
    }

    @When("I send a getMockRequest request to the ws endpoint")
    public void iSendAGetMockRequestRequestToTheEndpoint() throws NoSuchAlgorithmException, KeyManagementException {
        mockRequest = new Mock();
        mockRequest.setString(RandomStringUtils.randomAlphanumeric(100));
        mockRequest.setInteger(new Random().nextInt());
        mockRequest.setEnum(Enum.ONE);
        soapResponse = getMockSoapClient.getMockRequest(mockRequest);
    }

    @Then("I should receive a valid SOAP response")
    public void iShouldReceiveAValidSOAPResponse() {
        assertThat("The soap response should be valid", soapResponse.getMock(), is(not(nullValue())));
        assertThat("The soap response should be valid", soapResponse.getMock().getString(), equalTo(mockRequest.getString()));
        assertThat("The soap response should be valid", soapResponse.getMock().getInteger(), equalTo(mockRequest.getInteger()));
        assertThat("The soap response should be valid", soapResponse.getMock().getEnum(), equalTo(mockRequest.getEnum()));
    }
}
