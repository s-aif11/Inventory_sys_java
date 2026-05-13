package model;

public class VideoGame extends Game {
    private String developer;
    private String genre;
    private String platform;

    public VideoGame(String sku, String name, int quantity, double price,
                     String developer, String genre, String platform) {
        super(sku, name, quantity, price);
        this.developer = developer;
        this.genre = genre;
        this.platform = platform;
    }

    // Getters and Setters
    public String getDeveloper() { return developer; }
    public void setDeveloper(String developer) { this.developer = developer; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public String getPlatform() { return platform; }
    public void setPlatform(String platform) { this.platform = platform; }

    @Override
    public String getGameType() {
        return "Video Game";
    }

    @Override
    public String displayInfo() {
        return String.format("[%s] %s | Platform: %s | Genre: %s | Qty: %d | Price: $%.2f | Developer: %s",
                getSku(), getName(), platform, genre, getQuantity(), getPrice(), developer);
    }
}