package nz.co.afor.framework;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Copyright afor
 * Created by Matt Belcher on 12/10/2015.
 */
@Component
public class Fixture {

    @Autowired(required = false)
    @Qualifier("nz.co.afor.fixture.model")
    Class tClass;

    @Autowired
    GsonFactory gsonFactory;

    @Autowired
    FixtureReader fixtureReader;

    public <T> T getFixture() throws IOException {
        return (T) getFixture(tClass);
    }

    public <T> T getFixture(Class<T> tClass) throws IOException {
        return gsonFactory.getGson().fromJson(fixtureReader.getFixtureReader(), tClass);
    }
}
