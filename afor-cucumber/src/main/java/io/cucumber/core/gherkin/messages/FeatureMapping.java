package io.cucumber.core.gherkin.messages;

import io.cucumber.messages.Messages;
import io.cucumber.plugin.event.Node;
import io.cucumber.plugin.event.Status;

import java.lang.reflect.Field;

public final class FeatureMapping {
    private final String keyword;
    private final String name;
    private final String description;
    private Status featureStatus;
    private boolean written = false;
    private int hash;

    public FeatureMapping(Node node) {
        this.keyword = node.getKeyword().isPresent() ? node.getKeyword().get() : "Feature";
        this.name = node.getName().isPresent() ? node.getName().get() : "(unknown feature)";
        try {
            Field featureField = GherkinMessagesFeature.class.getDeclaredField("feature");
            featureField.setAccessible(true);
            this.description = ((Messages.GherkinDocument.Feature) (featureField.get(node))).getDescription();
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new FeatureMappingException(e);
        }
    }

    public String getKeyword() {
        return keyword;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isWritten() {
        return written;
    }

    public void setWritten(boolean written) {
        this.written = written;
    }

    public Status getFeatureStatus() {
        return featureStatus;
    }

    public void setFeatureStatus(Status featureStatus) {
        if (null == this.featureStatus || this.featureStatus == Status.PASSED) this.featureStatus = featureStatus;
    }

    public void setHash(int hash) {
        this.hash = hash;
    }

    public int getHash() {
        return hash;
    }

    public static final class FeatureMappingException extends RuntimeException {
        public FeatureMappingException(String message) {
            super(message);
        }

        public FeatureMappingException(String message, Throwable cause) {
            super(message, cause);
        }

        public FeatureMappingException(Throwable cause) {
            super(cause);
        }
    }
}
