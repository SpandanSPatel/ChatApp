package controller;

import view.ChatView;
import view.LoginView;
import network.Client;
import model.Message;
import util.JsonUtil;
import util.Constants;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AuthController { // Class

    private LoginView view; // Encapsulation
    private Client client; // Encapsulation

    public AuthController(LoginView view, Client client) {
        this.view = view;
        this.client = client;

        initListeners();
    }

    private void initListeners() { // Encapsulation

        view.loginButton.addActionListener(e -> handleAuth(Constants.LOGIN)); // Polymorphism
        view.registerButton.addActionListener(e -> handleAuth(Constants.SIGNUP)); // Polymorphism
    }

    private void handleAuth(String type) { // Abstraction

        String username = view.usernameField.getText();
        String password = new String(view.passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Fields cannot be empty");
            return;
        }

        try {
            Message msg = new Message( // Object
                    type,
                    username,
                    "SERVER",
                    password,
                    System.currentTimeMillis());

            client.send(JsonUtil.toJson(msg));

            String responseJson = client.receive();
            Message response = JsonUtil.fromJson(responseJson, Message.class); // Object

            if (response.getContent().equals("SUCCESS")) {
                JOptionPane.showMessageDialog(view, "Login Successful!");

                view.dispose();
                SwingUtilities.invokeLater(() -> {
                    ChatView chatView = new ChatView(); // Object
                    new ChatController(chatView, client, username); // Object
                });

            } else {
                JOptionPane.showMessageDialog(view, "Authentication Failed!");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Error connecting to server");
        }
    }
}