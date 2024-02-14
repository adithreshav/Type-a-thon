package fop.assignment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import javax.swing.border.EmptyBorder;

public class TimedGameMode {

    // Instance variables
    private int mistake = 0;
    private int typeRightWord = 0;
    public static int wordCount;
    static long startTime;
    int currentIndex = 0;
    HashMap<String, Integer> wrongWords;
    boolean[] mistakes;
    String targetText;
    private int correctChar;
    private static boolean includePunctuation;
    private final int duration;
    private final JFrame gamemode;
    private JButton newTextButton;
    private JButton repeatTextButton;
    JEditorPane screenText;

    private final Timer timer;
    private JLabel timerLabel;
    int secondsRemaining;

    // Constructor for TimedGameMode
    public TimedGameMode(int duration, boolean punctuation) {
        Font font = LoadFont.loadCustomFont("C:\\SEM 1\\FOP\\FOP ASSIGNMENT\\src\\fop\\assignment\\RobotoMono-VariableFont_wght.ttf", "Roboto Mono", Font.PLAIN, 24);
        // Initialize instance variables
        this.includePunctuation = punctuation;
        this.duration = duration;
        this.wrongWords = UserRepository.getInstance().getCurrentUser().getWrongWords();
        this.wordCount = 215; // Default word count
        targetText = wordPool(wordCount, includePunctuation);
        mistakes = new boolean[targetText.length()];
        startTime = System.currentTimeMillis();

        // Set up JFrame
        gamemode = new JFrame();
        gamemode.setSize(600, 670);
        gamemode.setTitle("Timed Mode");
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

        JLabel title = new JLabel("Type to start");
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setVerticalAlignment(JLabel.TOP);
        title.setVerticalTextPosition(JLabel.CENTER);
        title.setHorizontalTextPosition(JLabel.CENTER);
        title.setForeground(Color.decode("#E2B714"));
        title.setFont(new Font("Roboto Mono", Font.BOLD, 20));
        title.setBounds(0, 0, 570, 50);
        title.setBorder(new EmptyBorder(10,0,0,0));

        timerLabel = new JLabel(Integer.toString(duration));
        timerLabel.setHorizontalAlignment(JLabel.CENTER);
        timerLabel.setVerticalAlignment(JLabel.TOP);
        timerLabel.setVerticalTextPosition(JLabel.CENTER);
        timerLabel.setHorizontalTextPosition(JLabel.CENTER);
        timerLabel.setForeground(Color.decode("#E2B714"));
        timerLabel.setFont(new Font("Roboto Mono", Font.BOLD, 15));
        timerLabel.setBounds(0, 250, 570, 50);

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
                targetText = wordPool(wordCount, includePunctuation);
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
        
        // Set initial text in JEditorPane
        screenText.setText(parseHtml(targetText, 0));

        secondsRemaining = duration;
        // Set up Timer
        timer = new Timer(1000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (secondsRemaining > 0) {
                    secondsRemaining--;
                    timerLabel.setText(Integer.toString(secondsRemaining));
                } else {
                    stopTimer();
                    long endTime = System.currentTimeMillis();
                    long timeTaken = endTime - startTime;
                    wpmCalculator(timeTaken, wordCount);
                    UserRepository.getInstance().saveDataToFile();
                }
            }
        });

        // Add KeyListener to JEditorPane for typing interaction
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

        // Add components to the JFrame
        gamemode.add(screenText);
        gamemode.add(title);
        gamemode.add(timerLabel);
        gamemode.add(buttonPanel);
        gamemode.setLayout(null);
        gamemode.getContentPane().setBackground(Color.decode("#323437"));
        gamemode.setVisible(true);
        gamemode.setFocusable(true);
        screenText.grabFocus();  // Set focus to JEditorPane for typing
    }
    
    private void resetGame() {
        mistake = 0;
        typeRightWord = 0;
        currentIndex = 0;
        correctChar = 0;
        secondsRemaining = duration;
        mistakes = new boolean[targetText.length()];
        startTime = System.currentTimeMillis();
        screenText.setText(parseHtml(targetText, 0));
        timerLabel.setText(Integer.toString(duration));
    }
    
    private void startNewRound() {
        stopTimer();
        screenText.grabFocus();
    }

    // Method to format text in HTML for display
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

    // Method to find the word at a specific index in a sentence
    public static String findWordAtIndex(String sentence, int index) {
        String[] words = sentence.split("\\s+");
        int currentPosition = 0;
        String currentWord = null;

        for (int i = 0; i < words.length; i++) {
            currentPosition += words[i].length() + 1; // Add 1 for the space between words
            if (index < currentPosition) {
                currentWord = words[i];
                break;
            }
        }

        return currentWord;
    }

    // Method to calculate WPM and accuracy
    public void wpmCalculator(long timeTaken, double word) {
        double wpmInitial = (correctChar / 5) * (60.0 / duration);
        int wpm = (int) wpmInitial;

        double accuracy = (typeRightWord / (double) (typeRightWord + mistake)) * 100;

        String message = "Words Per Minute (WPM): " + wpm + "\n"
                + "Mistakes: " + mistake + "\n"
                + String.format("Accuracy: %.2f%%", accuracy);

        // Update user data and display result in JOptionPane
        UserRepository.getInstance().getCurrentUser().getWPM().add(wpm);
        UserRepository.getInstance().getCurrentUser().getAccuracy().add(accuracy);
        UserRepository.getInstance().saveDataToFile();

        // Add an AWTEventListener to consume key events
        AWTEventListener myListener = (AWTEvent event) -> {
            if (event instanceof KeyEvent) {
                ((KeyEvent) event).consume();
            }
        };
        Toolkit.getDefaultToolkit().addAWTEventListener(myListener, AWTEvent.KEY_EVENT_MASK);

        // Display the result in a JOptionPane
        JOptionPane.showMessageDialog(null, message, "WPM and Accuracy", JOptionPane.INFORMATION_MESSAGE);

        // Remove the AWTEventListener and close the JFrame
        Toolkit.getDefaultToolkit().removeAWTEventListener(myListener);
    }

    // Static method to check if punctuation is included
    public static boolean includePunctuation() {
        return includePunctuation;
    }

    // Static method to set whether punctuation should be included
    public static void setIncludePunctuation(boolean include) {
        includePunctuation = include;
    }

    // Static method to generate a word pool with or without punctuation
    public static String wordPool(int length, boolean includePunctuation) {
        String generatedSentence = ParagraphGenerator.generateRandomSentence(length);
        System.out.println("Generated sentence: " + generatedSentence);
        if (includePunctuation) {
            generatedSentence = insertRandomPunctuation(generatedSentence);
            System.out.println("Sentence with punctuation: " + generatedSentence);
        }

        return generatedSentence;
    }

    // Private static method to insert random punctuation into a sentence
    private static String insertRandomPunctuation(String sentence) {
        String[] punctuationOptions = { ",", ".", "!", "?" };
        int numWords = sentence.split("\\s+").length;
        int punctuationFrequency = 10; // Adjust the frequency of punctuations

        StringBuilder sentenceBuilder = new StringBuilder(sentence);

        for (int i = 0; i < numWords / punctuationFrequency; i++) {
            int randomWordIndex = (int) (Math.random() * numWords);
            int wordStart = findWordStart(sentenceBuilder.toString(), randomWordIndex);

            int randomPosition = wordStart;
            String randomPunctuation = punctuationOptions[(int) (Math.random() * punctuationOptions.length)];

            sentenceBuilder.insert(randomPosition, randomPunctuation);
        }

        return sentenceBuilder.toString();
    }

    // Private static method to find the start position of a word in a sentence
    private static int findWordStart(String sentence, int wordIndex) {
        String[] words = sentence.split("\\s+");
        int currentPosition = 0;

        for (int i = 0; i < wordIndex; i++) {
            currentPosition += words[i].length() + 1; // Add 1 for the space between words
        }

        return currentPosition;
    }

    // Private method to start the timer
    private void startTimer() {
        if (!timer.isRunning()) {
            timer.start();
        }
    }

    // Private method to stop the timer
    private void stopTimer() {
        if (timer.isRunning()) {
            timer.stop();
        }
    }
}
