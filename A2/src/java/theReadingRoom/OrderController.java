package theReadingRoom;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

public class OrderController {
    private String username;
    @FXML
    private Label orderNameLabel;
    @FXML
    private TableView<Orders> ordersTable;
    @FXML
    private TableColumn<Orders, String> descColumn;
    @FXML
    private TableColumn<Orders, Double> totalColumn;
    @FXML
    private TableColumn<Orders, String> dateColumn;
    @FXML
    private TableColumn<Orders, String> orderNum;
    @FXML
    private Button exportAllBtn;
    @FXML
    private Button exportSelBtn;
    @FXML
    private Button backBtn;
    private final ObservableList<Orders> orderItems = FXCollections.observableArrayList();
    private final Connection connection = DBConnection.openLink();

    public void initialize() {
        // Set up the table columns
        orderNum.setCellValueFactory(new PropertyValueFactory<>("order_id"));
        descColumn.setCellValueFactory(new PropertyValueFactory<>("order_description"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("order_total"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("order_date"));
        // Set the order name label
        String username = SessionManager.getInstance().getUsername();
        orderNameLabel.setText(username + "'s Orders");
        // Load the orders for the user
        loadOrders();
    }

    public void loadOrders() {
        String query = "SELECT * FROM Orders WHERE Username = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, SessionManager.getInstance().getUsername());
            ResultSet rs = ps.executeQuery();
            // Clear the previous orders
            orderItems.clear();
            while (rs.next()) {
                String orderID = rs.getString("Order_ID");
                String username = rs.getString("Username");
                String date = rs.getString("Date");
                Double total = rs.getDouble("Total");
                String description = rs.getString("Description");

                // Add to the observable list
                orderItems.add(new Orders(orderID, username, date, total, description));
            }
            // Set items to the table
            ordersTable.setItems(orderItems);
        } catch (SQLException e) {
            e.printStackTrace();
            // Consider showing an alert here
        } finally {
            DBConnection.closeLink();
        }
    }

    @FXML
    private void exportSelected(ActionEvent actionEvent) {
        //todo if i have time//

    }

    @FXML
    private void exportAll(ActionEvent actionEvent) throws IOException {
        exportToCSV(orderItems);
    }

    public void exportToCSV(ObservableList<Orders> orderItems) throws IOException {
        FileChooser fc = new FileChooser();
        fc.setTitle("Save Orders");
        //set the initial directory to the users home directory
        fc.setInitialDirectory(new File(System.getProperty("user.home")));
        // add the file type selection
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV file", "*.csv"));
        // show the dialog box
        File file = fc.showSaveDialog(new Stage());
        if (file != null) {
            try (FileWriter fw = new FileWriter(file)) {
                fw.write("Order_ID,Username,Date,Total,Description\n");
                // add the order details to the file
                for (Orders or : orderItems) {
                    fw.write((String.format("%s,%s,%s,%.2f,%s\n", or.getOrder_id(),
                            or.getUsername(),
                            or.getOrder_date(),
                            or.getOrder_total(),
                            or.getOrder_description())));
                }
                System.out.println("Orders exported to csv");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void backToDashboard(ActionEvent event) {
        try {
            Stage stage = (Stage) backBtn.getScene().getWindow();
            FXMLLoader loader;
            loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
            Parent root = loader.load();
            // Create and set up the new stage for order management
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}