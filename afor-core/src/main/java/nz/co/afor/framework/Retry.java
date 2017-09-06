package nz.co.afor.framework;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;
import java.util.concurrent.Callable;

/**
 * Created by Matt on 13/09/2016.
 */
public class Retry {
    private static final Log log = LogFactory.getLog(Retry.class);

    /**
     * Perform a runnable action, until the resulting condition is true
     *
     * @param action    The action to perform, such as {@code ()->$("button").click()}
     * @param condition The condition to validate that the action is complete, such as {@code ()->$("div.complete").exists()}
     * @param options   Configuration options to manage retry attempts, wait time between attempts etc
     * @throws Exception Throws exceptions from the action
     */
    public static void until(Runnable action, Callable<Boolean> condition, Options options) throws Exception {
        long startTime = new Date().getTime();
        for (int attempts = 0; (options.maxAttempts < 0 || attempts < options.maxAttempts) && (options.timeout < 0 || new Date().getTime() - startTime < options.timeout); attempts++) {
            Exception actionException = null;
            Exception conditionException = null;
            try {
                action.run();
            } catch (Exception e) {
                actionException = e;
                log.info("Caught an exception, while running action", e);
            }
            try {
                if (condition.call())
                    break;
                Thread.sleep(options.sleepTime);
            } catch (Exception e) {
                conditionException = e;
                log.info("Caught an exception, while evaluating condition", e);
            }

            // If we have reached our limit on attempts and have exceptions
            // in either the action or condition, then rethrow the appropriate exception
            if (attempts == options.maxAttempts) {
                if (null != actionException) {
                    throw actionException;
                } else if (null != conditionException) {
                    throw conditionException;
                }
            }
        }
    }

    /**
     * Perform a runnable action, until the resulting condition is true
     *
     * @param action    The action to perform, such as {@code ()->$("button").click()}
     * @param condition The condition to validate that the action is complete, such as {@code ()->$("div.complete").exists()}
     * @throws Exception Throws exceptions from the action
     */
    public static void until(Runnable action, Callable<Boolean> condition) throws Exception {
        until(action, condition, Options.getInstance());
    }

    /**
     * * Perform a callable action, until the resulting condition is true
     *
     * @param action    The action to perform, such as {@code ()->$("button").click()}
     * @param condition The condition to validate that the action is complete, such as {@code ()->$("div.complete").exists()}
     * @param options   Configuration options to manage retry attempts, wait time between attempts etc
     * @param <E>       The action resulting type
     * @return Returns the type, specified in action
     * @throws Exception Throws exceptions from the action
     */
    public static <E> E until(Callable<E> action, Callable<Boolean> condition, Options options) throws Exception {
        E result = null;
        long startTime = new Date().getTime();
        for (int attempts = 0; (options.maxAttempts < 0 || attempts < options.maxAttempts) && (options.timeout < 0 || new Date().getTime() - startTime < options.timeout); attempts++) {
            Exception actionException = null;
            Exception conditionException = null;
            try {
                result = action.call();
            } catch (Exception e) {
                actionException = e;
                log.info("Caught an exception, while running action", e);
            }
            try {
                if (null == condition) {
                    // Verify the condition of the action, as a null and not empty check
                    if (String.class.isInstance(result)) {
                        if (null != result && !(((String) result).isEmpty()))
                            return result;
                    } else if (Boolean.class.isInstance(result)) {
                        if (null != result && (((Boolean) result)))
                            return result;
                    } else {
                        if (null != result)
                            return result;
                    }
                } else {
                    // Verify the condition
                    if (condition.call())
                        return result;
                }
                Thread.sleep(options.sleepTime);
            } catch (Exception e) {
                conditionException = e;
                log.info("Caught an exception, while evaluating condition", e);
            }

            // If we have reached our limit on attempts and have exceptions
            // in either the action or condition, then rethrow the appropriate exception
            if (attempts == options.maxAttempts) {
                if (null != actionException) {
                    throw actionException;
                } else if (null != conditionException) {
                    throw conditionException;
                }
            }
        }
        return result;
    }

    /**
     * Perform a callable action, until the resulting condition is true
     *
     * @param action    The action to perform, such as {@code ()->$("button").click()}
     * @param condition The condition to validate that the action is complete, such as {@code ()->$("div.complete").exists()}
     * @param <E>       The action resulting type
     * @return Returns the type, specified in action
     * @throws Exception Throws exceptions from the action
     */
    public static <E> E until(Callable<E> action, Callable<Boolean> condition) throws Exception {
        return until(action, condition, Options.getInstance());
    }

    /**
     * Perform a condition, until the resulting condition is true
     *
     * @param condition The condition to validate that the action is complete, such as {@code ()->$("div.complete").exists()}
     * @param options   Configuration options to manage retry attempts, wait time between attempts etc
     * @throws Exception Throws exceptions from the action
     */
    public static void until(Callable<Boolean> condition, Options options) throws Exception {
        until(() -> {
        }, condition, options);
    }

    /**
     * Perform a condition, until the resulting condition is true
     *
     * @param condition The condition to validate that the action is complete, such as {@code ()->$("div.complete").exists()}
     * @throws Exception Throws exceptions from the action
     */
    public static void until(Callable<Boolean> condition) throws Exception {
        until(() -> {
        }, condition, Options.getInstance());
    }

    /**
     * Options class, used with SelenideRetry class
     * timeout and maxAttempts can be set to infinite by setting to -1
     */
    public static class Options {
        private static Options options = null;
        int timeout = 60000;
        int maxAttempts = 10;
        int sleepTime = 250;

        private Options() {
        }

        public static Options getInstance() {
            if (null == Options.options)
                Options.options = new Options();
            return Options.options;
        }

        public Options withMaxAttempts(int maxAttempts) {
            this.maxAttempts = maxAttempts;
            return this;
        }

        // Sleep time in milliseconds
        public Options withSleepTime(int sleepTime) {
            this.sleepTime = sleepTime;
            return this;
        }

        // Timeout in milliseconds
        public Options withTimeout(int timeout) {
            this.timeout = timeout;
            return this;
        }
    }
}
