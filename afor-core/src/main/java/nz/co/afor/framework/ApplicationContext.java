package nz.co.afor.framework;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContext implements ApplicationContextAware {

    private static org.springframework.context.ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(org.springframework.context.ApplicationContext applicationContext) throws BeansException {
        ApplicationContext.applicationContext = applicationContext;
    }

    public static org.springframework.context.ApplicationContext getContext() {
        return ApplicationContext.applicationContext;
    }
}
