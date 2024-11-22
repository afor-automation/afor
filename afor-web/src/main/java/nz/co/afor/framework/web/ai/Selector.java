package nz.co.afor.framework.web.ai;

import nz.co.afor.ai.model.AiResponse;

public class Selector extends AiResponse {
    private String selector;
    private String type;

    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
