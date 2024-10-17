package theReadingRoom;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.*;
import java.util.Optional;

public class AdminController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private String username;
    @FXML
    private Label welcomeLabel;
    @FXML
    private TableView<Book> inventoryTable;
    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TableColumn<Book, Integer> stockColumn;
    @FXML
    private TableColumn<Book, Double> priceColumn;
    @FXML
    private TableColumn<Book, Integer> salesColumn;
    @FXML
    private Button increaseBtn;
    @FXML
    private Button decreaseBtn;
    @FXML
    private Button logoutBtn;


    public void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        salesColumn.setCellValueFactory(new PropertyValueFactory<>("sales"));
        //gets the username from the session manager
        this.username = SessionManager.getInstance().getUsername();
        welcomeLabel.setText("Hello, " + username);
        insertBooks();
    }

    public void insertBooks() {
        DBConnection dbcon = new DBConnection();
        Connection connection = dbcon.openLink();
        ObservableList<Book> bookInventory = FXCollections.observableArrayList();

        String query = "SELECT * FROM Books";

        try {
            PreparedStatement psmt = connection.prepareStatement(query);
            ResultSet rs = psmt.executeQuery();

            while (rs.next()) {
                String title = rs.getString("title");
                String author = rs.getString("author");
                int stock = rs.getInt("stock");
                int price = rs.getInt("price");
                int sales = rs.getInt("sales");

                bookInventory.add(new Book(title, author, stock, price, sales));
            }
            inventoryTable.setItems(bookInventory);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showStockDialog(String action) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Stock Update");
        dialog.setHeaderText("Enter the amount to " + action + " in stock");
        dialog.setContentText("Amount:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(amountStr -> {
            try {
                int amount = Integer.parseInt(amountStr);
                if (action.equals("decrease") && amount < 0) {
                    showAlert("Please enter a positive number for decrease.");
                    return;
                }

                // Use Math.abs for positive adjustment and specify true for increase
                updateStockInDatabase(Math.abs(amount), action.equals("increase"));

                refreshTableView();

            } catch (NumberFormatException e) {
                showAlert("Invalid input! Please enter a valid number.");
                e.printStackTrace();
            }
        });
    }

    private void updateStockInDatabase(int amount, boolean isIncrease) {
        DBConnection dbcon = new DBConnection();
        Connection connection = dbcon.openLink();

        String sql;
        if (isIncrease) {
            sql = "UPDATE Books SET Stock = stock + ? WHERE Title = ?";
        } else {
            sql = "UPDATE Books SET Stock = stock - ? WHERE Title = ?";
        }

        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setInt(1, amount);
            psmt.setString(2, getSelectedTitle());

            int affectedRows = psmt.executeUpdate();

            if (affectedRows > 0) {
                showAlert("Stock updated successfully");
            } else {
                showAlert("Something went wrong");
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    private String getSelectedTitle() {
        Book book = inventoryTable.getSelectionModel().getSelectedItem();
        return book.getTitle();
    }

    private void refreshTableView() {
       insertBooks();
    }

    public void doIncreaseStock(ActionEvent actionEvent) {
        showStockDialog("increase");
    }

    public void doDecreaseStock(ActionEvent actionEvent) {
        showStockDialog("decrease");
    }

    public void showLogin(ActionEvent actionEvent) {
        try {
            stage = (Stage) logoutBtn.getScene().getWindow();
            FXMLLoader loader;
            loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            root = loader.load();
            // Create and set up the new stage for login
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.getCause();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
