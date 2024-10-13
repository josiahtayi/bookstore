package com.example.thereadingroom;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DashboardController implements Initializable {

    private String username;
    private ObservableList<ShoppingCart> cartItems = FXCollections.observableArrayList();

    @FXML
    public Label welcomeLabel;
    @FXML
    public ListView<Book> searchResultsListView;
    @FXML
    private TextField searchField;
    @FXML
    private TableView<Book> top5table;
    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TableColumn<Book, Integer> salesColumn;
    @FXML
    private Button searchBtn;
    @FXML
    private TableView<Book> searchTable;
    @FXML
    private TableColumn<Book, String> searchTitle;
    @FXML
    private TableColumn<Book, String> searchAuthor;
    @FXML
    private TableColumn<Book, Integer> searchPrice;
    @FXML
    private TableColumn<Book, Integer> searchStock;
    @FXML
    private Button logOutBtn;
    @FXML
    private Button shoppingCartBtn;
    @FXML
    private Button addToCartBtn;
    @FXML
    private Button editProfile;
    @FXML
    private Button viewOrdersBtn;

    public void setUsername(String username) {
        this.username = username;
    }

    public void initialize(URL url, ResourceBundle rb) {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        salesColumn.setCellValueFactory(new PropertyValueFactory<>("sales"));

        searchTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        searchAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        searchPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        searchStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        searchBooks();

        loadTop5Books();
    }

    public void loadTop5Books() {
        DBConnection dbcon = new DBConnection();
        Connection connection = dbcon.openLink();
        ObservableList<Book> top5Books = FXCollections.observableArrayList();

        String query = "SELECT Title, Author, Sales FROM Books ORDER BY sales DESC LIMIT 5";

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String title = rs.getString("Title");
                String author = rs.getString("Author");
                int sales = rs.getInt("Sales");

                top5Books.add(new Book(title, author, 0, 0, sales));
            }
            top5table.setItems(top5Books);
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbcon.closeLink();
        }
    }

    public void searchBooks() {
        DBConnection dbcon = new DBConnection();
        Connection connection = dbcon.openLink();
        ObservableList<Book> BooksList = FXCollections.observableArrayList();

        String query = "SELECT Title, Author, Price, Stock FROM Books";

        try {

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String title = rs.getString("Title");
                String author = rs.getString("Author");
                int price = rs.getInt("Price");
                int stock = rs.getInt("Stock");
                //populate the book observable list
                BooksList.add(new Book(title, author, price, stock, 0));
            }
            searchTable.setItems(BooksList);

            //create filtered list
            FilteredList<Book> filteredList = new FilteredList<>(BooksList, b -> true);
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredList.setPredicate(book -> {
                    if (newValue == null || newValue.isEmpty() || newValue.isBlank()) {
                        return true;
                    }
                    String keyword = newValue.toLowerCase();
                    if (book.getTitle().toLowerCase().contains(keyword) || book.getAuthor().toLowerCase().contains(keyword)) {
                        return true; //filter list should change
                    } else
                        return false;
                });
            });

            SortedList<Book> sortedList = new SortedList<>(filteredList);

            sortedList.comparatorProperty().bind(searchTable.comparatorProperty());
            searchTable.setItems(sortedList);


        } catch (SQLException e) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
            e.getCause();
        }
    }

    public void addToCartBtn(ActionEvent actionEvent) {

        Book selectedBook = (Book) searchTable.getSelectionModel().getSelectedItem();

        if (selectedBook != null) {
            ShoppingCart inCart = cartItems.stream()
                    .filter(cartItem -> cartItem.getTitle().equals(selectedBook.getTitle()))
                    .findFirst()
                    .orElse(null);

            if (inCart != null) {
                inCart.setQuantity(inCart.getQuantity() + 1);
            } else {
                cartItems.add(new ShoppingCart(selectedBook.getTitle(), selectedBook.getPrice(), 1));
            }

            System.out.println("Added to cart" + selectedBook.getTitle());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(selectedBook.getTitle() + " Added To Cart");
            alert.setHeaderText(null);
            alert.setContentText(selectedBook.getTitle() + " Added To Cart");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Book Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please Select a Book");
            alert.showAndWait();
        }
    }


    public void setWelcomeLabel(String username) {
        welcomeLabel.setText("Welcome, " + username + "!");
    }


    public void logOutBtnOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) logOutBtn.getScene().getWindow();
        stage.close();
        showLogin();

    }

    public void showLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
            Stage loginStage = new Stage();
            loginStage.initStyle(StageStyle.UNDECORATED);
            loginStage.setScene(new Scene(root, 520, 400));
            loginStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void shoppingCartBtnOnAction(ActionEvent actionEvent) {
        openShoppingCart();
        Stage stage = (Stage) shoppingCartBtn.getScene().getWindow();

        stage.hide();
    }

    public void openShoppingCart() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ShoppingCart.fxml"));
            Parent root = loader.load();

            CartController cartController = loader.getController();
            cartController.setCartItems(cartItems);
            String username = this.username;
            cartController.setCartLabel(username);


            Stage shoppingCart = new Stage();
            shoppingCart.initStyle(StageStyle.UNDECORATED);
            shoppingCart.setScene(new Scene(root, 520, 400));
            shoppingCart.show();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void showOrders() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("OrderManagementTab.fxml"));
            Parent root = loader.load();
            String username = this.username;
            OrderController orderController = loader.getController();
            orderController.setOrderUsername(username);

            Stage profileStage = new Stage();
            profileStage.initStyle(StageStyle.UNDECORATED);
            profileStage.setScene(new Scene(root, 600, 600));
            profileStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EditProfile.fxml"));
            Parent root = loader.load();
            String username = this.username;
            ProfileController profileController = loader.getController();
            profileController.setUsernameLbl(username);

            Stage profileStage = new Stage();
            profileStage.initStyle(StageStyle.UNDECORATED);
            profileStage.setScene(new Scene(root, 600, 600));
            profileStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

