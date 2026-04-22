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

public class AuthController {

    private LoginView view;
    private Client client;

    public AuthController(LoginView view, Client client) {
        this.view = view;
        this.client = client;

        initListeners();
    }

    private void initListeners() {

        view.loginButton.addActionListener(e -> handleAuth(Constants.LOGIN));
        view.registerButton.addActionListener(e -> handleAuth(Constants.SIGNUP));
    }

    private void handleAuth(String type) {

        String username = view.usernameField.getText();
        String password = new String(view.passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Fields cannot be empty");
            return;
        }

        try {
            Message msg = new Message(
                    type,
                    username,
                    "SERVER",
                    password,
                    System.currentTimeMillis());

            client.send(JsonUtil.toJson(msg));

            String responseJson = client.receive();
            Message response = JsonUtil.fromJson(responseJson, Message.class);

            if (response.getContent().equals("SUCCESS")) {
                JOptionPane.showMessageDialog(view, "Login Successful!");

                view.dispose();
                SwingUtilities.invokeLater(() -> {
                    ChatView chatView = new ChatView();
                    new ChatController(chatView, client, username);
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