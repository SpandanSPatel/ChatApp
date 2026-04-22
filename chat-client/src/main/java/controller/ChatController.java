package controller;

import view.ChatView;
import network.Client;
import model.Message;
import util.JsonUtil;
import util.Constants;

import java.util.List;

import javax.swing.*;

public class ChatController {

    private ChatView view;
    private Client client;
    private String username;
    private String receiver = null;
    private boolean loadingHistory = false;

    public ChatController(ChatView view, Client client, String username) {
        this.view = view;
        this.client = client;
        this.username = username;

        initListeners();
        startListening();
    }

    private void initListeners() {

        view.sendButton.addActionListener(e -> sendMessage());
        view.messageField.addActionListener(e -> sendMessage());
        view.userList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {

                String selectedUser = view.userList.getSelectedValue();

                if (selectedUser != null && !selectedUser.equals(username)) {

                    receiver = selectedUser;

                    view.chatArea.setText("");

                    loadingHistory = true;

                    try {
                        Message switchMsg = new Message(
                                Constants.SWITCH,
                                username,
                                receiver,
                                "",
                                System.currentTimeMillis());

                        client.send(JsonUtil.toJson(switchMsg));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    private void sendMessage() {
        if (receiver == null) {
            JOptionPane.showMessageDialog(view, "Select a user first");
            return;
        }
        String content = view.messageField.getText().trim();

        if (content.isEmpty())
            return;

        try {
            Message msg = new Message(
                    Constants.CHAT,
                    username,
                    receiver,
                    content,
                    System.currentTimeMillis());

            client.send(JsonUtil.toJson(msg));

            view.addMessage(username + ": " + content);

            view.messageField.setText("");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadHistory() {
        try {
            while (true) {
                String json = client.receive();

                if (json.equals("END_HISTORY")) {
                    break;
                }

                Message msg = JsonUtil.fromJson(json, Message.class);
                SwingUtilities.invokeLater(() -> {
                    view.addMessage(msg.getSender() + ": " + msg.getContent());
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startListening() {

        new Thread(() -> {
            try {
                while (true) {

                    String json = client.receive();

                    if (json.equals("END_HISTORY")) {
                        loadingHistory = false;
                        continue;
                    }

                    Message msg = JsonUtil.fromJson(json, Message.class);
                    if (msg.getType().equals("USER_LIST")) {

                        List<String> users = JsonUtil.fromJson(
                                msg.getContent(),
                                new com.google.gson.reflect.TypeToken<List<String>>() {
                                }.getType());

                        SwingUtilities.invokeLater(() -> {
                            view.userListModel.clear();

                            for (String user : users) {
                                if (!user.equals(username)) {
                                    view.userListModel.addElement(user);
                                }
                            }
                        });

                        continue;
                    }
                    SwingUtilities.invokeLater(() -> {
                        view.addMessage(msg.getSender() + ": " + msg.getContent());
                    });
                }
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(view, "Disconnected from server"));
            }
        }).start();
    }
}