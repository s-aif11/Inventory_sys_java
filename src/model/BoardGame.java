package model;

public class BoardGame extends Game {
    private int minPlayers;
    private int maxPlayers;
    private String category;

    public BoardGame(String sku, String name, int quantity, double price,
                     int minPlayers, int maxPlayers, String category) {
        super(sku, name, quantity, price);
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.category = category;
    }

    // Getters and Setters
    public int getMinPlayers() { return minPlayers; }
    public void setMinPlayers(int minPlayers) { this.minPlayers = minPlayers; }
    public int getMaxPlayers() { return maxPlayers; }
    public void setMaxPlayers(int maxPlayers) { this.maxPlayers = maxPlayers; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    @Override
    public String getGameType() {
        return "Board Game";
    }

    @Override
    public String displayInfo() {
        return String.format("[%s] %s | Players: %d-%d | Category: %s | Qty: %d | Price: $%.2f",
                getSku(), getName(), minPlayers, maxPlayers, category, getQuantity(), getPrice());
    }
}