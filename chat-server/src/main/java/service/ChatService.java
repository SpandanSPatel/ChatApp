package service;

import server.ClientHandler;
import util.JsonUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;

import java.util.concurrent.ConcurrentHashMap;

import org.bson.Document;

import database.MongoService;
import model.Message;
import util.Constants;

public class ChatService { // Class
    private static final ConcurrentHashMap<String, ClientHandler> clients = new ConcurrentHashMap<>(); // Encapsulation, Object

    private static final MongoCollection<Document> messageCollection; // Encapsulation

    static {
        MongoDatabase db = MongoService.getDatabase();
        messageCollection = db.getCollection("messages");
    }

    public static void addClient(String username, ClientHandler handler) { // Abstraction
        clients.put(username, handler);
    }

    public static void removeClient(String username) { // Abstraction
        clients.remove(username);
    }

    public static ClientHandler getClient(String username) { // Abstraction
        return clients.get(username);
    }

    public static void saveMessage(Message msg) { // Abstraction

        Document doc = new Document("type", msg.getType()) // Object
                .append("sender", msg.getSender())
                .append("receiver", msg.getReceiver())
                .append("content", msg.getContent())
                .append("timestamp", msg.getTimestamp());

        messageCollection.insertOne(doc);
    }

    public static List<Message> getLastMessages(String user1, String user2) { // Abstraction

        List<Message> messages = new ArrayList<>(); // Object

        FindIterable<Document> docs = messageCollection
                .find(
                        Filters.or(
                                Filters.and(
                                        Filters.eq("sender", user1),
                                        Filters.eq("receiver", user2)),
                                Filters.and(
                                        Filters.eq("sender", user2),
                                        Filters.eq("receiver", user1))))
                .sort(Sorts.descending("timestamp"))
                .limit(50);

        for (Document doc : docs) {
            Message msg = new Message( // Object
                    doc.getString("type"),
                    doc.getString("sender"),
                    doc.getString("receiver"),
                    doc.getString("content"),
                    doc.getLong("timestamp"));
            messages.add(msg);
        }

        Collections.reverse(messages);

        return messages;
    }

    public static void broadcastUserList() { // Abstraction
        try {
            List<String> users = new ArrayList<>(clients.keySet()); // Object

            Message msg = new Message( // Object
                    Constants.USER_LIST,
                    "SERVER",
                    "ALL",
                    JsonUtil.toJson(users),
                    System.currentTimeMillis());

            String json = JsonUtil.toJson(msg);

            for (ClientHandler ch : clients.values()) {
                ch.sendMessage(json);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}