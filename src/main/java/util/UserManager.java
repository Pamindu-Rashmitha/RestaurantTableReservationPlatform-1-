package util;

import model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserManager {

    // Read all users from the file
    public List<User> getAllUsers(String filePath) {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    User user = new User(parts[0], parts[1], parts[2], parts[3], parts[4]);
                    users.add(user);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    // Add a new user (Create)
    public boolean addUser(User user, String filePath) {
        List<User> users = getAllUsers(filePath);
        // Check for duplicate username
        for (User u : users) {
            if (u.getUsername().equals(user.getUsername())) {
                return false; // Username exists
            }
        }
        users.add(user);
        saveUsers(users, filePath);
        return true;
    }

    // Get user by username (Read for login)
    public User getUserByUsername(String username, String filePath) {
        List<User> users = getAllUsers(filePath);
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null; // User not found
    }

    public boolean removeUser(String username, String filePath) {
        List<User> users = getAllUsers(filePath);
        boolean removed = users.removeIf(user -> user.getUsername().equals(username));
        if (removed) {
            saveUsers(users, filePath);
        }
        return removed;
    }

    // Save all users to file (Helper method)
    private void saveUsers(List<User> users, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (User user : users) {
                writer.write(user.getUsername() + "," + user.getPassword() + "," +
                        user.getRole() + "," + user.getName() + "," + user.getEmail());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}