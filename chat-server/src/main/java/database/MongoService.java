package database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import io.github.cdimascio.dotenv.Dotenv;

public class MongoService { // Class

    private static MongoClient mongoClient; // Encapsulation
    private static MongoDatabase database; // Encapsulation

    static {
        try {

            Dotenv dotenv = Dotenv.configure().directory("chat-server").load();

            String uri = dotenv.get("MONGO_URI");
            String dbName = dotenv.get("DB_NAME");

            mongoClient = MongoClients.create(uri); // Object
            database = mongoClient.getDatabase(dbName);

            System.out.println("Connected to MongoDB!");

        } catch (Exception e) {
            System.out.println("MongoDB Connection Failed");
            e.printStackTrace();
        }
    }

    public static MongoDatabase getDatabase() { // Abstraction
        return database;
    }
}