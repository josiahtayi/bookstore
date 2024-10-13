package com.example.thereadingroom;

import java.sql.Date;

public class Orders {

    private String order_id;
    private String customer_name;
    private Date order_date; // Now explicitly java.sql.Date
    private double order_total;
    private String order_description;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public Date getOrder_date() {
        return order_date;
    }

    public void setOrder_date(java.util.Date order_date) {
        // Convert java.util.Date to java.sql.Date if needed
        if (order_date instanceof java.sql.Date) {
            this.order_date = (java.sql.Date) order_date;
        } else {
            this.order_date = new java.sql.Date(order_date.getTime());
        }
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
