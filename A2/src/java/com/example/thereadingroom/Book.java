package com.example.thereadingroom;

public class Book {

    private String title;
    private String author;
    private int stock;
    private int price;
    private int sales;

    public Book(String title, String author, int stock, int price, int sales) {
        this.title = title;
        this.author = author;
        this.stock = stock;
        this.price = price;
        this.sales = sales;
    }

    public Book(String title, String author, int price, int sales) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.sales = sales;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public int getStock() {
        return stock;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }
    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public int getSales() {
        return sales;
    }
    public void setSales(int sales) {
        this.sales = sales;
    }
}
