package com.example.thereadingroom;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class OrderController {



    @FXML
    private Label orderNameLabel;
    @FXML
    private TableView<Orders> ordersTable;
    @FXML
    private TableColumn itemsColumn;
    @FXML
    private TableColumn priceColumn;
    @FXML
    private TableColumn dateColumn;
    @FXML
    private TableColumn qtyColumn;
    @FXML
    private Button exportAllBtn;
    @FXML
    private Button exportSelBtn;
    @FXML
    private Button backBtn;





    public void setOrderUsername(String username) {
        this.orderNameLabel.setText(username + "'s Orders");
    }
    public void loadOrders(Orders order) {
        ordersTable.getItems().add(order);
        ordersTable.getColumns().clear();
        ordersTable.getColumns().addAll(itemsColumn, priceColumn, dateColumn);

    }


    @FXML
    private void exportSelected(ActionEvent actionEvent){

    }

    @FXML
    private void exportAll(ActionEvent actionEvent){

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
