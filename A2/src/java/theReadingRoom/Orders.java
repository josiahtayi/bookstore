package theReadingRoom;

import java.sql.Date;

public class Orders {

    private String order_id;
    private String username;
    private String order_date; // Change to String
    private Double order_total;
    private String order_description;

    public Orders(String order_id, String username, String order_date, Double order_total, String order_description) {
        this.order_id = order_id;
        this.username = username;
        this.order_date = order_date; // Change to String
        this.order_total = order_total;
        this.order_description = order_description;
    }


    public Orders() {

    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public double getOrder_total() {
        return order_total;
    }

    public void setOrder_total(double order_total) {
        this.order_total = order_total;
    }

    public String getOrder_description() {
        return order_description;
    }

    public void setOrder_description(String order_description) {
        this.order_description = order_description;
    }
}
