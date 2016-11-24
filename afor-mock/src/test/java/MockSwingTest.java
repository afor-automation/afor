import nz.co.afor.framework.mock.Application;
import nz.co.afor.framework.mock.SwingApplication;
import nz.co.afor.framework.mock.SwingForm;
import nz.co.afor.framework.mock.SwingSpringApplicationContextLoader;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.swing.*;

/**
 * Created by Matt on 24/11/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SwingApplication.class, loader=SwingSpringApplicationContextLoader.class)
public class MockSwingTest {

    private FrameFixture window;

    @Before
    public void setUp() {
        System.getProperty("java.awt.headless");
        JFrame jFrame = new JFrame();
        jFrame.getContentPane().add(new SwingForm().getPanel());
        window = new FrameFixture(jFrame);
        window.show();
    }

    @Test
    public void testSwingApplicationRuns() {
        window.button(JButtonMatcher.withText("Create User")).click();
    }

    @After
    public void tearDown() {
        window.cleanUp();
    }
}
