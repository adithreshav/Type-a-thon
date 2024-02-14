package fop.assignment;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class UserProfiles extends JFrame {
    private final List<User> userList;
    private final UserRepository userRepository;

    // Constructor to initialize the UserProfiles JFrame
    public UserProfiles() {
        userRepository = UserRepository.getInstance();
        this.userList = List.copyOf(userRepository.getAllUser());

        setTitle("Type-a-thon");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        LeaderboardTableModel model = new LeaderboardTableModel();

        JTable table = new JTable(model);
        setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Inner class for the table model
    private class LeaderboardTableModel extends AbstractTableModel {

        // Get the number of rows in the table (equal to the number of users)
        @Override
        public int getRowCount() {
            return userList.size();
        }

        // Get the number of columns in the table (6 columns for various user statistics)
        @Override
        public int getColumnCount() {
            return 6; // To display six columns: username, last10wpmaverage, all time average, sudden death score, mispelled words
        }

        // Get the value at a specific row and column in the table
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            User user = userList.get(rowIndex);

            // Column 0: Username
            switch (columnIndex) {
                case 0:
                    return user.getUsername();
                case 1:
                    return user.getLastTenWpmAverage();
                case 2:
                    return user.getAllTimeAverageWPM();
                case 3:
                    return user.getSuddenDeathScore();
                case 4:
                    return user.getCalculatedAccuracy();
                case 5:
                    return user.getWrongWords();
                default:
                    break;
            }

            return null; // Default case
        }

        // Get the column name for a specific column index
        @Override
        public String getColumnName(int column) {
            // Provide column names
            switch (column) {
                case 0:
                    return "Username";
                case 1:
                    return "Last 10 WPM Average";
                case 2:
                    return "All Time Average WPM";
                case 3:
                    return "Sudden Death Score";
                case 4:
                    return "Average Accuracy";
                case 5:
                    return "10 most misspelled words";
                default:
                    break;
            }

            return ""; // Default case
        }
    }

    // Method to display the user profiles JFrame
    public void showUserProfile() {
        this.setVisible(true);
    }
}
