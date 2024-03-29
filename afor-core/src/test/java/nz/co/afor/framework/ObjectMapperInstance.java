package nz.co.afor.framework;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObjectMapperInstance {

    @Autowired
    Fixture fixture;

    public ObjectMapper getObjectMapper() {
        return fixture.getObjectMapper();
    }
}
