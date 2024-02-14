/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fop.assignment;

import java.util.*;
import java.io.*;

public class User implements Serializable {
    private String username;
    private String password;
    private final List<Integer> WPM;  // List to store Words Per Minute (WPM) values
    private final HashMap<String, Integer> wrongWords;  // Map to store wrong words and their occurrences
    private double suddenDeathScore;
    private final List<Double> accuracy;  // List to store accuracy values

    // Constructor to initialize a User object
    public User(String username, String password, List<Integer> WPM, int allTimeAverageWPM, int suddenDeathScore, double lastTenWpmAverage) {
        this.wrongWords = new HashMap<>();
        this.username = username;
        this.password = password;
        this.WPM = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0));  // Initialize WPM list with 10 elements
        this.suddenDeathScore = 0.0;
        this.accuracy = new ArrayList<>();
    }

    // Getters

    // Get the map of wrong words and their occurrences
    public HashMap<String, Integer> getWrongWords() {
        return wrongWords;
    }

    // Get the username of the user
    public String getUsername() {
        return username;
    }

    // Get the password of the user
    public String getPassword() {
        return password;
    }

    // Get the list of Words Per Minute (WPM) values
    public List<Integer> getWPM() {
        return WPM;
    }

    // Get the list of accuracy values
    public List<Double> getAccuracy() {
        return accuracy;
    }

    // Setters

    // Set the username of the user
    public void setUsername(String username) {
        this.username = username;
    }

    // Set the password of the user
    public void setPassword(String password) {
        this.password = password;
    }

    // Set the sudden death score of the user
    public void setSuddenDeathScore(double suddenDeathScore) {
        this.suddenDeathScore = suddenDeathScore;
    }

    // Get the sudden death score of the user
    public double getSuddenDeathScore() {
        return suddenDeathScore;
    }

    // Calculate the average WPM over the last ten sessions
    public double calculateLastTenWpmAverage(List<Integer> lastTenWPM) {
        if (lastTenWPM.isEmpty()) {
            return 0.0;
        }

        int sum = lastTenWPM.stream().mapToInt(Integer::intValue).sum();
        return (double) sum / lastTenWPM.size();
    }

    // Get the average WPM over the last ten sessions
    public double getLastTenWpmAverage() {
        double sum = 0.0;
        for (int i = 0; i < 10; i++) {
            sum += WPM.get(WPM.size() - i - 1);
        }
        return sum / 10;
    }

    // Get the overall average WPM
    public double getAllTimeAverageWPM() {
        double sum = 0.0;
        for (int i = 9; i < WPM.size(); i++) {
            sum += WPM.get(i);
        }
        return sum / (WPM.size() - 10);
    }

    // Get the calculated accuracy
    public double getCalculatedAccuracy() {
        double averageAccuracy = 0.0;
        for (int i = 0; i < accuracy.size(); i++) {
            averageAccuracy += accuracy.get(i);
        }
        return averageAccuracy / accuracy.size();
    }
}

