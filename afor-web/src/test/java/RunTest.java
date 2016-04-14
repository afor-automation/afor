import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

/**
 * Created by Matt Belcher on 12/10/2015.
 */
@RunWith(Cucumber.class)
@ContextConfiguration("classpath:cucumber.xml")
@CucumberOptions(plugin = {"pretty", "nz.co.afor.reports.HTML:target/afor", "junit:target/afor/junit.xml", "rerun:target/afor/rerun.txt"})
public class RunTest {
}
