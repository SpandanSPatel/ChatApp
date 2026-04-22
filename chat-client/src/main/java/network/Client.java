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

            boolean authenticated = false;
            String username = "";
            while (!authenticated) {
                System.out.println("1. Login");
                System.out.println("2. Register");
                System.out.println("Choose Option: ");
                int choice = new Scanner(System.in).nextInt();

                System.out.print("Enter username: ");
                username = input.nextLine();

                System.out.print("Enter password: ");
                String password = input.nextLine();

                String type = (choice == 1) ? Constants.LOGIN : Constants.SIGNUP;

                Message authMsg = new Message(type, username, "SERVER", password, System.currentTimeMillis());

                String authjson = JsonUtil.toJson(authMsg);
                dos.writeUTF(authjson);

                String responseJson = dis.readUTF();
                Message response = JsonUtil.fromJson(responseJson, Message.class);

                if (response.getContent().equals("SUCCESS")) {
                    System.out.println("Authentication Success!");
                    authenticated = true;
                } else {
                    System.out.println("Authentication Failed, Try again.\n");
                }

            }

            System.out.print("Enter Sender: ");
            String receiver = input.nextLine();

            dos.writeUTF(receiver);

            while (true) {
                String response = dis.readUTF();

                if (response.equals("END_HISTORY")) {
                    break;
                }

                Message msg = JsonUtil.fromJson(response, Message.class);

                System.out.println(msg.getSender() + ": " + msg.getContent());
            }

            new Thread(() -> {
                try {
                    while (true) {
                        String json = dis.readUTF();
                        Message msg = JsonUtil.fromJson(json, Message.class);
                        System.out.println("\nNew Message!");
                        System.out.println(msg.getSender() + ": " + msg.getContent());
                        System.out.print("Enter Message: ");
                    }
                } catch (Exception e) {
                    System.out.println("Disconnected from server.");
                }
            }).start();

            while (true) {
                System.out.print("Enter Message: ");
                String strMsg = input.nextLine();

                Message msg = new Message(Constants.CHAT, username, receiver, strMsg, System.currentTimeMillis());

                String json = JsonUtil.toJson(msg);

                dos.writeUTF(json);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}