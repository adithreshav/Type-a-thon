package fop.assignment;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

public class CorrectionFacilityGameMode {

    // Game statistics variables
    private int mistake = 0;
    private int typeRightWord = 0;
    public static int wordCount;
    static long startTime;
    String targetText;
    int currentIndex = 0;
    JFrame gamemode = new JFrame();
    HashMap<String, Integer> wrongWords;
    boolean[] mistakes;
    private int correctChar;
    private final Timer timer;
    private JLabel timerLabel;
    private JEditorPane screenText;
    private JButton newTextButton;
    private JButton repeatTextButton;
    private int secondsRemaining = 30;

    // Constructor for CorrectionFacilityGameMode
    public CorrectionFacilityGameMode() {
        Font font = LoadFont.loadCustomFont("./RobotoMono-VariableFont_wght.ttf", "Roboto Mono", Font.PLAIN, 24);
        // Initialize game parameters
        this.wrongWords = UserRepository.getInstance().getCurrentUser().getWrongWords();
        targetText = getMostMisspelledWords();
        mistakes = new boolean[targetText.length()];
        startTime = System.currentTimeMillis();
        gamemode.setSize(600, 670);
        gamemode.setTitle("Correction Facility");
        gamemode.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gamemode.setBackground(Color.black);
        gamemode.setLocationRelativeTo(null);
        gamemode.setResizable(false);

        screenText = new JEditorPane("text/html", " ");
        screenText.setEditable(false);
        screenText.setForeground(Color.green);
        screenText.setFont(new Font("Roboto Mono", Font.BOLD, 15));
        screenText.setBackground(Color.decode("#323437"));
        screenText.setOpaque(true);
        screenText.setBounds(0, 50, 570, 200);
        screenText.setSelectionColor(Color.decode("#E2B714"));

        JLabel title = new JLabel("Type to start");
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setVerticalAlignment(JLabel.TOP);
        title.setVerticalTextPosition(JLabel.CENTER);
        title.setHorizontalTextPosition(JLabel.CENTER);
        title.setForeground(Color.decode("#E2B714"));
        title.setFont(new Font("Roboto Mono", Font.BOLD, 20));
        title.setBounds(0, 0, 570, 50);
        title.setBorder(new EmptyBorder(10,0,0,0));

        timerLabel = new JLabel("30");
        timerLabel.setHorizontalAlignment(JLabel.CENTER);
        timerLabel.setVerticalAlignment(JLabel.TOP);
        timerLabel.setVerticalTextPosition(JLabel.CENTER);
        timerLabel.setHorizontalTextPosition(JLabel.CENTER);
        timerLabel.setForeground(Color.decode("#E2B714"));
        timerLabel.setFont(new Font("Roboto Mono", Font.BOLD, 15));
        timerLabel.setBounds(0, 250, 570, 50);

        // Timer setup
        timer = new Timer(1000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // Timer action - decrease remaining time
                if (secondsRemaining > 0) {
                    secondsRemaining--;
                    timerLabel.setText(Integer.toString(secondsRemaining));
                } else {
                    // Stop the timer and calculate statistics when time runs out
                    stopTimer();
                    long endTime = System.currentTimeMillis();
                    long timeTaken = endTime - startTime;
                    wpmCalculator(timeTaken, wordCount);
                    UserRepository.getInstance().saveDataToFile();
                }
            }
        });
        
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
                targetText = getMostMisspelledWords();
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

        // Set initial text in the JEditorPane
        screenText.setText(parseHtml(targetText, 0));

        // KeyListener for handling user input
        screenText.addKeyListener(new KeyAdapter() {
    // ... (other methods)

            @Override
            public void keyTyped(KeyEvent e) {
                char typedChar = e.getKeyChar();
                char targetChar = targetText.charAt(currentIndex);

                if (typedChar == ' ') {
                    // Space bar pressed, skip the current word
                    skipWord();
                    return;
                }

                if (typedChar == targetChar) {
                    // Correctly typed character
                    currentIndex++;
                    typeRightWord++;
                    correctChar++;

                    screenText.setText(parseHtml(targetText, currentIndex));

                    if (currentIndex == targetText.length()) {
                        // Stop the timer and calculate statistics when the end of the text is reached
                        stopTimer();
                        long endTime = System.currentTimeMillis();
                        long timeTaken = endTime - startTime;
                        wpmCalculator(timeTaken, wordCount);
                        UserRepository.getInstance().saveDataToFile();
                    }
                } else if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
                    // Backspace pressed
                    if (currentIndex > 0) {
                        currentIndex--;
                        screenText.setText(parseHtml(targetText, currentIndex));
                    }
                } else {
                    // Incorrectly typed character
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
                startTimer();
            }

            private void skipWord() {
                for(int i = currentIndex; i<getNextWordIndex(targetText, currentIndex) ;i++){
                    mistakes[i] = true;
                }
                currentIndex = getNextWordIndex(targetText, currentIndex)-1;
                if (currentIndex >= targetText.length() - 1) {
                    screenText.setText(parseHtml(targetText, currentIndex));
                    long endTime = System.currentTimeMillis();
                    long timeTaken = endTime - startTime;
                    wpmCalculator(timeTaken, targetText.length());
                }
                if (currentIndex < targetText.length()) {
                    // Display the skipped word in red
                    mistakes[currentIndex] = true;
                    currentIndex++;
                    screenText.setText(parseHtml(targetText, currentIndex));
                }
            }

            private int getNextWordIndex(String sentence, int currentIndex) {
                // Skip to the next word by finding the next space character
                while (currentIndex < sentence.length() && !Character.isWhitespace(sentence.charAt(currentIndex))) {
                    currentIndex++;
                }
                // Skip any consecutive whitespace characters
                while (currentIndex < sentence.length() && Character.isWhitespace(sentence.charAt(currentIndex))) {
                    currentIndex++;
                }
                return currentIndex;
            }
        });

        // Add components to the game window
        gamemode.add(screenText);
        gamemode.add(title);
        gamemode.add(timerLabel);
        gamemode.add(buttonPanel);
        gamemode.setLayout(null);
        gamemode.getContentPane().setBackground(Color.decode("#323437"));
        gamemode.setVisible(true);
        gamemode.setFocusable(true);
        screenText.grabFocus();
    }
    
    private void resetGame() {
        mistake = 0;
        typeRightWord = 0;
        currentIndex = 0;
        correctChar = 0;
        secondsRemaining = 30;
        mistakes = new boolean[targetText.length()];
        startTime = System.currentTimeMillis();
        screenText.setText(parseHtml(targetText, 0));
        timerLabel.setText("30");
    }
    
    private void startNewRound() {
        stopTimer();
        screenText.grabFocus();
    }

    // Method to parse HTML for displaying formatted text with mistakes highlighted
    public String parseHtml(String input, int index) {
        StringBuilder html = new StringBuilder("<html><body style='color: #646669; font-family: Roboto Mono; font-size: 13px;'> <span style='color: white; font-size: 13px;'>");

        if(currentIndex==0){
            html.append("<span style='color: #E2B714; font-size: 13px; text-decoration: underline;'>").append(input.charAt(0)).append("</span>");
            for (int i = 1; i < input.length() && i < mistakes.length; i++) {
                html.append("<span style='color: #646669; font-size: 13px;'>").append(input.charAt(i)).append("</span>");
            }
        }else{
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

    // Method to find the word at the given index in a sentence
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

    // Method to calculate words per minute (WPM) and display statistics
    public void wpmCalculator(long timeTaken, double word) {
        // Create a set of words from the targetText
        HashSet<String> wordSet = new HashSet<>(Arrays.asList(targetText.split("\\s+")));

        // Remove words with mistakes from the set
        for (int i = 0; i < mistakes.length; i++) {
            if (mistakes[i]) {
                wordSet.remove(findWordAtIndex(targetText, i));
            }
        }

        // Calculate WPM, accuracy, and display statistics
        double wpmInitial = (correctChar / 5) * 2;
        int wpm = (int) wpmInitial;

        double accuracy = (typeRightWord / (double) (typeRightWord + mistake)) * 100;

        String message = "Words Per Minute (WPM): " + wpm + "\n"
                + "Mistakes: " + mistake + "\n"
                + String.format("Accuracy: %.2f%%", accuracy);

        UserRepository.getInstance().getCurrentUser().getWPM().add(wpm);
        UserRepository.getInstance().getCurrentUser().getAccuracy().add(accuracy);
        UserRepository.getInstance().saveDataToFile();

        // Display a message dialog with the statistics
        AWTEventListener myListener = (AWTEvent event) -> {
            if (event instanceof KeyEvent) {
                ((KeyEvent) event).consume();
            }
        };
        Toolkit.getDefaultToolkit().addAWTEventListener(myListener, AWTEvent.KEY_EVENT_MASK);
        JOptionPane.showMessageDialog(null, message, "WPM and Accuracy", JOptionPane.INFORMATION_MESSAGE);
        Toolkit.getDefaultToolkit().removeAWTEventListener(myListener);
    }
    
    private String getMostMisspelledWords() {
        HashMap<String, Integer> wrongWords = UserRepository.getInstance().getCurrentUser().getWrongWords();

        List<String> misspelledWordList = new ArrayList<>(wrongWords.keySet());
        List<String> shuffledMisspelledWords = new ArrayList<>(misspelledWordList); // Make a copy

        Collections.shuffle(shuffledMisspelledWords);

        StringBuilder result = new StringBuilder();

        // Repeat the misspelled words to achieve the target word count
        while (result.toString().split("\\s+").length < 30) {
            Collections.shuffle(shuffledMisspelledWords); // Shuffle the copy
            for (String word : shuffledMisspelledWords) {
                result.append(word).append(" ");
            }
        }

        // Remove the trailing space
        if (result.length() > 1) {
            result.setLength(result.length() - 1);
        }

        return result.toString();
    }

    // Method to start the timer
    private void startTimer() {
        if (!timer.isRunning()) {
            timer.start();
        }
    }

    // Method to stop the timer
    private void stopTimer() {
        if (timer.isRunning()) {
            timer.stop();
        }
    }
}
