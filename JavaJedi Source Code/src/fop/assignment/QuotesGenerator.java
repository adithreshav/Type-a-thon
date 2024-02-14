package fop.assignment;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class QuotesGenerator {

    // Array to store quotes from the file
    static String[] quotePool;

    // Random index for selecting a quote
    static int randomQ;

    // Variable to store the source of the generated quote
    private String quoteSource;

    // Random object for generating random indices
    private final Random random;

    // Constructor
    public QuotesGenerator() {
        random = new Random();
    }

    // Method to generate a random quote
    public String generateRandomQuotes() {
        try {
            // Read quotes from the file
            Scanner quoteScanner = new Scanner(new FileInputStream("./Quote.txt"));
            String protoS = "";
            while (quoteScanner.hasNextLine()) {
                protoS += quoteScanner.nextLine();
            }
            
            // Split the quotes using a specified delimiter ("/" in this case)
            quotePool = protoS.split("/");

            // Generate a random index to select a quote from the pool
            randomQ = random.nextInt(quotePool.length);

            // Determine the source of the quote based on the random index
            determineQuoteSource(randomQ);

        } catch (IOException e) {
            // Handle exceptions
            e.printStackTrace(); // Consider logging or proper error handling
        }
        return quotePool[randomQ];
    }

    // Method to determine the source of the quote based on the random index
    private void determineQuoteSource(int randomQ) {
        if (randomQ <= 2) {
            quoteSource = "movie \"The Shawshank Redemption\"";
        } else if (randomQ <= 6) {
            quoteSource = "TV series \"Stranger Things\"";
        } else if (randomQ <= 9) {
            quoteSource = "novel \"The Great Gatsby\"";
        } else if (randomQ <= 12) {
            quoteSource = "video game \"The Witcher 3: Wild Hunt\"";
        } else {
            quoteSource = "anime series \"Attack on Titan\"";
        }
    }

    // Method to get the source of the last generated quote
    public String getQuoteSource() {
        return quoteSource;
    }
}
