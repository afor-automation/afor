package nz.co.afor.framework.web.ai;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.CacheLoader;
import nz.co.afor.ai.AiClient;
import nz.co.afor.ai.CoreAi;
import nz.co.afor.framework.minify.Minify;
import nz.co.afor.framework.minify.RegexMinifyHtml;
import org.openqa.selenium.NotFoundException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URI;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import static com.codeborne.selenide.Selenide.*;
import static java.lang.String.format;

@Component
public class BrowserAi {

    private static ApplicationContext applicationContext;
    private static Properties persistentProperties;

    @Value("${nz.co.afor.web.ai.cache.location:.aiwebcache}")
    private String cacheLocation;

    @Value("${nz.co.afor.web.ai.cache.revalidate:true}")
    private Boolean revalidateCache;

    private static Minify minify = new RegexMinifyHtml();

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        BrowserAi.applicationContext = applicationContext;
    }

    private static final CacheLoader<AiCache, Selector> cache = new CacheLoader<>() {
        @Override
        public Selector load(AiCache query) throws IOException {
            BrowserAi browserAi = BrowserAi.applicationContext.getBean(BrowserAi.class);
            File fileCache = new File(browserAi.cacheLocation);
            ObjectMapper objectMapper = new ObjectMapper();
            // Initialise the file cache
            if (null == persistentProperties) {
                persistentProperties = new Properties();
                if (fileCache.exists()) {
                    persistentProperties.load(new BufferedReader(new FileReader(fileCache)));
                }
            }
            // Check the file cache first
            for (Map.Entry<Object, Object> objectObjectEntry : persistentProperties.entrySet()) {
                String[] entry = String.valueOf(objectObjectEntry.getKey()).split("\\|");
                if (entry.length == 2) {
                    AiCache compareCache = new AiCache(entry[1], URI.create(entry[0]), null);
                    if (Objects.equals(compareCache, query)) {
                        Selector selector = objectMapper.readValue((String) objectObjectEntry.getValue(), Selector.class);
                        if (!browserAi.revalidateCache || getSelenideElement(selector).exists())
                            return selector;
                    }
                }
            }

            // Cache miss, call the AI service and cache to file
            Selector aiResponse = BrowserAi.applicationContext.getBean(AiClient.class).request(getChatMessage(query.getQuery(), query.getHtmlSource()), Selector.class);
            persistentProperties.put(query.getKey(), objectMapper.writeValueAsString(aiResponse));
            persistentProperties.store(new FileOutputStream(fileCache), null);
            return aiResponse;
        }
    };

    /**
     * Use AI to manipulate an object, such as generating field data
     *
     * @param query The query to run, such as 'Generate realistic email addresses for email fields'
     * @return Returns the object after generation
     */
    public static <T> T ai(String query, T object) {
        return CoreAi.ai(query, object);
    }

    /**
     * Find a browser element using an AI query against the html
     *
     * @param query The query to run, such as 'Find the button with the text Submit'
     * @return Returns the element based on the requested query
     */
    public static SelenideElement ai(String query) {
        try {
            Selector response = cache.load(new AiCache(query, URI.create(WebDriverRunner.currentFrameUrl()), "<html><body>" + executeJavaScript("return document.querySelector('body').getHTML()") + "</body></html>"));
            return getSelenideElement(response);
        } catch (Exception e) {
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
            Selector response = cache.load(new AiCache(query, URI.create(WebDriverRunner.currentFrameUrl()), "<html><body>" + executeJavaScript("return arguments[0].getHTML()", parent) + "</body></html>"));
            return getSelenideElement(response);
        } catch (Exception e) {
            throw new NotFoundException(e);
        }
    }

    private static SelenideElement getSelenideElement(Selector response) {
        if ("xpath".equalsIgnoreCase(response.getType())) {
            return $x(response.getSelector());
        }
        return $(response.getSelector());
    }

    /**
     * Sets the class used to minify the AI request
     *
     * @param minify The minify class used to minify requests
     */
    public static void setMinify(Minify minify) {
        BrowserAi.minify = minify;
    }

    private static String getChatMessage(String chatMessage, String htmlSource) {
        return format("""
                This is your task: %s
                
                * First, create a unique XPath selector. The selector should be as specific as possible, combining attributes or structural relationships to uniquely identify the element. It’s important to avoid generic tags like h1 unless they are uniquely qualified by classes, IDs, or other attributes.
                * When using text matching, use contains for partial matching
                * Stop once a valid selector is found. Do not attempt to create multiple options or rectifications. Once a selector is determined to be valid, return it in the requested JSON format without modifications.
                * Provide the answer in JSON format as follows: {"selector": "unique-selector","type": "XPATH"}
                
                ```
                %s
                ```""", chatMessage, null != minify ? minify.minify(htmlSource) : htmlSource);
    }
}
