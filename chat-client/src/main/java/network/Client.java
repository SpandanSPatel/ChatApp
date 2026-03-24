package network;

import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.net.Socket;
import java.util.Scanner;

import model.Message;
import util.Constants;
import util.JsonUtil;

import io.github.cdimascio.dotenv.Dotenv;

public class Client {

    public static void main(String[] args) {

        try {

            Dotenv dotenv = Dotenv.configure().directory("chat-client").load();

            String ip = dotenv.get("SERVER_IP");
            int port = Integer.parseInt(dotenv.get("SERVER_PORT"));

            System.out.println("Connecting to server...");

            Socket socket = new Socket(ip, port);

            System.out.println("Connected to server!!");
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            Scanner input = new Scanner(System.in);

            System.out.println("Enter username: ");
            String username = input.nextLine();

            dos.writeUTF(username);

            new Thread(() -> {
                try {
                    while (true) {
                        String json = dis.readUTF();
                        Message msg = JsonUtil.fromJson(json, Message.class);
                        System.out.println("New Message!");
                        System.out.println(msg.getSender() + ": " + msg.getContent());
                        System.out.println("Enter Message: ");
                    }
                } catch (Exception e) {
                    System.out.println("Disconnected from server.");
                }
            }).start();

            while (true) {
                System.out.print("Enter Message: ");
                String strMsg = input.nextLine();

                Message msg = new Message(Constants.CHAT, username, "Krush", strMsg, System.currentTimeMillis());

                String json = JsonUtil.toJson(msg);

                dos.writeUTF(json);
            }

        } catch (Exception e) {

            e.printStackTrace();

        }
    }
}