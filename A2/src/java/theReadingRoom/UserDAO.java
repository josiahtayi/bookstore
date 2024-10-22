package theReadingRoom;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    Connection connection = DBConnection.openLink();

    //method to validate user login
    public boolean validateLogin(String username, String password) {
        String query = "SELECT COUNT(1) FROM Users WHERE Username = ? AND Password = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next() && resultSet.getInt(1) == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("User validation failed");
            return false;
        }
    }

    //method to handle user registration
    public boolean registerUser(String username, String password, String fname, String lname) {
        String query = "INSERT INTO Users (Username, Password, Fname, Lname) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, fname);
            preparedStatement.setString(4, lname);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0; // Returns true if user is successfully registered
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("User registration failed");
            return false;
        }
    }

    //method to check if the username already exists
    public boolean checkUsernameExists(String username) {
        String query = "SELECT COUNT(1) FROM Users WHERE Username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next() && resultSet.getInt(1) == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //method to change Profile details
    public boolean updateUserProfile(String username, String newFName, String newLName, String newPassword) {
        String updateQuery = "UPDATE Users SET Fname = ?, Lname = ?, Password = ? WHERE Username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, newFName);
            preparedStatement.setString(2, newLName);
            preparedStatement.setString(3, newPassword);
            preparedStatement.setString(4, username);
            //similar to the method create new users but does not change the username as per requirements
            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0; // Return true if rows were updated
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false if an error occurs
        } finally {
            DBConnection.closeLink();
        }
    }

    //this method only used in the UserDaoTest to remove the created test users
    public void deleteUser(String username) {
        String query = "DELETE FROM Users WHERE Username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            int rowsDeleted = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.getCause();
        }
    }
}