package fop.assignment;

import java.io.*;
import java.util.*;

public class UserRepository {
    // File path for storing user data
    private static final String FILE_PATH = "C:\\SEM 1\\FOP\\FOP ASSIGNMENT\\src\\fop\\assignment\\userRepository.txt";
    
    // Singleton instance of the UserRepository
    private static UserRepository instance;
    
    // Map to store user data with usernames as keys
    private Map<String, User> users;
    
    // Current user logged in
    private static User currentUser;

    // Private constructor to prevent instantiation
    private UserRepository() {
        users = new HashMap<>();
        loadDataFromFile();
    }

    // Get the singleton instance of UserRepository
    public static synchronized UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    // Get the currently logged-in user
    public User getCurrentUser() {
        return currentUser;
    }

    // Set the currently logged-in user
    public void setCurrentUser(User user) {
        currentUser = user;
    }

    // Get user data based on username
    public User getUser(String username) {
        return users.get(username);
    }

    // Add a new user to the repository
    public void addUser(String username, User userData) {
        users.put(username, userData);
        saveDataToFile();
    }

    // Load user data from the file into the repository
    private void loadDataFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            users = (HashMap<String, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // Handle exceptions (e.g., file not found, class not found, etc.)
            // Initialize the users Map if the file is not present or empty
            users = new HashMap<>();
        }
    }

    // Save user data from the repository to the file
    public void saveDataToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(users);
            System.out.println("Saved");
        } catch (IOException e) {
            // Handle exceptions (e.g., file not found, etc.)
        }
    }

    // Get a collection of all users in the repository
    public Collection<User> getAllUser() {
        return users.values();
    }
}