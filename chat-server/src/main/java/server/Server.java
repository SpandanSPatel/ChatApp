package server;

import java.net.Socket;

import io.github.cdimascio.dotenv.Dotenv;

import java.net.ServerSocket;

public class Server { // Class

    public static void main(String[] args) {

        System.out.println("Server Starting...");

        try {

            Dotenv dotenv = Dotenv.configure().directory("chat-server").load();

            int port = Integer.parseInt(dotenv.get("SERVER_PORT"));

            System.out.println("Server is Starting");

            ServerSocket serverSocket = new ServerSocket(port); // Object

            System.out.println("Server started on Port: " + port);
            
            while (true) {
                Socket socket = serverSocket.accept(); // Object
                System.out.println("New Client connected: " + socket.getInetAddress());
                ClientHandler handler = new ClientHandler(socket); // Object
                new Thread(handler).start(); // Object, Polymorphism
            }

        } catch (Exception e) {

            e.printStackTrace();

        }
    }
}