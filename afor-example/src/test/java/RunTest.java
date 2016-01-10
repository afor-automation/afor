import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

/**
 * Created by Matt Belcher on 12/10/2015.
 */
@RunWith(Cucumber.class)
@ContextConfiguration("classpath:cucumber.xml")
@CucumberOptions(plugin = {"pretty", "html:target/cucumber", "junit:target/cucumber/junit.xml", "rerun:target/cucumber/rerun.txt"})
public class RunTest {
}
