package nz.co.afor.stereotype.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.cucumber.java.Scenario;
import nz.co.afor.framework.EncryptAnnotationIntrospector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

public interface Model<T> extends List<T> {
    ObjectMapper objectMapper = new ObjectMapper()
            .enable(INDENT_OUTPUT)
            .registerModule(new JavaTimeModule())
            .configure(WRITE_DATES_AS_TIMESTAMPS, false)
            .setAnnotationIntrospector(new EncryptAnnotationIntrospector())
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    default T get() {
        if (this.size() < 1)
            return null;
        return this.get(this.size() - 1);
    }

    default void log(Scenario scenario) {
        if (this.size() > 0) {
            Logger log = LoggerFactory.getLogger(this.getClass());
            try {
                log.info("'{}' data model for the scenario '{}', {}", this.getClass().getSimpleName(), scenario.getName(), objectMapper.writeValueAsString(this));
            } catch (JsonProcessingException e) {
                log.warn("Failed to log the '{}' data model for the scenario '{}', {}", this.getClass().getSimpleName(), scenario.getName(), e);
            }
        }
    }

    /**
     * Allows implementing custom before hooks at a model level
     *
     * @param scenario The current cucumber scenario
     */
    default void before(Scenario scenario) {
    }

    /**
     * Allows implementing custom after hooks at a model level
     *
     * @param scenario The current cucumber scenario
     */
    default void after(Scenario scenario) {
    }
}
