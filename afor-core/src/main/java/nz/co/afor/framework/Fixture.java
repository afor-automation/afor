package nz.co.afor.framework;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Copyright afor
 * Created by Matt Belcher on 12/10/2015.
 */
@Component
public class Fixture {

    @Autowired
    FixtureReader fixtureReader;

    @Value("${nz.co.afor.fixture.dateformat:yyyy-MM-dd'T'HH:mm:ss.SSSZ}")
    private String datePattern;

    @Value("${nz.co.afor.fixture.timezone:UTC}")
    private String timezone;

    protected ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
        dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
        objectMapper.setDateFormat(dateFormat);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper;
    }

    public <T> T getFixture(final Class<T> clazz) throws IOException {
        return getObjectMapper().readValue(fixtureReader.getFixtureStream(), clazz);
    }

    public <T> T getFixture(final String fixturePath, final Class<T> clazz) throws IOException {
        return getObjectMapper().readValue(fixtureReader.getFixtureStream(fixturePath), clazz);
    }

    @SuppressWarnings("unchecked")
    public <T> T getFixture(final Type classOfT) throws IOException {
        return (T) getFixture(com.fasterxml.jackson.databind.type.TypeFactory.rawClass(classOfT));
    }

    @SuppressWarnings("unchecked")
    public <T> T getFixture(final String fixturePath, final Type classOfT) throws IOException {
        return (T) getFixture(fixturePath, com.fasterxml.jackson.databind.type.TypeFactory.rawClass(classOfT));
    }

    public <T> T getFixture(final TypeReference<T> classOfT) throws IOException {
        return getObjectMapper().readValue(fixtureReader.getFixtureStream(), classOfT);
    }

    public <T> T getFixture(final String fixturePath, final TypeReference<T> classOfT) throws IOException {
        return getObjectMapper().readValue(fixtureReader.getFixtureStream(fixturePath), classOfT);
    }
}
