package network;

import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.net.Socket;
import java.util.Scanner;

import model.Message;
import util.Constants;
import util.JsonUtil;

import io.github.cdimascio.dotenv.Dotenv;

public class Client { // Class

    static DataOutputStream dos;
    static DataInputStream dis;

    public Client() {
        try {
            Dotenv dotenv = Dotenv.configure().directory("chat-client").load();

            String ip = dotenv.get("SERVER_IP");
            int port = Integer.parseInt(dotenv.get("SERVER_PORT"));

            System.out.println("Connecting to server...");

            Socket socket = new Socket(ip, port); // Object

            System.out.println("Connected to server!!");
            dos = new DataOutputStream(socket.getOutputStream()); // Object
            dis = new DataInputStream(socket.getInputStream()); // Object
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(String msg) throws Exception { // Abstraction
        dos.writeUTF(msg);
    }

    public String receive() throws Exception { // Abstraction
        return dis.readUTF();
    }

}