package server;

import java.io.DataInputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket socket;

    ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {

            DataInputStream dis = new DataInputStream(socket.getInputStream());

            while (true) {
                String messege = dis.readUTF();
                System.out.println("Received: "+messege);
            }

        } catch (Exception e) {

            System.out.println("Client Disconnected: "+ socket.getInetAddress());

        }

    }

}