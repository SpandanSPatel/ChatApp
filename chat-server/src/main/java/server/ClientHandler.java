package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

import model.*;
import util.*;
import service.ChatService;
import service.AuthService;

public class ClientHandler implements Runnable {

    private Socket socket;
    private String username;
    private DataOutputStream dos;

    ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.dos = new DataOutputStream(socket.getOutputStream());
    }

    public void sendMessage(String json) {
        try {
            dos.writeUTF(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {

            DataInputStream dis = new DataInputStream(socket.getInputStream());
            while (true) {

                String authJson = dis.readUTF();

                Message authMsg = JsonUtil.fromJson(authJson, Message.class);
                String username = authMsg.getSender();
                String password = authMsg.getContent();
                String type = authMsg.getType();

                if (username == null || username.trim().isEmpty() || password == null
                        || password.trim().isEmpty()) {
                    Message response = new Message(Constants.AUTH, "SERVER", "UNKNOWN", "INVALID_INPUT",
                            System.currentTimeMillis());

                    dos.writeUTF(JsonUtil.toJson(response));

                    continue;
                }

                if (type.equals(Constants.LOGIN)) {
                    if (ChatService.getClient(username) != null) {
                        Message response = new Message(Constants.AUTH, "SERVER", username, "ALREADY_LOGGED_IN",
                                System.currentTimeMillis());

                        dos.writeUTF(JsonUtil.toJson(response));

                        continue;
                    }
                }
                boolean success = false;

                if (type.equals(Constants.SIGNUP)) {
                    success = AuthService.register(new model.User(username, password));
                } else if (type.equals(Constants.LOGIN)) {
                    success = AuthService.login(username, password);
                }
                Message response = new Message(Constants.AUTH, "SERVER", username, success ? "SUCCESS" : "FAIL",
                        System.currentTimeMillis());

                dos.writeUTF(JsonUtil.toJson(response));

                if (success) {

                    this.username = username;
                    ChatService.addClient(username, this);
                    ChatService.broadcastUserList();

                    System.out.println(username + " Logged in");

                    break;
                }
            }
            String receiver;

            while (true) {
                String json = dis.readUTF();

                Message msg = JsonUtil.fromJson(json, Message.class);

                if (msg.getType().equals("SWITCH")) {

                    receiver = msg.getReceiver();

                    List<Message> history = ChatService.getLastMessages(username, receiver);

                    for (Message m : history) {
                        dos.writeUTF(JsonUtil.toJson(m));
                    }

                    dos.writeUTF("END_HISTORY");

                    continue;
                }

                ChatService.saveMessage(msg);

                receiver = msg.getReceiver();

                ClientHandler receiverHandler = ChatService.getClient(receiver);

                if (receiverHandler != null) {
                    receiverHandler.sendMessage(json);
                } else {
                    Message errorMsg = new Message(Constants.CHAT, "SERVER", msg.getSender(),
                            "User not online: " + receiver, System.currentTimeMillis());

                    String errorJson = JsonUtil.toJson(errorMsg);
                    this.sendMessage(errorJson);
                }

                System.out.println("Received :");
                System.out.println("From: " + msg.getSender());
                System.out.println("To: " + msg.getReceiver());
                System.out.println("Content: " + msg.getContent());
                System.out.println("----------------------");
            }

        } catch (Exception e) {

            System.out.println("Client Disconnected: " + socket.getInetAddress());
            try {
                if (username != null) {
                    ChatService.removeClient(username);
                    ChatService.broadcastUserList();
                }
                socket.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
