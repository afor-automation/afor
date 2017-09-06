package nz.co.afor.framework;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

/**
 * Created by Matt Belcher on 13/01/2017.
 */
@Component
public class DataModel {
    private static final Logger log = LoggerFactory.getLogger(DataModel.class);

    @Autowired(required = false)
    private ObjectMapper objectMapper;

    /**
     * Logs all list data in the data model, as json
     * <p>
     * Must be accessible with getters
     *
     * @param model The input data model to log
     * @throws JsonProcessingException   Throws on parsing exception
     * @throws InvocationTargetException Throws on reflection exception
     * @throws IllegalAccessException    Throws on reflection exception
     */
    @SuppressWarnings("unchecked")
    public void log(String description, Object model) throws JsonProcessingException, InvocationTargetException, IllegalAccessException {
        if (null == objectMapper)
            objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        for (Method method : model.getClass().getMethods()) {

            final String methodName = method.getName();
            final String modelName = methodName.replaceFirst("^get", "");
            if (methodName.matches("^get.*") && Collection.class.isAssignableFrom(method.getReturnType())) {

                // Log the model data
                final List<Object> modelData = (List<Object>) method.invoke(model);
                if (modelData.size() > 0)
                    log.info(String.format("%s, %s model: %s", description, modelName, objectMapper.writer().writeValueAsString(modelData)));
            }
        }
    }

    /**
     * Sets list types in a model to null
     * <p>
     * Must be accessible with setters
     *
     * @param model The input data model to update
     * @throws InvocationTargetException Throws on reflection exception
     * @throws IllegalAccessException    Throws on reflection exception
     */
    public void empty(Object model) throws InvocationTargetException, IllegalAccessException {
        for (Method method : model.getClass().getMethods()) {

            final String methodName = method.getName();
            final String modelName = methodName.replaceFirst("^set", "");
            if (methodName.matches("^set.*s") && method.getParameterCount() == 1 && Collection.class.isAssignableFrom(method.getParameterTypes()[0])) {

                // Empty out the data model
                method.invoke(model, (Object) null);
            }
        }
    }
}
