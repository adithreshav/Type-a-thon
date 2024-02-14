package fop.assignment;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class TimedGameOptionsDialog {
    private JCheckBox punctuationCheckBox;
    private final JRadioButton timer15Button;
    private final JRadioButton timer30Button;
    private final JRadioButton timer45Button;
    private final JRadioButton timer60Button;
    private final JFrame options;

    // Constructor for TimedGameOptionsDialog
    public TimedGameOptionsDialog() {
        Font font = LoadFont.loadCustomFont("C:\\SEM 1\\FOP\\FOP ASSIGNMENT\\src\\fop\\assignment\\RobotoMono-VariableFont_wght.ttf", "Roboto Mono", Font.PLAIN, 24);
        // Set up JFrame for options dialog
        options = new JFrame();
        options.setSize(300, 250);
        options.setTitle("Game Options");
        options.setLocationRelativeTo(null);
        options.getContentPane().setBackground(Color.decode("#323437"));

        // Set up checkbox for including punctuation
        punctuationCheckBox = new JCheckBox("Include Punctuation");
        punctuationCheckBox.setBounds(50, 20, 300, 30);
        punctuationCheckBox.setFont(new Font("Monospaced", Font.BOLD, 15));
        punctuationCheckBox.setForeground(Color.yellow);
        punctuationCheckBox.setBackground(Color.decode("#323437"));
        punctuationCheckBox.setSelected(TimedGameMode.includePunctuation());
        punctuationCheckBox.setFont(new Font("Roboto Mono", Font.BOLD, 15));
        punctuationCheckBox.setForeground(Color.decode("#646669"));
        punctuationCheckBox.setBackground(Color.decode("#323437"));
        punctuationCheckBox.setBorderPainted(false);

        // Add ActionListener to handle changes in punctuation checkbox state
        punctuationCheckBox.addActionListener((ActionEvent e) -> {
            TimedGameMode.setIncludePunctuation(punctuationCheckBox.isSelected());
            if(punctuationCheckBox.isSelected()){
                punctuationCheckBox.setForeground(Color.white);
            }else{
                punctuationCheckBox.setForeground(Color.decode("#646669"));
            }
        });

        // Set up radio buttons for timer durations
        timer15Button = new JRadioButton("15 seconds");
        timer15Button.setFont(new Font("Roboto Mono", Font.BOLD, 15));
        timer15Button.setForeground(Color.decode("#646669"));
        timer15Button.setBackground(Color.decode("#323437"));
        
        timer15Button.addActionListener((ActionEvent e) -> {
            if(timer15Button.isSelected()){
                timer15Button.setForeground(Color.white);
            }else{
                timer15Button.setForeground(Color.decode("#646669"));
            }
        });
        
        timer30Button = new JRadioButton("30 seconds");
        timer30Button.setFont(new Font("Roboto Mono", Font.BOLD, 15));
        timer30Button.setForeground(Color.decode("#646669"));
        timer30Button.setBackground(Color.decode("#323437"));
        
        timer30Button.addActionListener((ActionEvent e) -> {
            if(timer30Button.isSelected()){
                timer30Button.setForeground(Color.white);
            }else{
                timer30Button.setForeground(Color.decode("#646669"));
            }
        });
        
        timer45Button = new JRadioButton("45 seconds");
        timer45Button.setFont(new Font("Roboto Mono", Font.BOLD, 15));
        timer45Button.setForeground(Color.decode("#646669"));
        timer45Button.setBackground(Color.decode("#323437"));
        
        timer45Button.addActionListener((ActionEvent e) -> {
            if(timer45Button.isSelected()){
                timer45Button.setForeground(Color.white);
            }else{
                timer45Button.setForeground(Color.decode("#646669"));
            }
        });
        
        timer60Button = new JRadioButton("60 seconds");
        timer60Button.setFont(new Font("Roboto Mono", Font.BOLD, 15));
        timer60Button.setForeground(Color.decode("#646669"));
        timer60Button.setBackground(Color.decode("#323437"));
        
        timer60Button.addActionListener((ActionEvent e) -> {
            if(!timer60Button.isSelected()){
                timer60Button.setForeground(Color.decode("#646669"));
            }else{
                timer60Button.setForeground(Color.white);
            }
        });

        // Set up button group for radio buttons
        ButtonGroup group = new ButtonGroup();

        timer15Button.setBounds(50, 50, 200, 30);
        timer30Button.setBounds(50, 80, 200, 30);
        timer45Button.setBounds(50, 110, 200, 30);
        timer60Button.setBounds(50, 140, 200, 30);

        group.add(timer15Button);
        group.add(timer30Button);
        group.add(timer45Button);
        group.add(timer60Button);

        // Set up OK button to apply selected options
        JButton okButton = new JButton("OK");
        okButton.setBounds(120, 180, 60, 30);
        okButton.setFont(new Font("Roboto Mono", Font.BOLD, 15));
        okButton.setForeground(Color.decode("#646669"));
        okButton.setBackground(Color.decode("#323437"));
        okButton.setBorderPainted(false);
        okButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                okButton.setForeground(Color.white);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                okButton.setForeground(Color.decode("#646669"));
            }
        });

        // Add ActionListener to handle OK button click
        okButton.addActionListener((ActionEvent e) -> {
            int selectedDuration = getSelectedDuration();
            if (selectedDuration != -1) {
                // Apply selected options and start the game
                setTimerDuration(selectedDuration, punctuationCheckBox.isSelected());
            } else {
                JOptionPane.showMessageDialog(null, "Please select a timer duration");
            }
        });

        // Add components to the JFrame
        options.add(punctuationCheckBox);
        options.add(timer15Button);
        options.add(timer30Button);
        options.add(timer45Button);
        options.add(timer60Button);
        options.add(okButton);

        options.setLayout(null);
        options.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        options.setVisible(true);
        
        punctuationCheckBox.setFocusable(false);
        timer15Button.setFocusable(false);
        timer30Button.setFocusable(false);
        timer45Button.setFocusable(false);
        timer60Button.setFocusable(false);
        okButton.setFocusable(false);
    }

    // Method to get the selected timer duration
    private int getSelectedDuration() {
        if (timer15Button.isSelected()) {
            return 15;
        } else if (timer30Button.isSelected()) {
            return 30;
        } else if (timer45Button.isSelected()) {
            return 45;
        } else if (timer60Button.isSelected()) {
            return 60;
        }
        return -1; // No option selected
    }

    // Method to set the timer duration and start the game
    private void setTimerDuration(int seconds, boolean punctuation) {
        // Create an instance of TimedGameMode with the selected options
        TimedGameMode timedGameMode = new TimedGameMode(seconds, punctuation);
        options.dispose(); // Close the Game Options dialog
    }
}
