package nz.co.afor.framework.web;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.apache.commons.lang3.ArrayUtils;

import java.util.concurrent.Callable;

import static java.lang.Math.toIntExact;
import static nz.co.afor.framework.Retry.Options;
import static nz.co.afor.framework.Retry.until;

/**
 * Created by Matt on 6/03/2018.
 */
public class BrowserState {
    private static <T> T whenInState(Callable<T> block, String[] states) {
        try {
            until(() -> ArrayUtils.contains(states, Selenide.executeJavaScript("return document.readyState")), Options.getInstance().withTimeout(toIntExact(Configuration.timeout)));
            return block.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void whenInState(Runnable block, String[] states) {
        try {
            until(() -> ArrayUtils.contains(states, Selenide.executeJavaScript("return document.readyState")), Options.getInstance().withTimeout(toIntExact(Configuration.timeout)));
            block.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T whenLoaded(Callable<T> block) {
        return whenInState(block, new String[]{"complete"});
    }

    public static void whenLoaded(Runnable block) {
        whenInState(block, new String[]{"complete"});
    }

    public static <T> T whenInteractive(Callable<T> block) {
        return whenInState(block, new String[]{"complete", "interactive"});
    }

    public static void whenInteractive(Runnable block) {
        whenInState(block, new String[]{"complete", "interactive"});
    }
}
