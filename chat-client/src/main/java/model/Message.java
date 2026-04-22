package model;
public class Message { // Class

    private String type; // Encapsulation
    private String sender; // Encapsulation
    private String receiver; // Encapsulation
    private String content; // Encapsulation
    private long timestamp; // Encapsulation
    public Message(){}
    public Message(String type, String sender, String receiver, String content, long timestamp){
        this.type = type;
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.timestamp = timestamp;
    }
    public String getType() { // Encapsulation
        return type;
    }
    public void setType(String type) { // Encapsulation
        this.type = type;
    }
    public String getSender() { // Encapsulation
        return sender;
    }
    public void setSender(String sender) { // Encapsulation
        this.sender = sender;
    }
    public String getReceiver() { // Encapsulation
        return receiver;
    }
    public void setReceiver(String receiver) { // Encapsulation
        this.receiver = receiver;
    }
    public String getContent() { // Encapsulation
        return content;
    }
    public void setContent(String content) { // Encapsulation
        this.content = content;
    }
    public long getTimestamp() { // Encapsulation
        return timestamp;
    }
    public void setTimestamp(long timestamp) { // Encapsulation
        this.timestamp = timestamp;
    }
}