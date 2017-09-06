package nz.co.afor.framework.web;

/**
 * Created by belcherm on 23/05/2017.
 */
public abstract class By extends org.openqa.selenium.By {
    public static ByLabelText exactLabelText(final String labelText) {
        if (labelText == null) {
            throw new IllegalArgumentException("Cannot find elements with a null label attribute.");
        } else {
            return new ByLabelText(labelText, true);
        }
    }

    public static ByLabelText labelText(final String labelText) {
        if (labelText == null) {
            throw new IllegalArgumentException("Cannot find elements with a null label attribute.");
        } else {
            return new ByLabelText(labelText, false);
        }
    }

    public static ByLabelAssociation exactLabelAssociation(final String labelText) {
        if (labelText == null) {
            throw new IllegalArgumentException("Cannot find elements with a null label attribute.");
        } else {
            return new ByLabelAssociation(labelText, true);
        }
    }

    public static ByLabelAssociation labelAssociation(final String labelText) {
        if (labelText == null) {
            throw new IllegalArgumentException("Cannot find elements with a null label attribute.");
        } else {
            return new ByLabelAssociation(labelText, false);
        }
    }
}
