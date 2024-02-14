package fop.assignment;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class WordsGameOptionsDialog {
    // Constants representing different game modes
    protected static int mode1 = 10;
    protected static int mode2 = 25;
    protected static int mode3 = 50;
    protected static int mode4 = 100;

    private JFrame buttonmode;

    public WordsGameOptionsDialog() {
        Font font = LoadFont.loadCustomFont("C:\\SEM 1\\FOP\\FOP ASSIGNMENT\\src\\fop\\assignment\\RobotoMono-VariableFont_wght.ttf", "Roboto Mono", Font.PLAIN, 24);
        // Initialize the game mode options dialog
        buttonmode = new JFrame();
        buttonmode.setSize(570, 670);
        buttonmode.setTitle("Word Count Selection");
        buttonmode.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        buttonmode.setBackground(Color.black);
        
        // Create a panel with a grid layout to organize buttons
        JPanel panel = new JPanel(new GridLayout(4, 1));

        // Button for the first game mode (10 words)
        JButton words10 = new JButton("10");
        initializeButton(words10, 10);

        // Add action listener to start the game mode with 10 words
        words10.addActionListener((ActionEvent e) -> {
            WordsGameMode words = new WordsGameMode(10);
            buttonmode.dispose();
        });

        // Button for the second game mode (25 words)
        JButton words25 = new JButton("25");
        initializeButton(words25, 25);

        // Add action listener to start the game mode with 25 words
        words25.addActionListener((ActionEvent e) -> {
            WordsGameMode words = new WordsGameMode(25);
            buttonmode.dispose();
        });

        // Button for the third game mode (50 words)
        JButton words50 = new JButton("50");
        initializeButton(words50, 50);

        // Add action listener to start the game mode with 50 words
        words50.addActionListener((ActionEvent e) -> {
            WordsGameMode words = new WordsGameMode(50);
            buttonmode.dispose();
        });

        // Button for the fourth game mode (100 words)
        JButton words100 = new JButton("100");
        initializeButton(words100, 100);

        // Add action listener to start the game mode with 100 words
        words100.addActionListener((ActionEvent e) -> {
            WordsGameMode words = new WordsGameMode(100);
            buttonmode.dispose();
        });

        // Add buttons to the panel
        panel.add(words10);
        panel.add(words25);
        panel.add(words50);
        panel.add(words100);

        // Configure the appearance of the dialog
        buttonmode.setLocationRelativeTo(null);
        buttonmode.add(panel);
        buttonmode.getContentPane().setBackground(Color.DARK_GRAY);
        buttonmode.setVisible(true);
        
        words10.setFocusable(false);
        words25.setFocusable(false);
        words50.setFocusable(false);
        words100.setFocusable(false);
    }

    // Helper method to initialize the appearance of buttons
    private void initializeButton(JButton button, int mode) {
        button.setFont(new Font("Roboto Mono", Font.BOLD, 15));
        button.setForeground(Color.decode("#646669"));
        button.setBackground(Color.decode("#323437"));
        button.setBorderPainted(false);
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setForeground(Color.white);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setForeground(Color.decode("#646669"));
            }
        });
    }
}

