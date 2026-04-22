package model;

public class User { // Class

    private String username; // Encapsulation
    private String password; // Encapsulation

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() { // Encapsulation
        return username;
    }

    public String getPassword() { // Encapsulation
        return password;
    }

    public void setUsername(String username) { // Encapsulation
        this.username = username;
    }

    public void setPassword(String password) { // Encapsulation
        this.password = password;
    }
}