package nz.co.afor.framework.minify;

import in.wilsonl.minifyhtml.Configuration;

/**
 * Minifies html strings using MinifyHtml, note that this may not retain element
 */
public class MinifyHtml implements Minify {

    private final Configuration configuration;

    public MinifyHtml() {
        this.configuration = new Configuration.Builder().build();
    }

    public MinifyHtml(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public String minify(String htmlSource) {
        return in.wilsonl.minifyhtml.MinifyHtml.minify(htmlSource, configuration);
    }
}
