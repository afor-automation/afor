package nz.co.afor.framework.api.rest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.net.URI;

/**
 * Created by Matt Belcher on 10/10/2015.
 */
public class RestTemplateErrorHandler implements ResponseErrorHandler {
    private static final Log log = LogFactory.getLog(RestTemplateErrorHandler.class);

    @Override
    public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
        log.error(String.format("Received '%s' error response from '%s' '%s' '%s'", method, url, response.getStatusCode(), response.getStatusText()));
    }

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().isError();
    }

    public static boolean hasError(HttpStatus status) {
        HttpStatus.Series series = status.series();
        return (HttpStatus.Series.CLIENT_ERROR.equals(series) || HttpStatus.Series.SERVER_ERROR.equals(series));
    }
}
