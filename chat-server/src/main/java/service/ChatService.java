package service;

import server.ClientHandler;
import java.util.concurrent.ConcurrentHashMap;

public class ChatService {
    private static final ConcurrentHashMap<String, ClientHandler> clients = new ConcurrentHashMap<>();

    public static void addClient(String username, ClientHandler handler) {
        clients.put(username, handler);
    }

    public static void removeClient(String username) {
        clients.remove(username);
    }

    public static ClientHandler getClient(String username) {
        return clients.get(username);
    }
}