package nz.co.afor.framework.mock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.SpringApplicationContextLoader;

/**
 * Created by Matt on 24/11/2016.
 */
public class SwingSpringApplicationContextLoader extends SpringApplicationContextLoader {
    @Override
    protected SpringApplication getSpringApplication() {
        return new SpringApplicationBuilder().headless(false).build();
    }
}
