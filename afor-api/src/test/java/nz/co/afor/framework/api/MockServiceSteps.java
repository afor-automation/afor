package nz.co.afor.framework.api;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import nz.co.afor.framework.mock.Application;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;

/**
 * Created by Matt Belcher on 19/08/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Application.class, Get.class, Post.class, RestTemplate.class, RestTemplateFactory.class}, loader = SpringApplicationContextLoader.class)
@WebAppConfiguration
@IntegrationTest
public class MockServiceSteps {

    @Autowired
    Get get;

    @Autowired
    Post post;

    ResponseEntity<String> response;

    @Value("${local.server.port}")
    private int serverPort;

    @Given("^I have a mock service running$")
    public void I_have_a_mock_service_running() throws Throwable {
        assertThat(serverPort, is(greaterThan(0)));
    }

    @When("^I send a GET request to the \"([^\"]*)\" endpoint$")
    public void I_send_a_GET_request_to_the_endpoint(String path) throws Throwable {
        response = get.request(String.format("http://localhost:%s/%s", serverPort, path), String.class);
    }

    @When("^I send a GET request to the \"([^\"]*)\" endpoint with the headers$")
    public void I_send_a_GET_request_to_the_endpoint_with_the_headers(String path, Map<String, String> headers) throws Throwable {
        get.getHeaders().setAll(headers);
        response = get.request(String.format("http://localhost:%s/%s", serverPort, path), String.class);
    }

    @When("^I send a POST request to the \"([^\"]*)\" endpoint$")
    public void I_send_a_POST_request_to_the_endpoint(String path) throws Throwable {
        response = post.request(String.format("http://localhost:%s/%s", serverPort, path), "test", String.class);
    }

    @When("^I send a POST request to the \"([^\"]*)\" endpoint with the headers$")
    public void I_send_a_POST_request_to_the_endpoint_with_the_headers(String path, Map<String, String> headers) throws Throwable {
        post.getHeaders().setAll(headers);
        response = post.request(String.format("http://localhost:%s/%s", serverPort, path), "test", String.class);
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
}
