import nz.co.afor.framework.mock.Application;
import nz.co.afor.framework.mock.SwingForm;
import nz.co.afor.framework.mock.SwingSpringApplicationContextLoader;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.swing.*;

/**
 * Created by Matt on 24/11/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class, loader = SwingSpringApplicationContextLoader.class)
public class MockSwingTest {

    private FrameFixture window;

    @Before
    public void setUp() {
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
        if (null != window)
            window.cleanUp();
    }
}
