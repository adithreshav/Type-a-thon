package fop.assignment;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class QuotesGameMode {

    // Variables to track game state
    private int mistake = 0;
    private int typeRightWord = 0;
    private int correctChar;
    static long startTime;
    int currentIndex = 0;

    // Swing components
    JFrame gamemode = new JFrame();
    HashMap<String, Integer> wrongWords;
    boolean[] mistakes;
    String targetText;
    QuotesGenerator quote;
    private JButton newTextButton;
    private JButton repeatTextButton;
    private JEditorPane screenText;

    // Constructor
    public QuotesGameMode() {
        // Load custom font
        Font font = LoadFont.loadCustomFont("C:\\SEM 1\\FOP\\FOP ASSIGNMENT\\src\\fop\\assignment\\RobotoMono-VariableFont_wght.ttf", "Roboto Mono", Font.PLAIN, 24);

        // Initialize game components
        quote = new QuotesGenerator();
        this.wrongWords = UserRepository.getInstance().getCurrentUser().getWrongWords();
        targetText = quote.generateRandomQuotes();
        mistakes = new boolean[targetText.length()];
        startTime = System.currentTimeMillis();

        // Set up JFrame
        gamemode.setSize(600, 670);
        gamemode.setTitle("Quotes Mode");
        gamemode.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gamemode.setLocationRelativeTo(gamemode);
        gamemode.setResizable(false);

        // Create and configure JEditorPane for displaying text
        screenText = new JEditorPane("text/html", " ");
        screenText.setEditable(false);
        screenText.setForeground(Color.green);
        screenText.setFont(new Font("Monospaced", Font.BOLD, 15));
        screenText.setBackground(Color.decode("#323437"));
        screenText.setOpaque(true);
        screenText.setBounds(0, 50, 570, 200);
        screenText.setBorder(new EmptyBorder(20, 0, 0, 0));
        screenText.setSelectionColor(Color.decode("#E2B714"));

        // Create and configure JLabel for displaying title
        JLabel title = new JLabel("Type to start");
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setVerticalAlignment(JLabel.TOP);
        title.setVerticalTextPosition(JLabel.CENTER);
        title.setHorizontalTextPosition(JLabel.CENTER);
        title.setForeground(Color.decode("#E2B714"));
        title.setFont(new Font("Roboto Mono", Font.BOLD, 20));
        title.setBounds(0, 0, 570, 50);
        title.setBorder(new EmptyBorder(10, 0, 0, 0));

        // Create and configure JPanel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.setBounds(0, 300, 570, 50);
        buttonPanel.setBackground(Color.decode("#323437"));

        // Create "New Text" button
        newTextButton = new JButton("New Text");
        newTextButton.setFont(new Font("Roboto Mono", Font.BOLD, 15));
        newTextButton.setForeground(Color.decode("#646669"));
        newTextButton.setBackground(null);
        newTextButton.setBorderPainted(false);

        // ActionListener for "New Text" button
        newTextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                targetText = quote.generateRandomQuotes();
                resetGame();
                screenText.setText(parseHtml(targetText, 0));
                startNewRound();
            }
        });

        // MouseListener for "New Text" button
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

        // Create "Repeat Text" button
        repeatTextButton = new JButton("Repeat Text");
        repeatTextButton.setFont(new Font("Roboto Mono", Font.BOLD, 15));
        repeatTextButton.setForeground(Color.decode("#646669"));
        repeatTextButton.setBackground(null);
        repeatTextButton.setBorderPainted(false);

        // ActionListener for "Repeat Text" button
        repeatTextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetGame();
                screenText.setText(parseHtml(targetText, 0));
                startNewRound();
            }
        });

        // MouseListener for "Repeat Text" button
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

        // Add buttons to the button panel
        buttonPanel.add(newTextButton);
        buttonPanel.add(repeatTextButton);

        // Initialize screen text with parsed HTML
        screenText.setText(parseHtml(targetText, 0));

        // KeyListener for handling key events
        screenText.addKeyListener(new KeyAdapter() {
            // Implementation of keyTyped event
            @Override
            public void keyTyped(KeyEvent e) {
                char typedChar = e.getKeyChar();
                char targetChar = targetText.charAt(currentIndex);

                // Check for space key to skip word
                if (typedChar == ' ') {
                    skipWord();
                    return;
                }

                // Check if typed character matches target character
                if (typedChar == targetChar) {
                    currentIndex++;
                    typeRightWord++;
                    correctChar++;
                    screenText.setText(parseHtml(targetText, currentIndex));
                } else if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
                    // Handle backspace to correct mistakes
                    if (currentIndex > 0) {
                        currentIndex--;
                        screenText.setText(parseHtml(targetText, currentIndex));
                    }
                } else {
                    // Handle incorrect character
                    mistake++;
                    mistakes[currentIndex] = true;
                    currentIndex++;
                    if (currentIndex < targetText.length()) {
                        screenText.setText(parseHtml(targetText, currentIndex));
                        String word = findWordAtIndex(targetText, currentIndex);
                        if (wrongWords.containsKey(word)) {
                            wrongWords.put(word, wrongWords.get(word) + 1);
                        } else {
                            wrongWords.put(word, 1);
                        }
                    }
                }

                // Check if end of text is reached
                if (currentIndex == targetText.length() - 1) {
                    screenText.setText(parseHtml(targetText, targetText.length()));
                    long endTime = System.currentTimeMillis();
                    long timeTaken = endTime - startTime;
                    wpmCalculator(timeTaken, targetText.length());
                }
            }

            // Helper method to skip the current word
            private void skipWord() {
                for (int i = currentIndex; i < getNextWordIndex(targetText, currentIndex); i++) {
                    mistakes[i] = true;
                }
                currentIndex = getNextWordIndex(targetText, currentIndex) - 1;
                if (currentIndex >= targetText.length() - 1) {
                    screenText.setText(parseHtml(targetText, currentIndex));
                    long endTime = System.currentTimeMillis();
                    long timeTaken = endTime - startTime;
                    wpmCalculator(timeTaken, targetText.length());
                }
                if (currentIndex < targetText.length()) {
                    mistakes[currentIndex] = true;
                    currentIndex++;
                    screenText.setText(parseHtml(targetText, currentIndex));
                }
            }

            // Helper method to get the index of the next word
            private int getNextWordIndex(String sentence, int currentIndex) {
                while (currentIndex < sentence.length() && !Character.isWhitespace(sentence.charAt(currentIndex))) {
                    currentIndex++;
                }
                while (currentIndex < sentence.length() && Character.isWhitespace(sentence.charAt(currentIndex))) {
                    currentIndex++;
                }
                return currentIndex;
            }
        });

        // Add components to JFrame
        gamemode.add(screenText);
        gamemode.add(title);
        gamemode.add(buttonPanel);
        gamemode.setLayout(null);
        gamemode.getContentPane().setBackground(Color.decode("#323437"));
        gamemode.setVisible(true);
        gamemode.setFocusable(true);
        screenText.grabFocus();
    }

    // Helper method to reset game state
    private void resetGame() {
        mistake = 0;
        typeRightWord = 0;
        currentIndex = 0;
        correctChar = 0;
        mistakes = new boolean[targetText.length()];
        startTime = System.currentTimeMillis();
        screenText.setText(parseHtml(targetText, 0));
    }

    // Helper method to start a new round
    private void startNewRound() {
        screenText.grabFocus();
    }

    // Helper method to parse HTML for displaying text
    public String parseHtml(String input, int index) {
        StringBuilder html = new StringBuilder("<html><body style='color: #646669; font-family: Roboto Mono; font-size: 13px;'> <span style='color: white; font-size: 13px;'>");

        if (currentIndex == 0) {
            html.append("<span style='color: #E2B714; font-size: 13px; text-decoration: underline;'>").append(input.charAt(0)).append("</span>");
            for (int i = 1; i < input.length() && i < mistakes.length; i++) {
                html.append("<span style='color: #646669; font-size: 13px;'>").append(input.charAt(i)).append("</span>");
            }
        } else {
            for (int i = 0; i < input.length() && i < mistakes.length; i++) {
                if (i == index) {
                    html.append("<span style='color: #E2B714; font-size: 13px; text-decoration: underline;'>").append(input.charAt(i)).append("</span>");
                } else if (mistakes[i]) {
                    html.append("<span style='color: red; font-size: 13px;'>").append(input.charAt(i)).append("</span>");
                } else {
                    html.append(input.charAt(i));
                }

                if (i == index - 1) {
                    html.append("</span>");
                }
            }
        }

        html.append("</body></html>");
        return html.toString();
    }

    // Helper method to find the word at a given index in a sentence
    public static String findWordAtIndex(String sentence, int index) {
        String[] words = sentence.split("\\s+");
        int currentPosition = 0;
        String currentWord = null;

        for (String word : words) {
            currentPosition += word.length() + 1;

            if (index < currentPosition) {
                currentWord = word;
                break;
            }
        }

        return currentWord;
    }

    // Helper method to calculate words per minute (WPM) and accuracy
    public void wpmCalculator(long timeTaken, double word) {
        HashSet<String> wordSet = new HashSet<>(Arrays.asList(targetText.split("\\s+")));

        for (int i = 0; i < mistakes.length; i++) {
            if (mistakes[i]) {
                wordSet.remove(findWordAtIndex(targetText, i));
            }
        }

        double minutes = timeTaken / 60000.0;
        double wpmInitial = (correctChar / 5) / minutes;
        int wpm = (int) wpmInitial;

        double accuracy = (typeRightWord / (double) (typeRightWord + mistake)) * 100;

        String message = "Words Per Minute (WPM): " + wpm + "\n"
                + "Mistakes: " + mistake + "\n"
                + String.format("Accuracy: %.2f%%\n", accuracy)
                + "This quote was from the " + quote.getQuoteSource();

        UserRepository.getInstance().getCurrentUser().getWPM().add(wpm);
        UserRepository.getInstance().getCurrentUser().getAccuracy().add(accuracy);
        UserRepository.getInstance().saveDataToFile();

        AWTEventListener myListener = (AWTEvent event) -> {
            if (event instanceof KeyEvent) {
                ((KeyEvent) event).consume();
            }
        };
        Toolkit.getDefaultToolkit().addAWTEventListener(myListener, AWTEvent.KEY_EVENT_MASK);

        JOptionPane.showMessageDialog(null, message, "WPM and Accuracy", JOptionPane.INFORMATION_MESSAGE);

        Toolkit.getDefaultToolkit().removeAWTEventListener(myListener);
    }
}
