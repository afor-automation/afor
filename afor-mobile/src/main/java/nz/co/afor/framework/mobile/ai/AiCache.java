package nz.co.afor.framework.mobile.ai;

import java.util.Objects;

public class AiCache {
    private String query;
    private String htmlSource;

    public AiCache(String query, String htmlSource) {
        this.query = query;
        this.htmlSource = htmlSource;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getHtmlSource() {
        return htmlSource;
    }

    public void setHtmlSource(String htmlSource) {
        this.htmlSource = htmlSource;
    }

    public String getKey() {
        return getQuery();
    }

    public int hashCode() {
        return query.hashCode();
    }

    public boolean equals(Object o) {
        if (!o.getClass().isInstance(AiCache.class))
            return false;
        AiCache compare = (AiCache) o;
        return Objects.equals(query, compare.query);
    }
}
