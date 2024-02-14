package fop.assignment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameModeSelect {

    // JFrame to hold the buttons for game mode selection
    JFrame buttonMode;

    // Constructor to initialize the frame and add buttons
    public GameModeSelect() {
        initializeFrame();
        addButtons();
    }

    // Method to initialize the JFrame
    private void initializeFrame() {
        buttonMode = new JFrame();
        buttonMode.setSize(570, 670);
        buttonMode.setTitle("Game mode");
        buttonMode.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        buttonMode.getContentPane().setBackground(Color.DARK_GRAY);
        buttonMode.setLocationRelativeTo(null);
    }

    // Method to add buttons to the JFrame
    private void addButtons() {
        JPanel panel = new JPanel(new GridLayout(6, 1));

        // Adding buttons with corresponding action listeners
        addButton(panel, "Classic", e -> new ClassicGameMode());
        addButton(panel, "Quote", e -> new QuotesGameMode());
        addButton(panel, "Words", e -> new WordsGameOptionsDialog());
        addButton(panel, "Timed", e -> new TimedGameOptionsDialog());
        addButton(panel, "Sudden Death", e -> new SuddenDeathGameMode(100));
        addButton(panel, "Correction Facility", e -> new CorrectionFacilityGameMode());
        
        // Adding the panel with buttons to the main frame
        buttonMode.add(panel);
    }

    // Method to add a button to a panel
    private void addButton(JPanel panel, String buttonText, ActionListener listener) {
        JButton button = new JButton(buttonText);
        setButtonProperties(button);
        button.addActionListener(listener);
        panel.add(button);
        button.setFocusable(false);
    }

    // Method to set properties for a button (font, color, etc.)
    private void setButtonProperties(JButton button) {
        Font font = LoadFont.loadCustomFont("C:\\SEM 1\\FOP\\FOP ASSIGNMENT\\src\\fop\\assignment\\RobotoMono-VariableFont_wght.ttf", "Roboto Mono", Font.PLAIN, 24);
        button.setFont(new Font("Roboto Mono", Font.BOLD, 15));
        button.setForeground(Color.decode("#646669"));
        button.setBackground(Color.decode("#323437"));
        button.setBorderPainted(false);

        // Adding mouse listeners to change text color on hover
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
