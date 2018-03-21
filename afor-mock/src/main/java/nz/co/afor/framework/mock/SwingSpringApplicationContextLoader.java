package nz.co.afor.framework.mock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Matt on 24/11/2016.
 */
@Configuration
public class SwingSpringApplicationContextLoader extends SpringBootContextLoader {

    @Override
    protected SpringApplication getSpringApplication() {
        return new SpringApplicationBuilder().headless(false).build();
    }
}
