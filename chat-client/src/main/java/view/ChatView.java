package view;

import javax.swing.*;
import java.awt.*;

public class ChatView extends JFrame { // Class, Inheritance

    public JTextArea chatArea;
    public JTextField messageField;
    public JButton sendButton;
    public JList<String> userList;
    public DefaultListModel<String> userListModel;
    public JLabel typingLabel;
    private JLabel usernameLabel;

    public ChatView() {

        setTitle("Chat App");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout()); // Object

        usernameLabel = new JLabel("Logged in as: "); // Object
        add(usernameLabel, BorderLayout.NORTH);


        userListModel = new DefaultListModel<>(); // Object
        userList = new JList<>(userListModel); // Object
        JScrollPane userScroll = new JScrollPane(userList); // Object
        userScroll.setPreferredSize(new Dimension(140, 0)); // Object


        chatArea = new JTextArea(); // Object
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        JScrollPane chatScroll = new JScrollPane(chatArea); // Object


        JPanel bottomPanel = new JPanel(new BorderLayout()); // Object

        typingLabel = new JLabel(" "); // Object

        messageField = new JTextField(); // Object
        sendButton = new JButton("Send"); // Object

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        bottomPanel.add(typingLabel, BorderLayout.NORTH);
        bottomPanel.add(inputPanel, BorderLayout.SOUTH);

        JPanel chatPanel = new JPanel(new BorderLayout()); // Object
        chatPanel.add(chatScroll, BorderLayout.CENTER);
        chatPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(userScroll, BorderLayout.WEST);
        add(chatPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    public void addMessage(String message) { // Abstraction
        chatArea.append(message + "\n");
    }


    public void setLoggedInUser(String username) {
        usernameLabel.setText("Logged in as: " + username);
        setTitle("Chat App - " + username);
    }


    public void showTypingIndicator(String username) {
        typingLabel.setText(username + " is typing...");
    }


    public void hideTypingIndicator() {
        typingLabel.setText(" ");
    }
}