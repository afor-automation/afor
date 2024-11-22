package nz.co.afor.ai;

import nz.co.afor.ai.model.AiResponse;

public class CoreAiResponse<T> extends AiResponse {
    private T object;

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }
}
