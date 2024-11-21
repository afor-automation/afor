package nz.co.afor.ai.model;

public abstract class AiResponse {
    private int promptTokens;
    private int completionTokens;
    private int totalTokens;
    private String prompt;

    public int getPromptTokens() {
        return promptTokens;
    }

    public void setPromptTokens(int promptTokens) {
        this.promptTokens = promptTokens;
    }

    public int getCompletionTokens() {
        return completionTokens;
    }

    public void setCompletionTokens(int completionTokens) {
        this.completionTokens = completionTokens;
    }

    public int getTotalTokens() {
        return totalTokens;
    }

    public void setTotalTokens(int totalTokens) {
        this.totalTokens = totalTokens;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}
