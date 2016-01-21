package nz.co.afor.framework.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Created by Matt Belcher on 20/01/2016.
 */
@Component
public class AbstractHttpRequest {
    private HttpHeaders headers = new HttpHeaders();

    @Resource(name = "aforRestTemplate")
    private RestTemplate restTemplate;

    @PostConstruct
    private void setupRequest() {
        getHeaders().setContentType(MediaType.APPLICATION_JSON);
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }
}
