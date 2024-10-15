package theReadingRoom;

public class ShoppingCart {
    private String title;
    private String author;
    private double price;
    private int quantity;
    private boolean status = false;

    public ShoppingCart() {
    }

    public ShoppingCart(String title, String author, int quantity, double price) {
        this.title = title;
        this.author = author;
        this.quantity = quantity;
        this.price = price;
    }
    public ShoppingCart(String title, String author, double price, int quantity, boolean status) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
    }

    public ShoppingCart(String title, double price, int quantity) {
        this.title = title;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters and Setters
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }

}