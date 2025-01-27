package nz.co.afor.framework.api.rest;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Matt Belcher on 20/01/2016.
 */
@Scope("thread")
@Component
public class AbstractHttpRequest implements InitializingBean {
    private final HttpHeaders headers = new HttpHeaders();

    @Autowired()
    @Qualifier("aforRestTemplate")
    private RestTemplate restTemplate;

    @Autowired
    CookieStore cookieStore;

    @Override
    public void afterPropertiesSet() {
        getHeaders().setContentType(MediaType.APPLICATION_JSON);
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public CookieStore getCookieStore() {
        return cookieStore;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }
}
