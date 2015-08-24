package nz.co.afor.framework.test;

import com.jcabi.aspects.RetryOnFailure;
import com.jcabi.aspects.Timeable;
import org.apache.log4j.Logger;

import java.util.concurrent.TimeUnit;

/**
 * Copyright afor
 * Created by Matt Belcher on 10/08/2015.
 */
public class AspectOrientedExample {
    private Logger log = Logger.getLogger(this.getClass());
    private static Integer counter = 0;

    public static Integer getCounter() {
        return counter;
    }

    public static void setCounter(Integer counter) {
        AspectOrientedExample.counter = counter;
    }

    @Timeable(limit = 1, unit = TimeUnit.MILLISECONDS)
    public void timeout() throws InterruptedException {
        Thread.sleep(2000);
    }

    @RetryOnFailure(attempts = 3)
    public void retryIncrementCounter3Times() throws java.lang.InterruptedException {
        log.debug("Incrementing the retry counter");
        setCounter(getCounter() + 1);
        throw new RuntimeException("Expected exception to test counter increments");
    }
}
