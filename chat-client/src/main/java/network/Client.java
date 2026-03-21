package network;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

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
            Scanner input = new Scanner(System.in);
            while (true) {
                System.out.print("Enter Message: ");
                String msg = input.nextLine();
                dos.writeUTF(msg);
            }

        } catch (Exception e) {

            e.printStackTrace();

        }
    }
}