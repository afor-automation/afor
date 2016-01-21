package nz.co.afor.framework;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

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
        assertThat(String.format("The fixture resource '%s' should exist in the path or classpath", getFixturePath()), inputStream, is(not(nullValue())));
        return inputStream;
    }

    public InputStreamReader getFixtureReader() {
        return new InputStreamReader(getResourceAsStream(getFixturePath()));
    }

    public InputStreamReader getFixtureReader(String path) {
        return new InputStreamReader(getResourceAsStream(path));
    }
}
