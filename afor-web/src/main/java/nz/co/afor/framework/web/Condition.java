package nz.co.afor.framework.web;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementCondition;
import com.codeborne.selenide.impl.Html;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.regex.Pattern;

/**
 * Created by Matt on 10/02/2016.
 */
public abstract class Condition extends com.codeborne.selenide.WebElementCondition {

    private final String name;

    public Condition(String name) {
        super(name);
        this.name = name;

    }

    public Condition(String name, boolean absentElementMatchesCondition) {
        super(name, absentElementMatchesCondition);
        this.name = name;
    }

    /**
     * <p>Sample: <code>$("input").shouldHave(css("div.active"));</code></p>
     */
    public static WebElementCondition css(final String css) {

        return new Condition("css") {
            @Override
            public CheckResult check(Driver driver, WebElement element) {
                WebElement cssElement;
                try {
                    cssElement = element.findElement(By.cssSelector(css));
                    return new CheckResult(cssElement.isDisplayed(), css);
                } catch (NoSuchElementException e) {
                    return new CheckResult(false, e);
                }
            }

            @Override
            public String toString() {
                return " '" + css + '\'';
            }
        };
    }

    public static WebElementCondition cssWithText(final String css, final String text) {
        return new Condition("css textCaseInsensitive") {
            @Override
            public CheckResult check(Driver driver, WebElement element) {
                WebElement cssElement;
                try {
                    cssElement = element.findElement(By.cssSelector(css));
                } catch (NoSuchElementException e) {
                    return new CheckResult(false, e);
                }
                String value = cssElement.getText();
                return new CheckResult(Html.text.contains(value, text), value);
            }

            @Override
            public String toString() {
                return String.format("'%s' '%s'", css, text);
            }
        };
    }

    public static WebElementCondition cssWithTextCaseSensitive(final String css, final String text) {
        return new Condition("css textCaseSensitive") {
            @Override
            public CheckResult check(Driver driver, WebElement element) {
                WebElement cssElement;
                try {
                    cssElement = element.findElement(By.cssSelector(css));
                } catch (NoSuchElementException e) {
                    return new CheckResult(false, e);
                }
                String value = cssElement.getText();
                return new CheckResult(Html.text.containsCaseSensitive(value, text), value);
            }

            @Override
            public String toString() {
                return String.format("'%s' '%s'", css, text);
            }
        };
    }

    public static WebElementCondition cssWithExactText(final String css, final String text) {
        return new Condition("css exact text") {
            @Override
            public CheckResult check(Driver driver, WebElement element) {
                WebElement cssElement;
                try {
                    cssElement = element.findElement(By.cssSelector(css));
                } catch (NoSuchElementException e) {
                    return new CheckResult(false, e);
                }
                String value = cssElement.getText();
                return new CheckResult(Html.text.equals(value, text), value);
            }

            @Override
            public String toString() {
                return String.format("'%s' '%s'", css, text);
            }
        };
    }

    public static WebElementCondition cssWithExactTextCaseSensitive(final String css, final String text) {
        return new Condition("css exact text case sensitive") {
            @Override
            public CheckResult check(Driver driver, WebElement element) {
                WebElement cssElement;
                try {
                    cssElement = element.findElement(By.cssSelector(css));
                } catch (NoSuchElementException e) {
                    return new CheckResult(false, e);
                }
                String value = cssElement.getText();
                return new CheckResult(Html.text.equalsCaseSensitive(value, text), value);
            }

            @Override
            public String toString() {
                return String.format("'%s' '%s'", css, text);
            }
        };
    }

    /**
     * <p>Sample: <code>$("input").shouldHave(cssAndMatches("active", "Attempt [0-9] of [0-9]"));</code></p>
     */
    public static Condition cssAndMatches(final String css, final String regex) {
        return new Condition("css") {
            @Override
            public CheckResult check(Driver driver, WebElement element) {
                WebElement cssElement;
                try {
                    cssElement = element.findElement(By.cssSelector(css));
                } catch (NoSuchElementException e) {
                    return new CheckResult(false, e);
                }
                Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE + Pattern.DOTALL);
                return new CheckResult(null != cssElement && pattern.matcher(cssElement.getText()).matches(), null != cssElement ? cssElement.getText() : "");
            }

            @Override
            public String toString() {
                return String.format("'%s' '%s'", css, regex);
            }
        };
    }

    /**
     * Assert that element has given "value" attribute as substring
     * NB! Ignores difference in non-visible characters like spaces, non-breakable spaces, tabs, newlines  etc.
     * <p>
     * <p>Sample: <code>$("input").shouldHave(valueAsNumber("12345 666 77"));</code></p>
     *
     * @param expectedValue expected value of "value" attribute
     */
    public static Condition valueAsNumber(final String expectedValue) {
        return new Condition("value") {
            @Override
            public CheckResult check(Driver driver, WebElement element) {
                String value = getAttributeValueAsNumber(element, "value");
                return new CheckResult(Html.text.contains(value, expectedValue), value);
            }

            @Override
            public String toString() {
                return " '" + expectedValue + "'";
            }
        };
    }

    private static String getAttributeValueAsNumber(WebElement element, String attributeName) {
        String attr = element.getAttribute(attributeName);
        return attr == null ? "" : attr.trim().replace(",", "").replace(" ", "");
    }
}
