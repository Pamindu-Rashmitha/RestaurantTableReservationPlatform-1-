package model;

public class User {
    private String username;
    private String password;
    private String role; // "customer" or "admin"
    private String name;
    private String email;

    // Constructor
    public User(String username, String password, String role, String name, String email) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.name = name;
        this.email = email;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}