package model;

public class User {
    private String username;
    private String password;
    private String role;
    private String name;
    private String email;

    public User(String username, String password, String role, String name, String email) {
        this.username = username.trim();
        this.password = password.trim();
        this.role = role.trim();
        this.name = name.trim();
        this.email = email.trim();
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getName() { return name; }
    public String getEmail() { return email; }
}