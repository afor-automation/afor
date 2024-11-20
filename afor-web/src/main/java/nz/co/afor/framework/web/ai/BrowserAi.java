package nz.co.afor.framework.web.ai;

import com.codeborne.selenide.SelenideElement;
import com.fasterxml.jackson.core.JsonProcessingException;
import in.wilsonl.minifyhtml.Configuration;
import in.wilsonl.minifyhtml.MinifyHtml;
import nz.co.afor.ai.AiClient;
import org.openqa.selenium.NotFoundException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static java.lang.String.format;

@Component
public class BrowserAi {

    private static ApplicationContext applicationContext;

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        BrowserAi.applicationContext = applicationContext;
    }

    /**
     * Find a browser element using an AI query against the html
     *
     * @param query The query to run, such as 'Find the button with the text Submit'
     * @return Returns the element based on the requested query
     */
    public static SelenideElement ai(String query) {
        try {
            Selector response = BrowserAi.applicationContext.getBean(AiClient.class).request(getChatMessage(query, "<html><body>" + $("body").innerHtml() + "</body></html>"), Selector.class);
            if ("xpath".equalsIgnoreCase(response.getType())) {
                return $x(response.getSelector());
            }
            return $(response.getSelector());
        } catch (JsonProcessingException e) {
            throw new NotFoundException(e);
        }
    }

    /**
     * Find a browser element using an AI query against the html of a parent element
     *
     * @param parent The parent element to search under
     * @param query  The query to run, such as 'Find the button with the text Submit'
     * @return Returns the element based on the requested query
     */
    public static SelenideElement ai(SelenideElement parent, String query) {
        try {
            Selector response = BrowserAi.applicationContext.getBean(AiClient.class).request(getChatMessage(query, "<html><body>" + parent.innerHtml() + "</body></html>"), Selector.class);
            if ("xpath".equalsIgnoreCase(response.getType())) {
                return parent.$x("." + response.getSelector());
            }
            return parent.$(response.getSelector());
        } catch (JsonProcessingException e) {
            throw new NotFoundException(e);
        }
    }

    private static String getChatMessage(String chatMessage, String htmlSource) {
        return format("""
                This is your task: %s

                * First, create a unique XPath selector. The selector should be as specific as possible, combining attributes or structural relationships to uniquely identify the element. It’s important to avoid generic tags like h1 unless they are uniquely qualified by classes, IDs, or other attributes.
                * Stop once a valid selector is found. Do not attempt to create multiple options or rectifications. Once a selector is determined to be valid, return it in the requested JSON format without modifications.
                * Provide the answer in JSON format as follows: {"selector": "unique-selector","type": "XPATH"}

                ```
                %s
                ```""", chatMessage, MinifyHtml.minify(htmlSource, new Configuration.Builder().build()));
    }
}
