package nz.co.afor.framework.web.ai;

import nz.co.afor.framework.minify.Minify;
import nz.co.afor.framework.minify.RegexMinifyHtml;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

/**
 * Minifies html using jsoup
 */
public class MinifyJsoupHtml implements Minify {
    @Override
    public String minify(String htmlSource) {
        try {
            org.jsoup.nodes.Document document = Jsoup.parse(htmlSource);

            for (Element element : document.select("style,head,script,meta,link")) {
                element.remove();
            }
            return document.outputSettings(document.outputSettings().prettyPrint(false)).html();
        } catch (Exception ignore) {
            try {
                return new RegexMinifyHtml().minify(htmlSource);
            } catch (Exception ignore2) {
                return htmlSource;
            }
        }
    }
}