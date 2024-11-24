package nz.co.afor.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Component
public class CoreAi {

    private static ApplicationContext applicationContext;

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        CoreAi.applicationContext = applicationContext;
    }

    /**
     * Use AI to manipulate an object, such as generating field data
     *
     * @param query The query to run, such as 'Generate realistic email addresses for email fields'
     * @return Returns the object after generation
     */
    public static <T> T ai(String query, T object) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CoreAiResponse<T> request = new CoreAiResponse<>();
            request.setObject(object);
            String objectAsJson = objectMapper.writeValueAsString(request);
            CoreAiResponse<T> response = applicationContext.getBean(AiClient.class).request(getChatMessage(query, objectAsJson), request.getClass());
            return (T) objectMapper.convertValue(response.getObject(), request.getObject().getClass());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String getChatMessage(String chatMessage, String json) {
        return format("""
                This is your task: %s
                
                * Using the json data, populate field values as instructed
                * If no fields are specified, then assume all fields should be populated
                * Provide the answer in JSON format as follows: {"object": {"key":"value"}}
                
                ```
                %s
                ```""", chatMessage, json);
    }
}
