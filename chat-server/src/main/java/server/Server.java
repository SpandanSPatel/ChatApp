package server;

import java.net.Socket;

import io.github.cdimascio.dotenv.Dotenv;

import java.net.ServerSocket;

public class Server {

    public static void main(String[] args) {

        System.out.println("Server Starting...");

        try {

            Dotenv dotenv = Dotenv.configure().directory("chat-server").load();

            int port = Integer.parseInt(dotenv.get("SERVER_PORT"));

            System.out.println("Server is Starting");

            ServerSocket serverSocket = new ServerSocket(port);

            System.out.println("Server started on Port: " + port);
            
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New Client connected: " + socket.getInetAddress());
                ClientHandler handler = new ClientHandler(socket);
                new Thread(handler).start();
            }

        } catch (Exception e) {

            e.printStackTrace();

        }
    }
}