package com.example.thereadingroom;

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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;

public class OrderController {


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

    String username = SessionManager.getInstance().getUsername();
    private ObservableList<Orders> orderItems = FXCollections.observableArrayList();


    public void setUsername(String username) {
        this.username = username;
    }


    public void initialize() {
        orderNum.setCellValueFactory(new PropertyValueFactory<>("order_id")); // Ensure these match the getters
        descColumn.setCellValueFactory(new PropertyValueFactory<>("order_description"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("order_total"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("order_date")); // Date may need to be formatted for display

        loadOrders();
    }

    public String getFormattedOrderDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        return sdf.format(dateColumn); // format java.sql.Date to String
    }


    public void setOrderUsername(String username) {
        this.orderNameLabel.setText(username + "'s Orders");
    }

    public void loadOrders() {
        DBConnection dbcon = new DBConnection();
        Connection connection = dbcon.openLink();
        ObservableList<Orders> recentOrders = FXCollections.observableArrayList();

        String query = "SELECT * FROM Orders WHERE Username = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery(); // Use ps instead of query here

            while (rs.next()) {
                String orderID = rs.getString("Order_ID");
                String username = rs.getString("Username");
                String date = rs.getString("Date");
                Double total = rs.getDouble("Total");
                String description = rs.getString("Description");

                recentOrders.add(new Orders(orderID, username, date, total, description));
            }

            ordersTable.setItems(recentOrders);
            orderItems = recentOrders;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbcon.closeLink(); // Ensure to close the connection
        }
    }


    @FXML
    private void exportSelected(ActionEvent actionEvent) {
    }

    @FXML
    private void exportAll(ActionEvent actionEvent) throws IOException {
        loadOrders();
        exportToCSV(orderItems);
    }

    public void exportToCSV(ObservableList<Orders> orderItems) throws IOException {
        File file = new File("Orders.csv");

        try (FileWriter fw = new FileWriter(file, false)) {
            fw.write("Order_ID,Username,Date,Total,Description\n");
            for (Orders r : orderItems) {
                fw.write(String.format("%s,%s,%s,%.2f,%s\n",
                        r.getOrder_id(),
                        r.getUsername(),
                        r.getOrder_date(),
                        r.getOrder_total(),
                        r.getOrder_description()));
            }
            System.out.println("Orders exported to csv");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void backToDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
            Parent dashboard = loader.load();

            Stage stage = (Stage) backBtn.getScene().getWindow();
            stage.setScene(new Scene(dashboard));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
