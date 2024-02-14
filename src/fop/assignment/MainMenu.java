package fop.assignment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;

public class MainMenu extends JFrame {
    
    private final User currentUser;
    
    // Constructor for the MainMenu class
    public MainMenu(User user){
        Font font = LoadFont.loadCustomFont("C:\\SEM 1\\FOP\\FOP ASSIGNMENT\\src\\fop\\assignment\\RobotoMono-VariableFont_wght.ttf", "Roboto Mono", Font.PLAIN, 24);
        // Initialize the current user
        this.currentUser = user;
       
        // Set frame properties
        setLayout(new GridBagLayout());
        GridBagConstraints gridbag = new GridBagConstraints();
        gridbag.insets = new Insets(15, 0, 15, 0);
        setTitle("Type-a-thon");
        setSize(800,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        getContentPane().setBackground(Color.decode("#323437"));
        
        // Welcome Label
        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getUsername()+"!",JLabel.CENTER);
        welcomeLabel.setFont(new Font("Roboto Mono", Font.BOLD, 20));
        welcomeLabel.setForeground(Color.decode("#E2B714"));
        welcomeLabel.setBorder(new EmptyBorder(0,265,0,0));
        gridbag.gridx = 0;
        gridbag.gridy = 0;
        gridbag.gridwidth = 2;
        add(welcomeLabel, gridbag);
        
        // User Statistics Labels
        JLabel lastTenWPMLabel = new JLabel("Last 10 average WPM: " + currentUser.getLastTenWpmAverage());
        JLabel allTimeAverageWPMLabel = new JLabel(String.format("All-time Average WPM: %.2f", currentUser.getAllTimeAverageWPM()));
        JLabel suddenDeathScoreLabel = new JLabel("Sudden Death Score: " + currentUser.getSuddenDeathScore());
        
        lastTenWPMLabel.setFont(new Font("Roboto Mono", Font.BOLD, 14));
        lastTenWPMLabel.setForeground(Color.white);
        lastTenWPMLabel.setBorder(new EmptyBorder(0,265,0,0));
        
        allTimeAverageWPMLabel.setFont(new Font("Roboto Mono", Font.BOLD, 14));
        allTimeAverageWPMLabel.setForeground(Color.white);
        allTimeAverageWPMLabel.setBorder(new EmptyBorder(0,265,0,0));
        
        suddenDeathScoreLabel.setFont(new Font("Roboto Mono", Font.BOLD, 14));
        suddenDeathScoreLabel.setForeground(Color.white);
        suddenDeathScoreLabel.setBorder(new EmptyBorder(0,265,0,0));
        
        gridbag.gridx = 0;
        gridbag.gridy = 1;
        gridbag.gridwidth = 2;
        add(lastTenWPMLabel, gridbag);

        gridbag.gridx = 0;
        gridbag.gridy = 2;
        gridbag.gridwidth = 2;
        add(allTimeAverageWPMLabel, gridbag);

        gridbag.gridx = 0;
        gridbag.gridy = 3;
        gridbag.gridwidth = 2;
        add(suddenDeathScoreLabel, gridbag);

        // Play Button
        JButton playButton = new JButton("Play");
        
        playButton.setBorder(new EmptyBorder(0,265,0,0));
        playButton.setFont(new Font("Roboto Mono", Font.BOLD, 15));
        playButton.setForeground(Color.decode("#646669"));
        playButton.setBackground(Color.decode("#323437"));
        playButton.setBorderPainted(false);
        playButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                playButton.setForeground(Color.white);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                playButton.setForeground(Color.decode("#646669"));
            }
        });
        
        gridbag.gridx = 0;
        gridbag.gridy = 4;
        gridbag.gridwidth = 2;
        add(playButton, gridbag);
    
        // Action listener for the Play Button
        playButton.addActionListener((ActionEvent e) -> {
             showTypingGame();
        });
    
        // User Profiles Button
        JButton userProfileButton = new JButton("User profiles");
        userProfileButton.setBorder(new EmptyBorder(0,265,0,0));
        userProfileButton.setFont(new Font("Roboto Mono", Font.BOLD, 15));
        userProfileButton.setForeground(Color.decode("#646669"));
        userProfileButton.setBackground(Color.decode("#323437"));
        userProfileButton.setBorderPainted(false);
        userProfileButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                userProfileButton.setForeground(Color.white);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                userProfileButton.setForeground(Color.decode("#646669"));
            }
        });

        gridbag.gridx = 0;
        gridbag.gridy = 5;
        gridbag.gridwidth = 2;
        add(userProfileButton, gridbag);
        
        // Action listener for the User Profiles Button
        userProfileButton.addActionListener((ActionEvent e) -> {
            showUserProfile();
        });
    
        // Leaderboard Button
        JButton leaderboardButton = new JButton("Leaderboard");
        leaderboardButton.setFont(new Font("Roboto Mono", Font.BOLD, 15));
        leaderboardButton.setForeground(Color.decode("#646669"));
        leaderboardButton.setBackground(Color.decode("#323437"));
        leaderboardButton.setBorderPainted(false);
        leaderboardButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                leaderboardButton.setForeground(Color.white);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                leaderboardButton.setForeground(Color.decode("#646669"));
            }
        });

        gridbag.gridx = 0;
        gridbag.gridy = 7;
        gridbag.gridwidth = 1;
        add(leaderboardButton, gridbag);
        
        // Action listener for the Leaderboard Button
        leaderboardButton.addActionListener((ActionEvent e) -> {
            showLeaderboard();
        });
        
        // All-time Average Leaderboard Button
        JButton allTimeLeaderboardButton = new JButton("All-time average Leaderboard");
        allTimeLeaderboardButton.setFont(new Font("Roboto Mono", Font.BOLD, 15));
        allTimeLeaderboardButton.setForeground(Color.decode("#646669"));
        allTimeLeaderboardButton.setBackground(Color.decode("#323437"));
        allTimeLeaderboardButton.setBorderPainted(false);
        allTimeLeaderboardButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                allTimeLeaderboardButton.setForeground(Color.white);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                allTimeLeaderboardButton.setForeground(Color.decode("#646669"));
            }
        });

        gridbag.gridx = 1;
        gridbag.gridy = 7;
        gridbag.gridwidth = 1;
        add(allTimeLeaderboardButton, gridbag);
        
        // Action listener for the All-time Average Leaderboard Button
        allTimeLeaderboardButton.addActionListener((ActionEvent e) -> {
            showAllTimeLeaderboard();
        });
        
        // Sudden Death Leaderboard Button
        JButton SuddenDeathLeaderboardButton = new JButton("Sudden Death Leaderboard");
        SuddenDeathLeaderboardButton.setFont(new Font("Roboto Mono", Font.BOLD, 15));
        SuddenDeathLeaderboardButton.setForeground(Color.decode("#646669"));
        SuddenDeathLeaderboardButton.setBackground(Color.decode("#323437"));
        SuddenDeathLeaderboardButton.setBorderPainted(false);
        SuddenDeathLeaderboardButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                SuddenDeathLeaderboardButton.setForeground(Color.white);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                SuddenDeathLeaderboardButton.setForeground(Color.decode("#646669"));
            }
        });

        gridbag.gridx = 2;
        gridbag.gridy = 7;
        gridbag.gridwidth = 1;
        add(SuddenDeathLeaderboardButton, gridbag);
        
        // Action listener for the Sudden Death Leaderboard Button
        SuddenDeathLeaderboardButton.addActionListener((ActionEvent e) -> {
            showSuddenDeathLeaderboard();
        });
        
        playButton.setFocusable(false);
        userProfileButton.setFocusable(false);
        leaderboardButton.setFocusable(false);
        allTimeLeaderboardButton.setFocusable(false);
        SuddenDeathLeaderboardButton.setFocusable(false);
    }   
    
    // Method to show Sudden Death Leaderboard
    private void showSuddenDeathLeaderboard(){
        LeaderboardSuddenDeath leaderboard = new LeaderboardSuddenDeath();
        leaderboard.showSuddenDeathLeaderboard();
    } 
    
    // Method to show Regular Leaderboard
    private void showLeaderboard(){
        Leaderboard leaderboard = new Leaderboard();
        leaderboard.showLeaderboard();
    }
    
    // Method to show All-time Average Leaderboard
    private void showAllTimeLeaderboard(){
        LeaderboardAllTimeAvg leaderboard = new LeaderboardAllTimeAvg();
        leaderboard.showAllTimeLeaderboard();
    }
    
    // Method to show User Profiles
    private void showUserProfile(){
        UserProfiles userProfile = new UserProfiles();
        userProfile.showUserProfile();
    }
    
    // Method to show Typing Game
    private void showTypingGame(){
        GameModeSelect buttonSet = new GameModeSelect();
        buttonSet.buttonMode.setVisible(true);
    }
}