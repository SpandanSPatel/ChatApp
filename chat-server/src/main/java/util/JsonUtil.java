package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.Type;

public class JsonUtil { // Class
    private static final Gson gson = new GsonBuilder().create(); // Encapsulation

    public static String toJson(Object obj) { // Abstraction
        return gson.toJson(obj);
    }

    public static <T> T fromJson(String json, Class<T> clazz) { // Polymorphism
        return gson.fromJson(json, clazz);
    }

    public static <T> T fromJson(String json, Type type) { // Polymorphism
        return gson.fromJson(json, type);
    }
}