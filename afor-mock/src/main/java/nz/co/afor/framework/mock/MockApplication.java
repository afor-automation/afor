package nz.co.afor.framework.mock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.swing.*;

/**
 * Created by Matt Belcher on 18/08/2015.
 */
@SpringBootApplication
public class MockApplication {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SwingForm gui = new SwingForm();
            JFrame frame = new JFrame("Afor Swing Mock");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.getContentPane().add(gui.getPanel());
            frame.pack();
            frame.setVisible(true);
        });
        SpringApplication.run(MockApplication.class, args);
    }

}
