package theReadingRoom;

import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
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
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private String username;
    private final ObservableList<ShoppingCart> cartItems = FXCollections.observableArrayList();
    @FXML
    public Label welcomeLabel;
    @FXML
    public ListView<Book> searchResultsListView;
    @FXML
    private TextField searchField;
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
    @FXML
    private Label marqueeLabel;

    private final Connection connection = DBConnection.openLink();

    public void initialize(URL url, ResourceBundle rb) {
        // the search table
        searchTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        searchAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        searchPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        searchStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        this.username = SessionManager.getInstance().getUsername();
        welcomeLabel.setText("Welcome, " + username + "!");
        searchBooks();
        loadTop5Books();
        startMarquee();
    }

    private void startMarquee() {
        // Create a TranslateTransition for the Label
        TranslateTransition marqueeTransition = new TranslateTransition();
        // Set the duration for the transition (adjust the speed here)
        marqueeTransition.setDuration(Duration.seconds(20)); // Adjust this for scrolling speed
        // Set the starting point (label will start just outside the right side of the window)
        marqueeTransition.setFromX(599);
        // Set the ending point (label will exit the screen to the left)
        marqueeTransition.setToX(-820);
        // Set the label as the target of the transition
        marqueeTransition.setNode(marqueeLabel);
        // Make the animation loop indefinitely
        marqueeTransition.setCycleCount(TranslateTransition.INDEFINITE);
        marqueeTransition.setAutoReverse(false);
        // Start the animation
        marqueeTransition.play();
        // Listen for the end of the transition
        marqueeTransition.setOnFinished(event -> {
            // Immediately reset the position to the start for continuous scrolling
            marqueeLabel.setTranslateX(600); // Reset position to the right
            // Restart the marquee
            marqueeTransition.play();
        });
    }

    public void loadTop5Books() {
        ObservableList<Book> top5Books = FXCollections.observableArrayList();
        String query = "SELECT Title FROM Books ORDER BY sales DESC LIMIT 5";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String title = rs.getString("Title");
                top5Books.add(new Book(title));
            }
            marqueeLabel.setText(top5Books.toString());
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void searchBooks() {
        ObservableList<Book> BooksList = FXCollections.observableArrayList();
        String query = "SELECT Title, Author, Stock, Price  FROM Books";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String title = rs.getString("Title");
                String author = rs.getString("Author");
                int price = rs.getInt("Price");
                int stock = rs.getInt("Stock");
                //fill the book observable list
                BooksList.add(new Book(title, author, stock, price, 0));
            }
            searchTable.setItems(BooksList);
            //create a filtered list
            FilteredList<Book> filteredList = new FilteredList<>(BooksList, b -> true);
            //add a listener to search the filtered list based on user's input
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredList.setPredicate(book -> {
                    if (newValue == null || newValue.isEmpty() || newValue.isBlank()) {
                        return true; //this shows all the books if the search bar is empty
                    }
                    // this matches the keyword with either the title or the author
                    String keyword = newValue.toLowerCase();
                    return book.getTitle().toLowerCase().contains(keyword) || book.getAuthor().toLowerCase().contains(keyword); //filter list should change
                });
            });
            SortedList<Book> sortedList = new SortedList<>(filteredList); // created a new sorted list object
            sortedList.comparatorProperty().bind(searchTable.comparatorProperty());
            searchTable.setItems(sortedList);
        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void addToCartBtn(ActionEvent actionEvent) {
        Book selectedBook = searchTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            // Ask user for quantity
            TextInputDialog dialog = new TextInputDialog("");
            dialog.setTitle("Add to Cart");
            dialog.setHeaderText("Specify the quantity of " + selectedBook.getTitle());
            dialog.setContentText("Quantity:");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(quantityStr -> {
                try {
                    int quantity = Integer.parseInt(quantityStr);
                    if (quantity <= 0) {
                        showAlert("Please enter a positive quantity.");
                        return;
                    }
                    // Check stock availability
                    if (quantity > selectedBook.getStock()) {
                        showAlert("Only " + selectedBook.getStock() + " copies of " + selectedBook.getTitle() + " are available.");
                        return;
                    }
                    // Add to cart
                    ShoppingCart inCart = cartItems.stream().filter(cartItem -> cartItem.getTitle().equals(selectedBook.getTitle())).findFirst().orElse(null);
                    if (inCart != null) {
                        // Update existing item in cart
                        if (inCart.getQuantity() + quantity <= selectedBook.getStock()) {
                            inCart.setQuantity(inCart.getQuantity() + quantity);
                        } else {
                            showAlert("Total quantity exceeds available stock.");
                            return;
                        }
                    } else {
                        // Add new item to cart
                        ShoppingCart newItem = new ShoppingCart(selectedBook.getTitle(), selectedBook.getAuthor(), quantity, selectedBook.getPrice());
                        cartItems.add(newItem);
                        saveToDB(newItem);
                    }
                    showAlert(selectedBook.getTitle() + " added to cart with quantity: " + quantity);
                } catch (NumberFormatException e) {
                    showAlert("Invalid quantity. Please enter a number.");
                }
            });
        } else {
            showAlert("Please select a book.");
        }
    }

    // this method saves the contents of the cart to a database table
    private void saveToDB(ShoppingCart cartIem) {
        String query = "INSERT INTO UserCart (Username, Title, Author, Price, Quantity, Status) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement psmt = connection.prepareStatement(query)) {
            psmt.setString(1, this.username);
            psmt.setString(2, cartIem.getTitle());
            psmt.setString(3, cartIem.getAuthor());
            psmt.setDouble(4, cartIem.getPrice());
            psmt.setInt(5, cartIem.getQuantity());
            psmt.setBoolean(6, false); //this sets a status column to false which corresponds to is
            //the cart has been checked out
            psmt.executeUpdate();
        } catch (SQLException e) {
            e.getCause();
            System.out.println("Error saving the cart");
        }
    }

    public void openShoppingCart(ActionEvent actionEvent) {
        try {
            FXMLLoader loader;
            loader = new FXMLLoader(getClass().getResource("ShoppingCart.fxml"));
            root = loader.load();
            CartController cartController = loader.getController();
            cartController.setStockCheckCallback(this::checkStockAvailability);
            stage = (Stage) shoppingCartBtn.getScene().getWindow();
            // Create and set up the new stage for shopping cart page
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.getCause();
        }
    }

    // Callback method to verify stock availability
    private boolean checkStockAvailability(ShoppingCart item) {
        int availableStock = getStockForBook(item.getTitle());
        return item.getQuantity() <= availableStock;
    }

    private int getStockForBook(String title) {
        Connection connection = DBConnection.openLink();
        int stock = 0;
        String query = "SELECT Stock FROM Books WHERE Title = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, title);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                stock = rs.getInt("Stock");
            }
        } catch (SQLException e) {
            e.getCause();
        }
        return stock;
    }

    public void showOrders(ActionEvent actionEvent) {
        try {
            stage = (Stage) viewOrdersBtn.getScene().getWindow();
            FXMLLoader loader;
            loader = new FXMLLoader(getClass().getResource("OrderManagement.fxml"));
            root = loader.load();
            // Create and set up the new stage for order management
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            DBConnection.closeLink(); // close the database connection when the scene is closed
        } catch (IOException e) {
            showAlert("Error loading orders. Please try again.");
        }
    }

    public void showProfile(ActionEvent actionEvent) {
        try {
            stage = (Stage) editProfile.getScene().getWindow();
            FXMLLoader loader;
            loader = new FXMLLoader(getClass().getResource("EditProfile.fxml"));
            root = loader.load();
            // Create and set up the new stage for Profile Editor
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            DBConnection.closeLink(); // close the database connection when the scene is closed
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showLogin(ActionEvent actionEvent) {
        try {
            stage = (Stage) logOutBtn.getScene().getWindow();
            FXMLLoader loader;
            loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            root = loader.load();
            // Create and set up the new stage for login
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            DBConnection.closeLink(); // close the database connection when the scene is closed
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    // shortcut to bring up alerts
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}