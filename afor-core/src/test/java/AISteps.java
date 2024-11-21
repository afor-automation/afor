import io.cucumber.java8.En;
import nz.co.afor.framework.ai.AiClientService;
import nz.co.afor.framework.ai.AiContactRequest;
import nz.co.afor.framework.model.Selector;
import org.springframework.beans.factory.annotation.Autowired;

import static nz.co.afor.ai.CoreAi.ai;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AISteps implements En {

    private AiContactRequest aiResponse;
    @Autowired
    private AiClientService aiClientService;

    private Selector selector;

    public AISteps() {
        When("I perform an AI request", () -> selector = aiClientService.chatClientExample());
        Then("the AI response should be successful", () -> {
            assertThat(selector.getSelector(), not(nullValue()));
            assertThat(selector.getType(), equalToIgnoringCase("css"));
        });
        When("I run the AI query {string}", (String query) -> {
            aiResponse = ai(query, new AiContactRequest());
        });
        Then("the AI response should contain populated email addresses and phone numbers", () -> {
            assertThat("Email addresses should be populated", aiResponse.getEmailAddress(), not(nullValue()));
            assertThat("Email addresses should be populated", aiResponse.getEmailAddress(), containsString("@"));
            assertThat("Phone numbers should be populated", aiResponse.getPhoneNumber(), not(nullValue()));
            assertThat("Phone numbers should be populated", aiResponse.getMobileNumber(), not(nullValue()));
        });
        And("the AI response should not contain any addresses", () ->
                assertThat("Phone numbers should be populated", aiResponse.getPostalAddress(), is(nullValue())));
    }
}
