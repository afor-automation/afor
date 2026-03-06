package nz.co.afor.framework;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static nz.co.afor.framework.ApplicationContext.getContext;

@Component
public class Browser {
    public static final ThreadLocal<Playwright> playwright = new ThreadLocal<>();
    public static final ThreadLocal<com.microsoft.playwright.Browser> browser = new ThreadLocal<>();
    public static final ThreadLocal<BrowserContext> context = new ThreadLocal<>();
    public static final ThreadLocal<Page> page = new ThreadLocal<>();

    @Value("${playwright.browser:chromium}")
    private String browserType;

    /**
     * Initialises everything required to run playwright
     */
    public static void init() {
        if (null == playwright.get())
            playwright.set(Playwright.create());
        if (null == browser.get()) {
            String browserType = getContext().getBean(Browser.class).browserType;
            switch (browserType.toLowerCase()) {
                case "firefox": {
                    browser.set(playwright.get().firefox().launch());
                }
                case "webkit": {
                    browser.set(playwright.get().webkit().launch());
                }
                default: {
                    browser.set(playwright.get().chromium().launch());
                }
            }
        }
        if (null == context.get())
            context.set(browser.get().newContext());
        if (null == page.get())
            page.set(context.get().newPage());
    }

    public static void newContext() {
        if (null == context.get()) {
            init();
        } else {
            if (null == browser.get())
                init();
            for (Page page : context.get().pages()) {
                if (!page.isClosed())
                    page.close();
            }
            context.get().close();
            context.set(browser.get().newContext());
        }
    }

    public static void newPage() {
        if (null == context.get())
            init();
        if (null != page.get()) {
            if (!page.get().isClosed())
                page.get().close();
        }
        page.set(context.get().newPage());
    }
}
