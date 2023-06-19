import nz.co.afor.framework.mock.MockApplication;
import nz.co.afor.framework.mock.SwingSpringApplicationContextLoader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;

/**
 * Created by Matt Belcher on 20/08/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MockApplication.class, loader = SwingSpringApplicationContextLoader.class)
@SpringBootTest(classes = MockApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MockServiceTest {

    @LocalServerPort
    int serverPort;

    @Test
    public void testPortIsSet() {
        assertThat(serverPort, is(greaterThan(0)));
    }
}
