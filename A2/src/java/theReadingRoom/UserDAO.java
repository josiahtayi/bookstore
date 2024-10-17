package theReadingRoom;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private DBConnection dbConnection;

    public UserDAO() {
        dbConnection = new DBConnection();
    }
    //method to validate user login
    public boolean validateLogin(String username, String password) {
        String query = "SELECT COUNT(1) FROM Users WHERE Username = ? AND Password = ?";
        try (Connection connection = dbConnection.openLink();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next() && resultSet.getInt(1) == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Consider logging this error
        }
    }

    //method to handle user registration
    public boolean registerUser(String username, String password, String fname, String lname) {
        String query = "INSERT INTO Users (Username, Password, Fname, Lname) VALUES (?, ?, ?, ?)";
        try (Connection connection = dbConnection.openLink();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, fname);
            preparedStatement.setString(4, lname);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0; // Returns true if user is successfully registered
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Consider logging this error
        }
    }
    //method to check if the username already exists
    public boolean checkUsernameExists(String username) {
        String query = "SELECT COUNT(1) FROM Users WHERE Username = ?";
        try (Connection connection = dbConnection.openLink();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next() && resultSet.getInt(1) == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //method To chan Profile details
    public boolean updateUserProfile(String username, String newFName, String newLName, String newPassword) {
        DBConnection dbcon = new DBConnection();
        Connection connection = dbcon.openLink();

        String updateQuery = "UPDATE Users SET Fname = ?, Lname = ?, Password = ? WHERE Username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, newFName);
            preparedStatement.setString(2, newLName);
            preparedStatement.setString(3, newPassword);
            preparedStatement.setString(4, username);

            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0; // Return true if rows were updated
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false if an error occurs
        } finally {
            dbcon.closeLink();
        }
    }




















}


