package nz.co.afor.framework;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Copyright afor
 * Created by Matt Belcher on 12/10/2015.
 */
@Component
public class FixtureReader {

    @Value("${nz.co.afor.fixture.path:}")
    private String fixturePath;

    private String getFixturePath() {
        return fixturePath;
    }

    private InputStream getResourceAsStream(String resourcePath) {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(resourcePath);
        if (null == inputStream)
            throw new InvalidResourceException(String.format("The fixture resource '%s' does not exist in the path or classpath", getFixturePath()));
        return inputStream;
    }

    public InputStream getFixtureStream() {
        return getResourceAsStream(getFixturePath());
    }

    public InputStream getFixtureStream(String path) {
        return getResourceAsStream(path);
    }

    public InputStreamReader getFixtureReader() {
        return new InputStreamReader(getResourceAsStream(getFixturePath()));
    }

    public InputStreamReader getFixtureReader(String path) {
        return new InputStreamReader(getResourceAsStream(path));
    }

    private static class InvalidResourceException extends RuntimeException {
        InvalidResourceException(String message) {
            super(message);
        }
    }
}
