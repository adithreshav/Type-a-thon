package fop.assignment;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class SuddenDeathGameMode {
    // Instance variables
    private int typeRightWord = 0;
    public static int wordCount;
    static long startTime;
    int currentIndex = 0;
    HashMap<String, Integer> wrongWords;
    private final JFrame gamemode;
    private JButton newTextButton;
    private JButton repeatTextButton;
    private JEditorPane screenText;
    String targetText;

    // Constructor for SuddenDeathGameMode
    public SuddenDeathGameMode(int words) {
        Font font = LoadFont.loadCustomFont("C:\\SEM 1\\FOP\\FOP ASSIGNMENT\\src\\fop\\assignment\\RobotoMono-VariableFont_wght.ttf", "Roboto Mono", Font.PLAIN, 24);
        // Initialize instance variables
        this.wrongWords = UserRepository.getInstance().getCurrentUser().getWrongWords();
        this.wordCount = words;
        targetText = wordPool(wordCount);
        startTime = System.currentTimeMillis();
        gamemode = new JFrame();

        // Set up JFrame
        gamemode.setSize(600, 670);
        gamemode.setTitle("Sudden Death Mode");
        gamemode.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gamemode.setBackground(Color.black);
        gamemode.setLocationRelativeTo(null);
        gamemode.setResizable(false);

        // Set up JEditorPane to display text
        screenText = new JEditorPane("text/html", " ");
        screenText.setEditable(false);
        screenText.setForeground(Color.green);
        screenText.setFont(new Font("Roboto Mono", Font.BOLD, 15));
        screenText.setBackground(Color.decode("#323437"));
        screenText.setOpaque(true);
        screenText.setBounds(0, 50, 570, 200);
        screenText.setSelectionColor(Color.decode("#E2B714"));

        // Set up JLabel for title
        JLabel title = new JLabel("Type to start");
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setVerticalAlignment(JLabel.TOP);
        title.setVerticalTextPosition(JLabel.CENTER);
        title.setHorizontalTextPosition(JLabel.CENTER);
        title.setForeground(Color.decode("#E2B714"));
        title.setFont(new Font("Roboto Mono", Font.BOLD, 20));
        title.setBounds(0, 0, 570, 50);
        title.setBorder(new EmptyBorder(10,0,0,0));
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.setBounds(0, 300, 570, 50);
        buttonPanel.setBackground(Color.decode("#323437"));

        newTextButton = new JButton("New Text");
        newTextButton.setFont(new Font("Roboto Mono", Font.BOLD, 15));
        newTextButton.setForeground(Color.decode("#646669"));
        newTextButton.setBackground(null);
        newTextButton.setBorderPainted(false);
        
        newTextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Reset the game and start a new round with a new text
                resetGame();
                targetText = wordPool(wordCount);
                screenText.setText(parseHtml(targetText, 0));
                startNewRound();
            }
        });
        
        newTextButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                newTextButton.setForeground(Color.white);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                newTextButton.setForeground(Color.decode("#646669"));
            }
        });

        // Create the Repeat Text button
        repeatTextButton = new JButton("Repeat Text");
        repeatTextButton.setFont(new Font("Roboto Mono", Font.BOLD, 15));
        repeatTextButton.setForeground(Color.decode("#646669"));
        repeatTextButton.setBackground(null);
        repeatTextButton.setBorderPainted(false);
        
        repeatTextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Reset the game and start a new round with the same text
                resetGame();
                screenText.setText(parseHtml(targetText, 0));
                startNewRound();
            }
        });
        
        repeatTextButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                repeatTextButton.setForeground(Color.white);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                repeatTextButton.setForeground(Color.decode("#646669"));
            }
        });

        buttonPanel.add(newTextButton);
        buttonPanel.add(repeatTextButton);
        
        screenText.setText(parseHtml(targetText, 0));

        // Add KeyListener to JEditorPane for typing interaction
        screenText.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                char typedChar = e.getKeyChar();
                char targetChar = targetText.charAt(currentIndex);

                // Check if the typed character matches the target character
                if (typedChar == targetChar || Character.isWhitespace(targetChar)) {
                    currentIndex++;
                    typeRightWord++;
                    screenText.setText(parseHtml(targetText, currentIndex));
                } else {
                    // Handle wrong key press
                    String wrongWord = findWordAtIndex(targetText, currentIndex);
                    if (wrongWords.containsKey(wrongWord)) {
                        wrongWords.put(wrongWord, wrongWords.get(wrongWord) + 1);
                    } else {
                        wrongWords.put(wrongWord, 1);
                    }
                    stopGame();  // Stop the game on wrong key press
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        // Add components to the JFrame
        gamemode.add(screenText);
        gamemode.add(title);
        gamemode.add(buttonPanel);
        gamemode.setLayout(null);
        gamemode.getContentPane().setBackground(Color.decode("#323437"));
        gamemode.setVisible(true);
        gamemode.setFocusable(true);
        screenText.grabFocus();  // Set focus to JEditorPane for typing
    }
    
    private void resetGame() {
        typeRightWord = 0;
        currentIndex = 0;
        startTime = System.currentTimeMillis();
        screenText.setText(parseHtml(targetText, 0));
    }
    
    private void startNewRound() {
        screenText.grabFocus();
    }

    // Method to format text in HTML for display
    // Method to format text in HTML for display with cursor indicator
