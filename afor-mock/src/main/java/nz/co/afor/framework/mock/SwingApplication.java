package nz.co.afor.framework.mock;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;

/**
 * Created by Matt on 24/11/2016.
 */
@SpringBootApplication
public class SwingApplication {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SwingForm gui = new SwingForm();
            JFrame frame = new JFrame("Afor Swing Mock");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.getContentPane().add(gui.getPanel());
            frame.pack();
            frame.setVisible(true);
        });
    }

}
