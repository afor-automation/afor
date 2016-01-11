package nz.co.afor.framework;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Copyright afor
 * Created by Matt Belcher on 12/10/2015.
 */
@Component
public class Fixture {

    @Autowired
    GsonFactory gsonFactory;

    @Autowired
    FixtureReader fixtureReader;

    public <T> T getFixture(Type classOfT) throws IOException {
        return gsonFactory.getGson().fromJson(fixtureReader.getFixtureReader(), classOfT);
    }
}
