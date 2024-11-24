package nz.co.afor.framework.ai;

import com.fasterxml.jackson.core.JsonProcessingException;
import nz.co.afor.ai.AiClient;
import nz.co.afor.framework.model.Selector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Component
public class AiClientService {

    @Autowired
    private AiClient aiClient;

    private static final String FILE_CONTENT = """
            <!DOCTYPE html>
            <html lang="en">
            <body>
                <h1 class="display-5 fw-bolder text-white mb-2">Afor Automation
                </h1>
            </body>
            </html>
            """;

    private String getChatMessage() {
        return format("""
                This is your task: %s
                
                * Create a unique CSS selector, ensure the selector is specific enough to select only one element, avoiding generic tags (e.g., 'h1') alone
                * If there is no valid CSS selector, return an XPATH selector instead
                * Combine attributes or structural relationships to form a unique selector, prefer matching on text
                * If no unique selector can be created, generate a basic selector without structural relationships based on the most likely matching selector
                * Provide the answer in JSON format as follows: {"selector": "value1","type:" "value2"}
                
                ```
                %s
                ```""", "find the Afor title", FILE_CONTENT);
    }

    public Selector chatClientExample() throws JsonProcessingException {
        return aiClient.request(getChatMessage(), Selector.class);
    }
}
