package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import model.*;
import util.*;
import service.ChatService;

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
            this.username = dis.readUTF();

            System.out.println(username + " connected");

            ChatService.addClient(username, this);

            while (true) {
                String json = dis.readUTF();

                Message msg = JsonUtil.fromJson(json, Message.class);
                
                String receiver = msg.getReceiver();

                ClientHandler receiverHandler = ChatService.getClient(receiver);

                if (receiverHandler!=null) {
                    receiverHandler.sendMessage(json);
                }else{
                    System.out.println("User not online: "+ receiver);
                }
                
                System.out.println("Received :");
                System.out.println("From: " + msg.getSender());
                System.out.println("To: " + msg.getReceiver());
                System.out.println("Content: " + msg.getContent());
                System.out.println("----------------------");
            }

        } catch (Exception e) {

            System.out.println("Client Disconnected: " + socket.getInetAddress());

        }

    }

}