package com.example.thereadingroom;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DashboardController {

    @FXML
    public Label welcomeLabel;
    @FXML
    public TextField firstNameField;
    @FXML
    public TextField lastNameField;
    @FXML
    public PasswordField passwordField;
    @FXML
    private TextField searchField;
    @FXML
    private ListView<String> topBooksListView;
    @FXML
    private ListView<String> searchResultsListView;



    @FXML
    public void handleSearchBooks() {
        String query = searchField.getText().toLowerCase();
        searchResultsListView.getItems().clear();

        if (!query.isEmpty()) {
            List<String> results = books.stream()
                    .filter(book -> book.toLowerCase().contains(query))
                    .collect(Collectors.toList());

            if (!results.isEmpty()) {
                searchResultsListView.getItems().addAll(results);
            } else {
                searchResultsListView.getItems().add("No books found.");
            }
            searchResultsListView.setVisible(true);  // Show search results
        } else {
            searchResultsListView.setVisible(false);  // Hide if the search is empty
        }
    }

    public void handleSaveProfile(ActionEvent actionEvent) {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();

        // Basic validation
        if (firstName.isEmpty() || lastName.isEmpty()) {
            // Handle empty fields (e.g., show a message)
            System.out.println("First and last names cannot be empty!");
            return;
        }

        // Update the welcome message
        welcomeLabel.setText("Welcome, " + firstName + " " + lastName + "!");
        // Save logic goes here (e.g., update database)

        // Clear fields after saving
        firstNameField.clear();
        lastNameField.clear();
        passwordField.clear();
    }

    // Optionally, add a method to clear search results
    @FXML
    public void handleClearSearch() {
        searchField.clear();
        searchResultsListView.getItems().clear();
        searchResultsListView.setVisible(false);
    }
}
