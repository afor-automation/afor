package nz.co.afor.framework.web.ai;

import com.google.common.net.InternetDomainName;

import java.net.URI;
import java.util.Objects;

public class AiCache {
    private String query;
    private URI uri;
    private String htmlSource;

    public AiCache(String query, URI uri, String htmlSource) {
        this.query = query;
        this.uri = uri;
        this.htmlSource = htmlSource;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getHtmlSource() {
        return htmlSource;
    }

    public void setHtmlSource(String htmlSource) {
        this.htmlSource = htmlSource;
    }

    public String getKey() {
        return uri.toASCIIString() + "|" + getQuery();
    }

    public int hashCode() {
        return query.hashCode() + uri.hashCode();
    }

    public boolean equals(Object o) {
        if (!o.getClass().isAssignableFrom(AiCache.class))
            return false;
        AiCache compare = (AiCache) o;
        return Objects.equals(query, compare.query)
                && isSameDomain(compare.uri)
                && Objects.equals(uri.getPath(), compare.uri.getPath())
                && Objects.equals(uri.getPort(), compare.uri.getPort())
                && Objects.equals(uri.getScheme(), compare.uri.getScheme());
    }

    private boolean isSameDomain(URI compare) {
        InternetDomainName uriHost = InternetDomainName.from(uri.getHost());
        InternetDomainName compareHost = InternetDomainName.from(compare.getHost());
        if (uriHost.isTopDomainUnderRegistrySuffix() && compareHost.isTopDomainUnderRegistrySuffix()) {
            return Objects.equals(uriHost.topDomainUnderRegistrySuffix(), compareHost.topDomainUnderRegistrySuffix());
        }
        if (uriHost.isTopPrivateDomain() && compareHost.isTopPrivateDomain()) {
            return Objects.equals(uriHost.topPrivateDomain(), compareHost.topPrivateDomain());
        }
        return Objects.equals(uri.getHost(), compare.getHost());
    }
}
