package service;

import model.User;
import java.util.concurrent.ConcurrentHashMap;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import database.MongoService;

public class AuthService { // Class

    private static final MongoCollection<Document> userCollection; // Encapsulation

    static {
        MongoDatabase db = MongoService.getDatabase();
        userCollection = db.getCollection("users");
    }

    public static boolean register(User user) { // Abstraction
        String username = user.getUsername();

        Document exisiting = userCollection.find(new Document("username", username)).first(); // Object

        if (exisiting != null) {
            return false;
        }

        Document doc = new Document("username", username).append("password", user.getPassword()); // Object

        userCollection.insertOne(doc);

        return true;
    }

    public static boolean login(String username, String password) { // Abstraction

        Document userDoc = userCollection.find(new Document("username", username)).first(); // Object

        if (userDoc == null) {
            return false;
        }

        String storedPassword = userDoc.getString("password");

        return storedPassword.equals(password);
    }
}