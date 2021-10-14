package nz.co.afor.stereotype.model;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Map;

public class ModelHooks {

    Logger log = LoggerFactory.getLogger(ModelHooks.class);

    @Autowired
    ApplicationContext applicationContext;

    @Before
    public void before(Scenario scenario) {
        for (Map.Entry<String, Object> entry : applicationContext.getBeansWithAnnotation(AforModel.class).entrySet()) {
            if (Model.class.isAssignableFrom(entry.getValue().getClass())) {
                ModelList modelList = (ModelList) entry.getValue();
                modelList.clear();
                modelList.before(scenario);
            } else {
                log.warn("The class '" + entry.getValue().getClass() + "' must be assignable from Model.class");
            }
        }
    }

    @After
    public void after(Scenario scenario) {
        for (Map.Entry<String, Object> entry : applicationContext.getBeansWithAnnotation(AforModel.class).entrySet()) {
            if (Model.class.isAssignableFrom(entry.getValue().getClass())) {
                ModelList modelList = (ModelList) entry.getValue();
                modelList.after(scenario);
                modelList.log(scenario);
            } else {
                log.warn("The class '" + entry.getValue().getClass() + "' must be assignable from Model.class");
            }
        }
    }
}
