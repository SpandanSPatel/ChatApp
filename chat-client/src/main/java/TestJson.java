import model.Message;
import util.JsonUtil;

public class TestJson {

    public static void main(String[] args) {

        Message msg = new Message(
                "CHAT",
                "spandan",
                "rahul",
                "yooo mfers",
                System.currentTimeMillis()
        );

        // Object → JSON
        String json = JsonUtil.toJson(msg);
        System.out.println("JSON Output:");
        System.out.println(json);

        // JSON → Object
        Message parsed = JsonUtil.fromJson(json, Message.class);

        System.out.println("\nParsed Object:");
        System.out.println("Sender: " + parsed.getSender());
        System.out.println("Receiver: " + parsed.getReceiver());
        System.out.println("Content: " + parsed.getContent());
    }
}