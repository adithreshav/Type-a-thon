package fop.assignment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

// The main class for the ClassicGameMode
public class ClassicGameMode {

    // Game variables
    private int mistake = 0;
    private int typeRightWord = 0;
    public static int wordCount;
    static long startTime;
    int currentIndex = 0;
    HashMap<String, Integer> wrongWords;
    boolean[] mistakes;
    String targetText;
    private int correctChar = 0;
    private final JFrame gamemode;

    // GUI components
    private JEditorPane screenText;
    private JButton newTextButton;
    private JButton repeatTextButton;
    private final Timer timer;
    private JLabel timerLabel;
    private int secondsRemaining = 30;

    // Constructor for ClassicGameMode
    public ClassicGameMode() {
        
        // Load custom font
        Font font = LoadFont.loadCustomFont("C:\\SEM 1\\FOP\\FOP ASSIGNMENT\\src\\fop\\assignment\\RobotoMono-VariableFont_wght.ttf", "Roboto Mono", Font.PLAIN, 24);
        
        // Initialize user data and game settings
        this.wrongWords = UserRepository.getInstance().getCurrentUser().getWrongWords();
        this.wordCount = 110;
        targetText = wordPool(wordCount);
        mistakes = new boolean[targetText.length()];
        startTime = System.currentTimeMillis();

        // Initialize the game frame
        gamemode = new JFrame();
        gamemode.setSize(600, 670);
        gamemode.setTitle("Classic Mode");
        gamemode.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gamemode.setLocationRelativeTo(null);
        gamemode.setResizable(false);
        
        // Initialize text editor and set its properties
        screenText = new JEditorPane("text/html", " ");
        screenText.setEditable(false);
        screenText.setForeground(Color.green);
        screenText.setFont(new Font("Roboto Mono", Font.BOLD, 15));
        screenText.setBackground(Color.decode("#323437"));
        screenText.setOpaque(true);
        screenText.setBounds(0, 50, 570, 200);
        screenText.setSelectionColor(Color.decode("#E2B714"));
        
        // Create and configure UI components
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
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.setBounds(0, 300, 570, 50);
        buttonPanel.setBackground(Color.decode("#323437"));

        // Create buttons and add action listeners
        newTextButton = new JButton("New Text");
        newTextButton.setFont(new Font("Roboto Mono", Font.BOLD, 15));
        newTextButton.setForeground(Color.decode("#646669"));
        newTextButton.setBackground(null);
        newTextButton.setBorderPainted(false);
        
        newTextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetGame();
                targetText = wordPool(wordCount);
                screenText.setText(parseHtml(targetText, 0));
                startNewRound();
            }
        });
        
        // Add mouse listeners for button hover effect
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

        repeatTextButton = new JButton("Repeat Text");
        repeatTextButton.setFont(new Font("Roboto Mono", Font.BOLD, 15));
        repeatTextButton.setForeground(Color.decode("#646669"));
        repeatTextButton.setBackground(null);
        repeatTextButton.setBorderPainted(false);
        
        repeatTextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetGame();
                screenText.setText(parseHtml(targetText, 0));
                startNewRound();
            }
        });
        
        // Add mouse listeners for button hover effect
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

        // Set text and timer, add key listener
        screenText.setText(parseHtml(targetText, 0));

        // Timer for game countdown
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (secondsRemaining > 0) {
                    secondsRemaining--;
                    String displaySeconds = Integer.toString(secondsRemaining);
                    timerLabel.setText(displaySeconds);
                } else {
                    stopTimer();
                    long endTime = System.currentTimeMillis();
                    long timeTaken = endTime - startTime;
                    wpmCalculator(timeTaken, wordCount);
                    UserRepository.getInstance().saveDataToFile();
                }
            }
        });

        // Key listener for text typing
        screenText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char typedChar = e.getKeyChar();
                char targetChar = targetText.charAt(currentIndex);

                if (typedChar == ' ') {
                    skipWord();
                    return;
                }

                if (typedChar == targetChar) {
                    currentIndex++;
                    typeRightWord++;
                    correctChar++;

                    screenText.setText(parseHtml(targetText, currentIndex));

                    if (currentIndex == targetText.length()) {
                        stopTimer();
                        long endTime = System.currentTimeMillis();
                        long timeTaken = endTime - startTime;
                        wpmCalculator(timeTaken, wordCount);
                        UserRepository.getInstance().saveDataToFile();
                    }
                } else if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
                    if (currentIndex > 0) {
//                        currentIndex--;
                        screenText.setText(parseHtml(targetText, currentIndex));
                    }
                } else {
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
                    mistakes[currentIndex] = true;
                    currentIndex++;
                    screenText.setText(parseHtml(targetText, currentIndex));
                }
            }

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

        // Add components to the game frame
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
    
    // Reset the game state
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
    
    // Start a new round by setting focus to the text editor
    private void startNewRound() {
        stopTimer();
        screenText.grabFocus();
    }

    // Parse HTML for text display
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

    // Find the word at a given index in a sentence
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

    // Calculate words per minute (WPM) and accuracy
    public void wpmCalculator(long timeTaken, double word) {

        double wpmInitial = (correctChar / 5) * 2;
        int wpm = (int) wpmInitial;

        double accuracy = (typeRightWord / (double) (typeRightWord + mistake)) * 100;

        String message = "Words Per Minute (WPM): " + wpm + "\n"
                + "Mistakes: " + mistake + "\n"
                + String.format("Accuracy: %.2f%%", accuracy);

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

    // Generate a random sentence for the word pool
    public static String wordPool(int length) {
        return ParagraphGenerator.generateRandomSentence(length);
    }

    // Helper method to start the timer
    private void startTimer() {
        if (!timer.isRunning()) {
            timer.start();
        }
    }

    // Helper method to stop the timer
    private void stopTimer() {
        if (timer.isRunning()) {
            timer.stop();
        }
    }
}