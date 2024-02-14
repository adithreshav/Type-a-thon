package fop.assignment;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LeaderboardSuddenDeath extends JFrame {
    private final UserRepository userRepository;

    // Constructor for the LeaderboardSuddenDeath class
    public LeaderboardSuddenDeath() {
        // Initialize the user repository
        userRepository = UserRepository.getInstance();
        
        // Set JFrame properties
        setTitle("Leaderboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(this);

        // Get the top players and create a LeaderboardTableModel
        List<User> topPlayers = getTopPlayers();
        LeaderboardTableModel model = new LeaderboardTableModel(topPlayers);

        // Create a JTable and set up the layout
        JTable table = new JTable(model);
        setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Method to retrieve the top players based on sudden death scores
    private List<User> getTopPlayers() {
        // Get all users from the repository
        List<User> allUsers = new ArrayList<>(userRepository.getAllUser());
        
        // Sort users based on their sudden death scores in descending order
        Collections.sort(allUsers, Comparator.comparingDouble(User::getSuddenDeathScore).reversed());
        
        // Return the top 10 players or all users if less than 10
        return allUsers.subList(0, Math.min(allUsers.size(), 10));
    }

    // Inner class representing the table model for the sudden death leaderboard
    private static class LeaderboardTableModel extends AbstractTableModel {
        // Column names for the table
        private static final String[] COLUMN_NAMES = {"Username", "Sudden Death Score"};
        
        // List of top players
        private final List<User> topPlayers;

        // Constructor for the LeaderboardTableModel
        public LeaderboardTableModel(List<User> topPlayers) {
            this.topPlayers = topPlayers;
        }

        // Get the number of rows in the table
        @Override
        public int getRowCount() {
            return topPlayers.size();
        }

        // Get the number of columns in the table
        @Override
        public int getColumnCount() {
            return COLUMN_NAMES.length;
        }

        // Get the value at a specific cell in the table
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            User user = topPlayers.get(rowIndex);

            // Return values based on column index
            if (columnIndex == 0) {
                return user.getUsername();
            } else if (columnIndex == 1) {
                return user.getSuddenDeathScore();
            }

            return null;
        }

        // Get the column name for a specific column index
        @Override
        public String getColumnName(int column) {
            return COLUMN_NAMES[column];
        }
    }

    // Method to display the sudden death leaderboard
    public void showSuddenDeathLeaderboard() {
        setVisible(true);
    }
}
