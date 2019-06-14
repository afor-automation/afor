package nz.co.afor.framework.web;

import com.codeborne.selenide.Driver;
import com.codeborne.selenide.impl.Html;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.regex.Pattern;

/**
 * Created by Matt on 10/02/2016.
 */
public abstract class Condition extends com.codeborne.selenide.Condition {

    private final String name;

    public Condition(String name) {
        super(name);
        this.name = name;

    }

    public Condition(String name, boolean absentElementMatchesCondition) {
        super(name, absentElementMatchesCondition);
        this.name= name;
    }

    /**
     * <p>Sample: <code>$("input").shouldHave(css("div.active"));</code></p>
     */
    public static Condition css(final String css) {
        return new Condition("css") {
            @Override
            public boolean apply(Driver driver, WebElement element) {
                WebElement cssElement;
                try {
                    cssElement = element.findElement(By.cssSelector(css));
                } catch (NoSuchElementException e) {
                    return false;
                }
                return null != cssElement;
            }

            @Override
            public String toString() {
                return " '" + css + '\'';
            }
        };
    }

    public static com.codeborne.selenide.Condition cssWithText(final String css, final String text) {
        return new com.codeborne.selenide.Condition("css textCaseInsensitive") {
            @Override
            public boolean apply(Driver driver, WebElement element) {
                WebElement cssElement;
                try {
                    cssElement = element.findElement(By.cssSelector(css));
                } catch (NoSuchElementException e) {
                    return false;
                }
                return Html.text.contains(cssElement.getText(), text);
            }

            @Override
            public String toString() {
                return String.format("'%s' '%s'", css, text);
            }
        };
    }

    public static com.codeborne.selenide.Condition cssWithTextCaseSensitive(final String css, final String text) {
        return new com.codeborne.selenide.Condition("css textCaseSensitive") {
            @Override
            public boolean apply(Driver driver, WebElement element) {
                WebElement cssElement;
                try {
                    cssElement = element.findElement(By.cssSelector(css));
                } catch (NoSuchElementException e) {
                    return false;
                }
                return Html.text.containsCaseSensitive(cssElement.getText(), text);
            }

            @Override
            public String toString() {
                return String.format("'%s' '%s'", css, text);
            }
        };
    }

    public static com.codeborne.selenide.Condition cssWithExactText(final String css, final String text) {
        return new com.codeborne.selenide.Condition("css exact text") {
            @Override
            public boolean apply(Driver driver, WebElement element) {
                WebElement cssElement;
                try {
                    cssElement = element.findElement(By.cssSelector(css));
                } catch (NoSuchElementException e) {
                    return false;
                }
                return Html.text.equals(cssElement.getText(), text);
            }

            @Override
            public String toString() {
                return String.format("'%s' '%s'", css, text);
            }
        };
    }

    public static com.codeborne.selenide.Condition cssWithExactTextCaseSensitive(final String css, final String text) {
        return new com.codeborne.selenide.Condition("css exact text case sensitive") {
            @Override
            public boolean apply(Driver driver, WebElement element) {
                WebElement cssElement;
                try {
                    cssElement = element.findElement(By.cssSelector(css));
                } catch (NoSuchElementException e) {
                    return false;
                }
                return Html.text.equalsCaseSensitive(cssElement.getText(), text);
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
            public boolean apply(Driver driver, WebElement element) {
                WebElement cssElement;
                try {
                    cssElement = element.findElement(By.cssSelector(css));
                } catch (NoSuchElementException e) {
                    return false;
                }
                Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE + Pattern.DOTALL);
                return null != cssElement && pattern.matcher(cssElement.getText()).matches();
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
            public boolean apply(Driver driver, WebElement element) {
                return Html.text.contains(getAttributeValueAsNumber(element, "value"), expectedValue);
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
