package fop.assignment;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class WordsGameMode {

    private int mistake = 0;
    private int typeRightWord = 0;
    public static int wordCount;
    static long startTime;
    int currentIndex = 0;
    HashMap<String, Integer> wrongWords;
    boolean[] mistakes;
    String targetText;
    private final JFrame gamemode;
    private int correctChar;
    private JButton newTextButton;
    private JButton repeatTextButton;
    private JEditorPane screenText;

    public WordsGameMode(int words) {
        Font font = LoadFont.loadCustomFont("C:\\SEM 1\\FOP\\FOP ASSIGNMENT\\src\\fop\\assignment\\RobotoMono-VariableFont_wght.ttf", "Roboto Mono", Font.PLAIN, 24);
        // Initialize variables and components
        this.wrongWords = UserRepository.getInstance().getCurrentUser().getWrongWords();
        this.wordCount = words;
        targetText = wordPool(wordCount);
        mistakes = new boolean[targetText.length()];
        startTime = System.currentTimeMillis();

        gamemode = new JFrame();
        gamemode.setSize(600, 670);
        gamemode.setTitle("Words Mode");
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
        screenText.setBounds(0, 50, 570, 300);
        screenText.setBorder(new EmptyBorder(20,0,0,0));
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
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.setBounds(0, 350, 570, 50);
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
                targetText = wordPool(wordCount);
                resetGame();
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
                } else if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
                    // Backspace pressed
                    if (currentIndex > 0) {
                        currentIndex--;
                        if (mistakes[currentIndex]) mistake--;
                        mistakes[currentIndex] = false;
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

                if (currentIndex == targetText.length() - 1) {
                    screenText.setText(parseHtml(targetText, targetText.length()));
                    long endTime = System.currentTimeMillis();
                    long timeTaken = endTime - startTime;
                    wpmCalculator(timeTaken, targetText.length());
                }
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

            // ... (other methods)
        });

        gamemode.add(screenText);
        gamemode.add(title);
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
//        targetText = wordPool(wordCount);
        mistakes = new boolean[targetText.length()];
        startTime = System.currentTimeMillis();
        screenText.setText(parseHtml(targetText, 0));
    }
    
    private void startNewRound() {
//        startTimer();
        screenText.grabFocus();
    }

    // Format the target text with HTML tags
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

    // Find the word at the specified index in a sentence
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

    // Calculate and display words per minute (WPM) and accuracy
    public void wpmCalculator(long timeTaken, double word) {
        HashSet<String> wordSet = new HashSet<>(Arrays.asList(targetText.split("\\s+")));

        // Exclude words with mistakes from the word set
        for (int i = 0; i < mistakes.length; i++) {
            if (mistakes[i]) {
                wordSet.remove(findWordAtIndex(targetText, i));
            }
        }

        // Calculate WPM and accuracy
        double minutes = timeTaken / 60000.0;
        double wpmInitial = (correctChar / 5) / minutes;
        int wpm = (int) wpmInitial;

        double accuracy = (typeRightWord / (double) (typeRightWord + mistake)) * 100;

        // Display results and update user data
        String message = "Words Per Minute (WPM): " + wpm + "\n"
                + "Mistakes: " + mistake + "\n"
                + String.format("Accuracy: %.2f%%", accuracy);

        UserRepository.getInstance().getCurrentUser().getWPM().add(wpm);
        UserRepository.getInstance().getCurrentUser().getAccuracy().add(accuracy);
        UserRepository.getInstance().saveDataToFile();

        // Disable further key events to prevent additional typing
        AWTEventListener myListener = (AWTEvent event) -> {
            if (event instanceof KeyEvent) {
                ((KeyEvent) event).consume();
            }
        };
        Toolkit.getDefaultToolkit().addAWTEventListener(myListener, AWTEvent.KEY_EVENT_MASK);
        JOptionPane.showMessageDialog(null, message, "WPM and Accuracy", JOptionPane.INFORMATION_MESSAGE);
        Toolkit.getDefaultToolkit().removeAWTEventListener(myListener);
    }

    // Generate a random sentence for the game
    public static String wordPool(int length) {
        return ParagraphGenerator.generateRandomSentence(length);
    }
}

