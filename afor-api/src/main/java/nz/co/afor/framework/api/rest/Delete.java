package nz.co.afor.framework.api.rest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * Created by Matt Belcher on 10/10/2015.
 */
@Scope("thread")
@Component
public class Delete extends AbstractHttpRequest {
    private static final Log log = LogFactory.getLog(Delete.class);

    public <T> ResponseEntity<T> request(String uri, Class<T> responseType) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uri);
        return request(builder.build().encode().toUri(), responseType);
    }

    public <T> ResponseEntity<T> request(URI uri, Class<T> responseType) {
        log.info(String.format("Sending DELETE request to URL '%s'", uri.toString()));
        HttpEntity entity = new HttpEntity(getHeaders());
        ResponseEntity<T> response = getRestTemplate().exchange(uri, HttpMethod.DELETE, entity, responseType);
        log.info(String.format("Received the status code from request '%s'", response.getStatusCode()));
        log.info(String.format("Received the response body '%s'", response.getBody()));
        return response;
    }
}