public String parseHtml(String input, int index) {
    StringBuilder html = new StringBuilder("<html><body style='color: #646669; font-family: Roboto Mono; font-size: 13px;'> <span style='color: white; font-size: 13px;'>");

    for (int i = 0; i < input.length(); i++) {
        if (i == index) {
            html.append("<span style='color: #E2B714; font-size: 13px; text-decoration: underline;'>").append(input.charAt(i)).append("</span>");
        } else if (i<=index) {
            html.append("<span style='color: white; font-size: 13px;'>").append(input.charAt(i)).append("</span>");
        }
        if(i>index){
            html.append("<span style='color: #646669; font-size: 13px;'>").append(input.charAt(i)).append("</span>");
        }
    }

    html.append("</body></html>");
    return html.toString();
}


    // Method to stop the game and display the result
    private void stopGame() {
        long endTime = System.currentTimeMillis();
        long timeTaken = endTime - startTime;
        double minutes = timeTaken / 60000.0;
        int suddenDeathScore = (int) Math.round((typeRightWord / 5) / minutes);

        // Update user data with the sudden death score
        UserRepository.getInstance().getCurrentUser().setSuddenDeathScore(suddenDeathScore);
        UserRepository.getInstance().saveDataToFile();
        
        // Add an AWTEventListener to consume key events
        AWTEventListener myListener = (AWTEvent event) -> {
            if (event instanceof KeyEvent) {
                ((KeyEvent) event).consume();
            }
        };
        Toolkit.getDefaultToolkit().addAWTEventListener(myListener, AWTEvent.KEY_EVENT_MASK);

        // Display game over message
        JOptionPane.showMessageDialog(null,
                "Game over! You pressed a wrong key.\nSudden Death Score: " + suddenDeathScore);

        // Remove the AWTEventListener and close the JFrame
        Toolkit.getDefaultToolkit().removeAWTEventListener(myListener);

        // Save user data
        UserRepository.getInstance().saveDataToFile();
    }

    // Method to find the word at a specific index in a sentence
    public static String findWordAtIndex(String sentence, int index) {
        String[] words = sentence.split("\\s+");
        int currentPosition = 0;
        String currentWord = null;

        for (String word : words) {
            currentPosition += word.length();
            if (index < currentPosition) {
                currentWord = word;
                break;
            }
            currentPosition++;
        }

        return currentWord;
    }

    // Method to generate a word pool based on the specified length
    public static String wordPool(int length) {
        return ParagraphGenerator.generateRandomSentence(length);
    }
}

