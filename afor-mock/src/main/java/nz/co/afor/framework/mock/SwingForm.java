package nz.co.afor.framework.mock;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

/**
 * Created by Matt on 6/12/2016.
 */
public class SwingForm {
    private JButton createUserButton = new JButton("Create User");
    private JTextField usernameTextField = new JTextField();
    private JPasswordField passwordField = new JPasswordField();
    private JList userType = new JList();
    private JComboBox domain = new JComboBox();
    private JTable roles = new JTable();
    private JPanel panel = new JPanel();
    private JPanel userPanel = new JPanel();
    private JPanel buttonPanel = new JPanel();

    public JPanel getPanel() {
        return panel;
    }

    public SwingForm() {
        panel.setLayout(new BorderLayout());
        userPanel.setLayout(new BorderLayout());
        buttonPanel.setLayout(new BorderLayout());
        panel.add(userPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        userPanel.add(usernameTextField, BorderLayout.NORTH);
        userPanel.add(passwordField, BorderLayout.SOUTH);
        buttonPanel.add(createUserButton);
        createUserButton.setActionCommand("create");
        usernameTextField.setPreferredSize(new Dimension(250,25));
        passwordField.setPreferredSize(new Dimension(250,25));
        createUserButton.setPreferredSize(new Dimension(250,25));
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
