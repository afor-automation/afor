package nz.co.afor.framework.api.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by Matt on 6/09/2017.
 */
public abstract class AbstractApi {
    private final Log log = LogFactory.getLog(this.getClass());

    @Autowired
    ObjectMapper objectMapper;

    public <T> T readEntity(Response response, Class<T> tClass) throws IOException {
        final String body = response.readEntity(String.class);
        log.info("Received an HTTP " + response.getStatus() + " response with the body: " + ((null == body || body.isEmpty()) ? "(empty body)" : body));
        return objectMapper.readValue(body, tClass);
    }

    public List<String> validate(Object o) {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<String> errors = Sets.newHashSet();
        Set<ConstraintViolation<Object>> violations = validator.validate(o);
        for (ConstraintViolation<Object> v : violations) {
            errors.add(String.format("%s %s (was %s)",
                    v.getPropertyPath(), v.getMessage(), v.getInvalidValue()));
        }
        return ImmutableList.copyOf(Ordering.natural().sortedCopy(errors));
    }

    public void assertValidate(Object object) {
        List<String> validationErrors = validate(object);
        assertThat(String.format("There should be no validation errors, however the errors '%s' were found", validationErrors), validationErrors.size(), is(0));
    }
}
