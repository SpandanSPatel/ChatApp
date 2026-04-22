package service;

import server.ClientHandler;

import java.util.ArrayList;
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

public class ChatService {
    private static final ConcurrentHashMap<String, ClientHandler> clients = new ConcurrentHashMap<>();

    private static final MongoCollection<Document> messageCollection;

    static {
        MongoDatabase db = MongoService.getDatabase();
        messageCollection = db.getCollection("messages");
    }

    public static void addClient(String username, ClientHandler handler) {
        clients.put(username, handler);
    }

    public static void removeClient(String username) {
        clients.remove(username);
    }

    public static ClientHandler getClient(String username) {
        return clients.get(username);
    }

    public static void saveMessage(Message msg) {

        Document doc = new Document("type", msg.getType())
                .append("sender", msg.getSender())
                .append("receiver", msg.getReceiver())
                .append("content", msg.getContent())
                .append("timestamp", msg.getTimestamp());

        messageCollection.insertOne(doc);
    }

    public static List<Message> getLastMessages(String user1, String user2) {

        List<Message> messages = new ArrayList<>();

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
            Message msg = new Message(
                    doc.getString("type"),
                    doc.getString("sender"),
                    doc.getString("receiver"),
                    doc.getString("content"),
                    doc.getLong("timestamp"));
            messages.add(msg);
        }

        return messages;
    }

}