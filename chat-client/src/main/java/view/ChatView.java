package view;

import javax.swing.*;
import java.awt.*;

public class ChatView extends JFrame { // Class, Inheritance

    public JTextArea chatArea;
    public JTextField messageField;
    public JButton sendButton;
    public JList<String> userList;
    public DefaultListModel<String> userListModel;

    public ChatView() {

        setTitle("Chat App");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout()); // Object

        userListModel = new DefaultListModel<>(); // Object
        userList = new JList<>(userListModel); // Object
        JScrollPane userScroll = new JScrollPane(userList); // Object
        userScroll.setPreferredSize(new Dimension(120, 0)); // Object

        chatArea = new JTextArea(); // Object
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        JScrollPane chatScroll = new JScrollPane(chatArea); // Object

        JPanel bottomPanel = new JPanel(new BorderLayout()); // Object
        messageField = new JTextField(); // Object
        sendButton = new JButton("Send"); // Object

        bottomPanel.add(messageField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);

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
}