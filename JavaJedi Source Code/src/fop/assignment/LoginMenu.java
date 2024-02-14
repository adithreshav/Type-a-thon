/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fop.assignment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.border.LineBorder;

public class LoginMenu extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private final UserRepository userRepository;
    private Shape shape;

    // Constructor for the LoginMenu class
    public LoginMenu() {
        // Load custom font
        Font font = LoadFont.loadCustomFont("./RobotoMono-VariableFont_wght.ttf", "Roboto Mono", Font.PLAIN, 13);
        
        // Initialize the user repository
        userRepository = UserRepository.getInstance();

        // Set JFrame properties
        setTitle("Type-a-thon");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        setLocationRelativeTo(null);

        // Set background color
        getContentPane().setBackground(Color.decode("#323437"));

        // Set GridBagConstraints insets for spacing
        GridBagConstraints gridbag = new GridBagConstraints();
        gridbag.insets = new Insets(15, 15, 15, 15);

        // Create and add title label
        JLabel titleLabel = new JLabel("Welcome to type-a-thon!", JLabel.CENTER);
        titleLabel.setFont(new Font("Roboto Mono", Font.BOLD, 20));
        titleLabel.setForeground(Color.decode("#E2B714"));
        gridbag.gridx = 0;
        gridbag.gridy = 0;
        gridbag.gridwidth = 2;
        add(titleLabel, gridbag);

        // Create and add subtitle label
        JLabel subTitleLabel = new JLabel("by JavaJedi", JLabel.CENTER);
        subTitleLabel.setFont(new Font("Roboto Mono", Font.PLAIN, 14));
        subTitleLabel.setForeground(Color.decode("#E2B714"));
        gridbag.gridx = 0;
        gridbag.gridy = 1;
        gridbag.gridwidth = 2;
        add(subTitleLabel, gridbag);

        // Create and add username label
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(font);
        usernameLabel.setForeground(Color.white);
        gridbag.gridx = 0;
        gridbag.gridy = 2;
        gridbag.gridwidth = 1;
        add(usernameLabel, gridbag);

        // Create and add password label
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(font);
        passwordLabel.setForeground(Color.white);
        gridbag.gridx = 0;
        gridbag.gridy = 3;
        gridbag.gridwidth = 1;
        add(passwordLabel, gridbag);

        // Create and add username text field
        usernameField = new RoundJTextField(8);
        usernameField.setBackground(Color.decode("#2C2E31"));
        gridbag.gridx = 1;
        gridbag.gridy = 2;
        gridbag.gridwidth = 1;
        gridbag.fill = GridBagConstraints.HORIZONTAL;
        usernameField.setFont(new Font("Roboto Mono", Font.PLAIN, 15));
        usernameField.setForeground(Color.white);
        usernameField.setBorder(null);
        usernameField.setBorder(BorderFactory.createCompoundBorder(usernameField.getBorder(), BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        usernameField.setCaretColor(Color.decode("#E2B714"));
        add(usernameField, gridbag);

        // Create and add password text field
        passwordField = new RoundJPasswordField(8);
        passwordField.setBackground(Color.decode("#2C2E31"));
        gridbag.gridx = 1;
        gridbag.gridy = 3;
        gridbag.gridwidth = 1;
        gridbag.fill = GridBagConstraints.HORIZONTAL;
        passwordField.setForeground(Color.white);
        passwordField.setBorder(null);
        passwordField.setBorder(BorderFactory.createCompoundBorder(usernameField.getBorder(), BorderFactory.createEmptyBorder(3, 3, 3, 3)));
        passwordField.setCaretColor(Color.decode("#E2B714"));
        add(passwordField, gridbag);

        // Create and add login button
        JButton loginButton = new JButton("Login");
        gridbag.gridx = 1;
        gridbag.gridy = 4;
        loginButton.setFont(new Font("Roboto Mono", Font.BOLD, 15));
        loginButton.setForeground(Color.decode("#646669"));
        loginButton.setBackground(Color.decode("#323437"));
        loginButton.setBorderPainted(false);
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setForeground(Color.white);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setForeground(Color.decode("#646669"));
            }
        });
        add(loginButton, gridbag);

        // Set default button for Enter key
        getRootPane().setDefaultButton(loginButton);

        // Add action listener for login button
        loginButton.addActionListener((ActionEvent e) -> {
            login();
        });

        // Create and add register button
        JButton registerButton = new JButton("Register");
        gridbag.gridx = 0;
        gridbag.gridy = 4;
        registerButton.setFont(new Font("Roboto Mono", Font.BOLD, 15));
        registerButton.setForeground(Color.decode("#646669"));
        registerButton.setBackground(Color.decode("#323437"));
        registerButton.setBorderPainted(false);
        registerButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                registerButton.setForeground(Color.white);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                registerButton.setForeground(Color.decode("#646669"));
            }
        });
        add(registerButton, gridbag);

        // Add action listener for register button
        registerButton.addActionListener((ActionEvent e) -> {
            register();
        });
    }

    // Method to handle login logic
    private void login() {
        String username = usernameField.getText();
        char[] passwordChars = passwordField.getPassword();
        String password = new String(passwordChars);

        // Check for empty fields
        if (username.equals("") || password.equals("")) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Notice", JOptionPane.INFORMATION_MESSAGE);
        }

        // Retrieve user from repository
        User currentUser =  userRepository.getUser(username);

        // Check if the password matches
        if (currentUser != null && currentUser.getPassword().equals(password)) {
            // Open the main menu
            MainMenu mainMenu = new MainMenu(currentUser);
            mainMenu.setVisible(true);
            userRepository.setCurrentUser(currentUser);
            JOptionPane.showMessageDialog(this, "Login successful!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to handle registration logic
    private void register(){
        String username = usernameField.getText();
        char[] passwordChars= passwordField.getPassword();
        String password = new String(passwordChars);

        // Check if the username is already taken
        if (usernameUsed(username)){
            JOptionPane.showMessageDialog(this, "Username has already been taken");
            return;
        }

        // Validate username and password
        if (username.equals("")) {
            JOptionPane.showMessageDialog(this, "Username can't be empty", "Error", JOptionPane.ERROR_MESSAGE);
        } else if(password.equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter your password", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            // Create a new user and add to the repository
            User newUser = new User(username, password, new ArrayList<>(), 0, 0, 0.0);
            userRepository.addUser(username, newUser);
            JOptionPane.showMessageDialog(this, "Registration Successful!");
        }
    }

    // Method to check if the username is already in use
    private boolean usernameUsed(String username){
        User user = userRepository.getUser(username);
        return user != null;
    }

    // Main method to launch the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginMenu().setVisible(true);
        });
    }
}
