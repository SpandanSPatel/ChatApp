package service;

import model.User;
import java.util.concurrent.ConcurrentHashMap;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import database.MongoService;

public class AuthService {

    private static final MongoCollection<Document> userCollection;

    static {
        MongoDatabase db = MongoService.getDatabase();
        userCollection = db.getCollection("users");
    }

    public static boolean register(User user) {
        String username = user.getUsername();

        Document exisiting = userCollection.find(new Document("username", username)).first();

        if (exisiting != null) {
            return false;
        }

        Document doc = new Document("username", username).append("password", user.getPassword());

        userCollection.insertOne(doc);

        return true;
    }

    public static boolean login(String username, String password) {

        Document userDoc = userCollection.find(new Document("username", username)).first();

        if (userDoc == null) {
            return false;
        }

        String storedPassword = userDoc.getString("password");

        return storedPassword.equals(password);
    }
}