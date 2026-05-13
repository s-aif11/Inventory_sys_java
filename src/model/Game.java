package model;

public abstract class Game implements Cloneable {
    private String sku;
    private String name;
    private int quantity;
    private double price;

    public Game(String sku, String name, int quantity, double price) {
        this.sku = sku;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters and Setters
    public String getSku() { return sku; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public void updateQuantity(int newQuantity) {
        this.quantity = newQuantity;
    }

    public abstract String getGameType();
    public abstract String displayInfo();

    @Override
    public Game clone() {
        try {
            return (Game) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}