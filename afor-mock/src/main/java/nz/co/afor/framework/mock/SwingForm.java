package nz.co.afor.framework.mock;

import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

/**
 * Created by Matt on 24/11/2016.
 */
@Component
public class SwingForm {
    private JButton createUserButton;
    private JTextField usernameTextField;
    private JPasswordField passwordField;
    private JList userType;
    private JComboBox domain;
    private JTable roles;
    private JPanel panel;

    public JPanel getPanel() {
        return panel;
    }

    public SwingForm() {
        createUserButton.setActionCommand("create");
        createUserButton.addActionListener(e -> {
            if (usernameTextField.getText().equalsIgnoreCase("username") && Arrays.equals(passwordField.getPassword(), "password".toCharArray())) {
                usernameTextField.setEnabled(false);
                passwordField.setEnabled(false);
                userType.setEnabled(false);
                domain.setEnabled(false);
                roles.setEnabled(false);
                createUserButton.setEnabled(false);
                message("User created successfully");
            } else {
                alert("Could not create user, invalid username or password");
            }
        });
    }

    private void alert(String message) {
        JOptionPane optionPane = new JOptionPane(message, JOptionPane.WARNING_MESSAGE);
        JDialog dialog = optionPane.createDialog("Warning!");
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }

    private void message(String message) {
        JOptionPane optionPane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = optionPane.createDialog("Success!");
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }
}
