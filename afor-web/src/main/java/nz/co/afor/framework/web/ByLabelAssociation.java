package nz.co.afor.framework.web;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Matt on 3/10/2016.
 */
public class ByLabelAssociation extends By implements Serializable {

    private final String labelText;
    private final String xpathExpression;
    private final boolean exact;

    public ByLabelAssociation(String labelText, boolean exact) {
        this.labelText = labelText;
        this.exact = exact;
        if (this.exact) {
            this.xpathExpression = ".//label[text()='" + labelText + "']//*[self::textarea or self::input or self::select]";
        } else {
            this.xpathExpression = ".//label[contains(text(),'" + labelText + "')]//*[self::textarea or self::input or self::select]";
        }
    }

    @Override
    public List<WebElement> findElements(SearchContext context) {
        return context.findElements(By.xpath(xpathExpression));
    }

    @Override
    public WebElement findElement(SearchContext context) {
        return context.findElement(By.xpath(xpathExpression));
    }


    @Override
    public String toString() {
        return "By.labelText: " + labelText;
    }
}
