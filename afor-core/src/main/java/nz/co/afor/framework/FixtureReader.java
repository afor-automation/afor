package nz.co.afor.framework;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Copyright afor
 * Created by Matt Belcher on 12/10/2015.
 */
@Component
public class FixtureReader {

    @Value("${nz.co.afor.fixture.path}")
    String fixturePath;

    public BufferedReader getFixtureReader() throws FileNotFoundException {
        return new BufferedReader(new FileReader(fixturePath));
    }
}
