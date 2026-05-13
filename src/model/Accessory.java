package model;

public class Accessory extends Game {
    private String compatibleWith;
    private String accessoryType;

    public Accessory(String sku, String name, int quantity, double price,
                     String compatibleWith, String accessoryType) {
        super(sku, name, quantity, price);
        this.compatibleWith = compatibleWith;
        this.accessoryType = accessoryType;
    }

    // Getters and Setters
    public String getCompatibleWith() { return compatibleWith; }
    public void setCompatibleWith(String compatibleWith) { this.compatibleWith = compatibleWith; }
    public String getAccessoryType() { return accessoryType; }
    public void setAccessoryType(String accessoryType) { this.accessoryType = accessoryType; }

    @Override
    public String getGameType() {
        return "Accessory";
    }

    @Override
    public String displayInfo() {
        return String.format("[%s] %s | Type: %s | Compatible: %s | Qty: %d | Price: $%.2f",
                getSku(), getName(), accessoryType, compatibleWith, getQuantity(), getPrice());
    }
}