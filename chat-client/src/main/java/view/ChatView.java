package view;

import javax.swing.*;
import java.awt.*;

public class ChatView extends JFrame {

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

        setLayout(new BorderLayout());

        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        JScrollPane userScroll = new JScrollPane(userList);
        userScroll.setPreferredSize(new Dimension(120, 0));

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        JScrollPane chatScroll = new JScrollPane(chatArea);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        messageField = new JTextField();
        sendButton = new JButton("Send");

        bottomPanel.add(messageField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);

        JPanel chatPanel = new JPanel(new BorderLayout());
        chatPanel.add(chatScroll, BorderLayout.CENTER);
        chatPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(userScroll, BorderLayout.WEST);
        add(chatPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    public void addMessage(String message) {
        chatArea.append(message + "\n");
    }
}