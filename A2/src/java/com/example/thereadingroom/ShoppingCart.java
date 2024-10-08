package com.example.thereadingroom;

public class ShoppingCart {
    private String title;
    private String author;
    private String type;
    private double price;
    private int quantity;

    public ShoppingCart() {

    }

    public ShoppingCart(String title, int quantity, String type, double price) {
        this.title = title;
        this.quantity = quantity;
        this.type = type;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getType() {
        return type;
    }

    public Double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;

    }
}