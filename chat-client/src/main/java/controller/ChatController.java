package controller;

import view.ChatView;
import network.Client;
import model.Message;
import util.JsonUtil;
import util.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class ChatController { // Class

    private ChatView view; // Encapsulation
    private Client client; // Encapsulation
    private String username; // Encapsulation
    private String receiver = null; // Encapsulation
    private boolean loadingHistory = false; // Encapsulation

    private Map<String, Integer> unreadCounts = new HashMap<>(); // Encapsulation, Object

    private boolean isTyping = false; // Encapsulation
    private Timer typingTimer; // Encapsulation

    public ChatController(ChatView view, Client client, String username) {
        this.view = view;
        this.client = client;
        this.username = username;

        view.setLoggedInUser(username);

        initListeners();
        startListening();
    }

    private void initListeners() { // Encapsulation

        view.addWindowListener(new WindowAdapter() { // Object, Polymorphism
            @Override
            public void windowClosing(WindowEvent e) { // Polymorphism
                client.close();
            }
        });

        view.sendButton.addActionListener(e -> sendMessage()); // Polymorphism
        view.messageField.addActionListener(e -> sendMessage()); // Polymorphism
        view.userList.addListSelectionListener(e -> { // Polymorphism
            if (!e.getValueIsAdjusting()) {

                String selectedUser = view.userList.getSelectedValue();

                if (selectedUser != null && !stripBadge(selectedUser).equals(username)) {

                    receiver = stripBadge(selectedUser);

                    unreadCounts.remove(receiver);
                    updateUserListBadges();

                    view.hideTypingIndicator();

                    view.chatArea.setText("");

                    loadingHistory = true;

                    try {
                        Message switchMsg = new Message( // Object
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

        typingTimer = new Timer(500, e -> { // Object
            if (isTyping) {
                isTyping = false;
                sendTypingStatus(Constants.TYPING_STOP);
            }
        });
        typingTimer.setRepeats(false);

        view.messageField.getDocument().addDocumentListener(new DocumentListener() { // Object, Polymorphism
            @Override
            public void insertUpdate(DocumentEvent e) {
                onTyping();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                onTyping();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                onTyping();
            }
        });
    }

    private void onTyping() { // Abstraction
        if (receiver == null)
            return;

        if (!isTyping) {
            isTyping = true;
            sendTypingStatus(Constants.TYPING_START);
        }
        typingTimer.restart();
    }

    private void sendTypingStatus(String type) { // Abstraction
        if (receiver == null)
            return;
        try {
            Message msg = new Message( // Object
                    type,
                    username,
                    receiver,
                    "",
                    System.currentTimeMillis());
            client.send(JsonUtil.toJson(msg));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void updateUserListBadges() { // Abstraction
        for (int i = 0; i < view.userListModel.size(); i++) {
            String name = stripBadge(view.userListModel.get(i));
            int count = unreadCounts.getOrDefault(name, 0);
            String display = count > 0 ? name + " (" + count + ")" : name;
            if (!display.equals(view.userListModel.get(i))) {
                view.userListModel.set(i, display);
            }
        }
    }

    private String stripBadge(String text) { // Abstraction
        if (text == null)
            return null;
        int idx = text.lastIndexOf(" (");
        if (idx > 0 && text.endsWith(")")) {
            return text.substring(0, idx);
        }
        return text;
    }

    private void sendMessage() { // Abstraction
        if (receiver == null) {
            JOptionPane.showMessageDialog(view, "Select a user first");
            return;
        }
        String content = view.messageField.getText().trim();

        if (content.isEmpty())
            return;

        try {
            Message msg = new Message( // Object
                    Constants.CHAT,
                    username,
                    receiver,
                    content,
                    System.currentTimeMillis());

            client.send(JsonUtil.toJson(msg));

            view.addMessage(username + ": " + content);

            view.messageField.setText("");

            if (isTyping) {
                sendTypingStatus(Constants.TYPING_STOP);
                isTyping = false;
            }
            typingTimer.stop();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void startListening() { // Abstraction

        new Thread(() -> { // Object, Polymorphism
            try {
                while (true) {

                    String json = client.receive();

                    if (json.equals("END_HISTORY")) {
                        loadingHistory = false;
                        continue;
                    }

                    Message msg = JsonUtil.fromJson(json, Message.class); // Object

                    if (msg.getType().equals(Constants.USER_LIST)) {

                        List<String> users = JsonUtil.fromJson(
                                msg.getContent(),
                                new com.google.gson.reflect.TypeToken<List<String>>() {
                                }.getType());

                        SwingUtilities.invokeLater(() -> {
                            String currentSelection = view.userList.getSelectedValue();

                            view.userListModel.clear();

                            for (String user : users) {
                                if (!user.equals(username)) {
                                    view.userListModel.addElement(user);
                                }
                            }

                            if (currentSelection != null) {
                                String cleanSelection = stripBadge(currentSelection);
                                for (int i = 0; i < view.userListModel.size(); i++) {
                                    if (view.userListModel.get(i).equals(cleanSelection)) {
                                        view.userList.setSelectedIndex(i);
                                        break;
                                    }
                                }
                            }

                            updateUserListBadges();
                        });

                        continue;
                    }

                    if (msg.getType().equals(Constants.TYPING_START)) {
                        SwingUtilities.invokeLater(() -> {
                            if (msg.getSender().equals(receiver)) {
                                view.showTypingIndicator(msg.getSender());
                            }
                        });
                        continue;
                    }

                    if (msg.getType().equals(Constants.TYPING_STOP)) {
                        SwingUtilities.invokeLater(() -> {
                            if (msg.getSender().equals(receiver)) {
                                view.hideTypingIndicator();
                            }
                        });
                        continue;
                    }

                    if (msg.getType().equals(Constants.CHAT)) {
                        SwingUtilities.invokeLater(() -> {
                            String sender = msg.getSender();

                            if (sender.equals(receiver) || sender.equals(username)) {
                                view.addMessage(sender + ": " + msg.getContent());
                            } else {
                                unreadCounts.put(sender,
                                        unreadCounts.getOrDefault(sender, 0) + 1);
                                updateUserListBadges();
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