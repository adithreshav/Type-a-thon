package fop.assignment;

import java.io.*;
import java.util.*;

public class ParagraphGenerator {

    // List to store words for training the generator
    private final List<String> wordList;

    // Random number generator for word selection
    private final Random random;

    // Constructor for the ParagraphGenerator class
    public ParagraphGenerator() {
        wordList = new ArrayList<>();
        random = new Random();
    }

    // Train the generator with text input
    public void train(String text) {
        // Split the text into words and add them to the word list
        String[] words = text.split("\\s+");
        wordList.addAll(Arrays.asList(words));
    }

    // Generate a sentence of a given length
    public String generateSentence(int length) {
        StringBuilder sentence = new StringBuilder();
        // Generate each word in the sentence
        for (int i = 0; i < length; i++) {
            String nextWord = generateWord();
            if (nextWord != null) {
                // Append the word to the sentence with a space
                sentence.append(" ").append(nextWord);
            } else {
                // If no more words are available, break the loop
                break;
            }
        }
        // Trim leading and trailing spaces and return the sentence
        return sentence.toString().trim();
    }

    // Generate a random word from the word list
    private String generateWord() {
        // Return null if the word list is empty
        return wordList.isEmpty() ? null : wordList.get(random.nextInt(wordList.size()));
    }

    // Read content from a file and return it as a string
    public static String readFromFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        // Use try-with-resources to automatically close the BufferedReader
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Read each line from the file and append it to the content
            while ((line = reader.readLine()) != null) {
                content.append(line).append(" ");
            }
        }
        // Return the content as a string
        return content.toString();
    }

    // Generate a random sentence using the generator and a pre-trained word list
    public static String generateRandomSentence(int length) {
        // Create a new generator instance
        ParagraphGenerator generator = new ParagraphGenerator();
        // Specify the file path containing the training text
        String filePath = "./easywordlist.txt";
        try {
            // Read the training text from the file
            String trainingText = readFromFile(filePath);
            // Train the generator with the training text
            generator.train(trainingText);
            // Generate a sentence of the specified length
            String generatedSentence = generator.generateSentence(length);
            // Return the generated sentence (or null if empty)
            return generatedSentence.isEmpty() ? null : generatedSentence;
        } catch (IOException e) {
            // Handle IOException (print stack trace for simplicity; consider proper error handling)
            e.printStackTrace();
        }
        // Return null in case of an exception
        return null;
    }
}
