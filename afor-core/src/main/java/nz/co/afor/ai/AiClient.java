package nz.co.afor.ai;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.assistants.AssistantsClient;
import com.azure.ai.openai.assistants.AssistantsClientBuilder;
import com.azure.ai.openai.models.*;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.credential.KeyCredential;
import com.azure.core.http.ProxyOptions;
import com.azure.core.http.policy.FixedDelayOptions;
import com.azure.core.http.policy.RetryOptions;
import com.azure.core.util.HttpClientOptions;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Splitter;
import nz.co.afor.ai.model.AiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.net.URI;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Scope("thread")
@Component
public class AiClient {

    private static final Logger log = LoggerFactory.getLogger(AiClient.class);

    @Value("${proxy.username:@null}")
    private String proxyUsername;

    @Value("${proxy.password:@null}")
    private String proxyPassword;

    @Value("${proxy.address:}")
    private URI proxyAddress;

    @Value("${nz.co.afor.ai.key:${AI_KEY:}}")
    private String key;

    @Value("${nz.co.afor.ai.endpoint:${AI_ENDPOINT:}}")
    private String endpoint;

    @Value("${nz.co.afor.ai.openapisecretkey:${AI_SECRET_KEY:}}")
    private String openApiSecretKey;

    @Value("${nz.co.afor.ai.model:gpt-4o}")
    private String deploymentOrModelId;

    @Value("${nz.co.afor.ai.request.chunksize:40000}")
    private Integer chunkSize;

    @Value("${nz.co.afor.ai.request.maxsize:200000}")
    private Integer maxSize;

    private OpenAIClient openAIClient;
    private AssistantsClient assistantsClient;
    private static int completionTokens = 0;
    private static int promptTokens = 0;
    private static int totalTokens = 0;

    /**
     * Create an OpenAI chat client
     *
     * @return An OpenAIClient
     */
    public OpenAIClient getChatClient() {
        if (null == openAIClient) {
            OpenAIClientBuilder clientBuilder;
            if (null != openApiSecretKey && !openApiSecretKey.isEmpty()) {
                clientBuilder = new OpenAIClientBuilder()
                        .credential(new KeyCredential(openApiSecretKey));
            } else if (null != key && !key.isEmpty() && null != endpoint && !endpoint.isEmpty()) {
                clientBuilder = new OpenAIClientBuilder()
                        .credential(new AzureKeyCredential(key))
                        .endpoint(endpoint);
            } else {
                throw new IllegalArgumentException("AI features require setting an OpenAPI secret key and endpoint for Azure clients, or an OpenAPI secret for OpenAI clients");
            }
            if (null != proxyAddress && null != proxyUsername) {
                ProxyOptions proxyOptions = new ProxyOptions(ProxyOptions.Type.HTTP, new InetSocketAddress(proxyAddress.getHost(), proxyAddress.getPort()))
                        .setCredentials(proxyUsername, proxyPassword);
                clientBuilder.clientOptions(new HttpClientOptions().setProxyOptions(proxyOptions));
            }
            clientBuilder.retryOptions(new RetryOptions(new FixedDelayOptions(3, Duration.of(5, ChronoUnit.SECONDS))));
            openAIClient = clientBuilder.buildClient();
        }
        return openAIClient;
    }

    /**
     * Create an OpenAI Assistants client
     *
     * @return An OpenAI AssistantsClient
     */
    public AssistantsClient getAssistantClient() {
        if (null == assistantsClient) {
            AssistantsClientBuilder assistantsClientBuilder;
            if (null != openApiSecretKey && !openApiSecretKey.isEmpty()) {
                assistantsClientBuilder = new AssistantsClientBuilder()
                        .credential(new KeyCredential(openApiSecretKey));
            } else if (null != key && !key.isEmpty() && null != endpoint && !endpoint.isEmpty()) {
                assistantsClientBuilder = new AssistantsClientBuilder()
                        .credential(new AzureKeyCredential(key))
                        .endpoint(endpoint);
            } else {
                throw new IllegalArgumentException("AI features require setting an OpenAPI secret key and endpoint for Azure clients, or an OpenAPI secret for OpenAI clients");
            }
            if (null != proxyAddress && null != proxyUsername) {
                ProxyOptions proxyOptions = new ProxyOptions(ProxyOptions.Type.HTTP, new InetSocketAddress(proxyAddress.getHost(), proxyAddress.getPort()))
                        .setCredentials(proxyUsername, proxyPassword);
                assistantsClientBuilder.clientOptions(new HttpClientOptions().setProxyOptions(proxyOptions));
            }
            assistantsClientBuilder.retryOptions(new RetryOptions(new FixedDelayOptions(1, Duration.of(5, ChronoUnit.SECONDS))));
            assistantsClient = assistantsClientBuilder.buildClient();
        }
        return assistantsClient;
    }

    public <T extends AiResponse> T request(final String message, final Class<T> clazz) throws JsonProcessingException {
        ChatCompletions chatCompletions = getChatCompletions(message);
        return readResponse(chatCompletions, clazz);
    }

    public ChatCompletions getChatCompletions(final String message) {
        if (message.length() < chunkSize) {
            // Send the message through as a single message
            return getChatCompletions(List.of(new ChatRequestUserMessage(message)));
        }
        // Break the message up into chunks to send through, up until a max size
        List<ChatRequestMessage> messages = new ArrayList<>();
        String messageStart = "The length of this request is too large for one message, multiple messages will be sent in chunks, do not respond until all messages have been received, the final message will end with ====EOM====\n";
        String messageEnd = "\n====EOM====";
        final String preparedMessage = messageStart + message.substring(0, Math.min(message.length(), maxSize - messageStart.length() - messageEnd.length())) + messageEnd;
        for (String part : Splitter.fixedLength(chunkSize).splitToList(preparedMessage)) {
            messages.add(new ChatRequestUserMessage(part));
        }
        return getChatCompletions(messages);
    }

    public ChatCompletions getChatCompletions(List<ChatRequestMessage> messages) {
        ChatCompletionsOptions chatCompletionsOptions = new ChatCompletionsOptions(messages).setResponseFormat(new ChatCompletionsJsonResponseFormat());
        return getChatClient().getChatCompletions(deploymentOrModelId, chatCompletionsOptions);
    }

    public <T extends AiResponse> T readResponse(ChatCompletions chatCompletions, final Class<T> clazz) throws JsonProcessingException {
        T response = null;
        for (ChatChoice choice : chatCompletions.getChoices()) {
            ChatResponseMessage message = choice.getMessage();
            response = new ObjectMapper().readValue(message.getContent(), clazz);
        }
        if (null == response) {
            return null;
        }
        CompletionsUsage usage = chatCompletions.getUsage();
        response.setCompletionTokens(usage.getCompletionTokens());
        response.setPromptTokens(usage.getPromptTokens());
        response.setTotalTokens(usage.getTotalTokens());
        incrementTotalUsage(usage);
        return response;
    }

    private void incrementTotalUsage(CompletionsUsage usage) {
        AiClient.completionTokens += usage.getCompletionTokens();
        AiClient.promptTokens += usage.getPromptTokens();
        AiClient.totalTokens += usage.getTotalTokens();
    }

    public static int getCompletionTokens() {
        return completionTokens;
    }

    public static int getPromptTokens() {
        return promptTokens;
    }

    public static int getTotalTokens() {
        return totalTokens;
    }

    public static void logUsage() {
        log.info("AI Token Usage, prompt tokens {}, completion tokens {}, total tokens {}",
                AiClient.promptTokens,
                AiClient.completionTokens,
                AiClient.totalTokens);
    }
}
