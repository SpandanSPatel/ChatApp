package view;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame { // Class, Inheritance

    public JTextField usernameField;
    public JPasswordField passwordField;
    public JButton loginButton;
    public JButton registerButton;

    public LoginView() {

        setTitle("Chat App - Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(); // Object
        panel.setLayout(new GridLayout(3, 2, 10, 10)); // Object

        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Username:")); // Object
        usernameField = new JTextField(); // Object
        panel.add(usernameField);

        panel.add(new JLabel("Password:")); // Object
        passwordField = new JPasswordField(); // Object
        panel.add(passwordField);

        loginButton = new JButton("Login"); // Object
        registerButton = new JButton("Register"); // Object

        panel.add(loginButton);
        panel.add(registerButton);

        add(panel);
        setVisible(true);
    }
    
}