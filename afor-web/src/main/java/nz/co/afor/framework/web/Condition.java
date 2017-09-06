package nz.co.afor.framework.web;

import com.codeborne.selenide.impl.Html;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.regex.Pattern;

/**
 * Created by Matt on 10/02/2016.
 */
public abstract class Condition extends com.codeborne.selenide.Condition {
    public Condition(String name) {
        super(name);
    }

    public Condition(String name, boolean absentElementMatchesCondition) {
        super(name, absentElementMatchesCondition);
    }

    public boolean test(WebElement element) {
        return apply(element);
    }

    /**
     * <p>Sample: <code>$("input").waitUntil(hasClass("blocked"), 7000);</code></p>
     */
    public static Condition hasCss(String css) {
        return css(css);
    }

    /**
     * <p>Sample: <code>$("input").shouldHave(cssClass("active"));</code></p>
     */
    public static Condition css(final String css) {
        return new Condition("css") {
            @Override
            public boolean apply(WebElement element) {
                return hasCss(element, css);
            }

            @Override
            public String toString() {
                return name + " '" + css + '\'';
            }
        };
    }

    /**
     * <p>Sample: <code>$("input").hasCss("a.blocked");</code></p>
     */
    public static boolean hasCss(WebElement element, String css) {
        WebElement cssElement;
        try {
            cssElement = element.findElement(By.cssSelector(css));
        } catch (NoSuchElementException e) {
            return false;
        }
        return null != cssElement;
    }

    /**
     * <p>Sample: <code>$("input").waitUntil(hasClass("blocked"), 7000);</code></p>
     */
    public static Condition hasCssAndMatches(String css, String regex) {
        return css(css, regex);
    }

    /**
     * <p>Sample: <code>$("input").shouldHave(cssClass("active"));</code></p>
     */
    public static Condition css(final String css, final String regex) {
        return new Condition("css") {
            @Override
            public boolean apply(WebElement element) {
                return hasCss(element, css, regex);
            }

            @Override
            public String toString() {
                return String.format("%s '%s' '%s'", name, css, regex);
            }
        };
    }

    /**
     * <p>Sample: <code>$("div.form").hasCss($("div.name"),"span", "Attempt [0-9] of [0-9]");</code></p>
     */
    public static boolean hasCss(WebElement element, String css, String regex) {
        WebElement cssElement;
        try {
            cssElement = element.findElement(By.cssSelector(css));
        } catch (NoSuchElementException e) {
            return false;
        }
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE + Pattern.DOTALL);
        return null != cssElement && pattern.matcher(cssElement.getText()).matches();
    }

    /**
     * Assert that element has given "value" attribute as substring
     * NB! Ignores difference in non-visible characters like spaces, non-breakable spaces, tabs, newlines  etc.
     * <p>
     * <p>Sample: <code>$("input").shouldHave(value("12345 666 77"));</code></p>
     *
     * @param expectedValue expected value of "value" attribute
     */
    public static Condition valueAsNumber(final String expectedValue) {
        return new Condition("value") {
            @Override
            public boolean apply(WebElement element) {
                return Html.text.contains(getAttributeValueAsNumber(element, "value"), expectedValue);
            }

            @Override
            public String toString() {
                return name + " '" + expectedValue + "'";
            }
        };
    }

    private static String getAttributeValueAsNumber(WebElement element, String attributeName) {
        String attr = element.getAttribute(attributeName);
        return attr == null ? "" : attr.trim().replace(",", "");
    }
}
