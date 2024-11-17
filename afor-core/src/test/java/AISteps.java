import io.cucumber.java8.En;
import nz.co.afor.framework.ai.AiClientService;
import nz.co.afor.framework.model.Selector;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AISteps implements En {

    @Autowired
    private AiClientService aiClientService;

    private Selector selector;

    public AISteps() {
        When("I perform an AI request", () -> selector = aiClientService.chatClientExample());
        Then("the AI response should be successful", () -> {
            assertThat(selector.getSelector(), not(nullValue()));
            assertThat(selector.getType(), equalToIgnoringCase("css"));
        });
    }
}
