package manager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import model.Game;
import model.VideoGame;
import model.BoardGame;
import model.Accessory;
import model.TradeOperation;
import memento.InventoryMemento;
import memento.InventoryCaretaker;

public class GameInventoryManager {
    private List<Game> games = new ArrayList<>();
    private InventoryCaretaker caretaker = new InventoryCaretaker();

    // Add game
    public void addGame(Game game) {
        games.add(game);
    }

    // Get all games
    public List<Game> getAllGames() {
        return new ArrayList<>(games);
    }

    // Get game by SKU
    public Game getGame(String sku) {
        return games.stream()
                .filter(g -> g.getSku().equals(sku))
                .findFirst()
                .orElse(null);
    }

    // Create memento
    public InventoryMemento createMemento(String description) {
        return new InventoryMemento(this, games, description);
    }

    // Restore state (called by memento)
    public void restoreState(List<Game> state) {
        this.games = new ArrayList<>();
        for (Game game : state) {
            this.games.add(game.clone());
        }
        System.out.println("\n[✓ Inventory restored from snapshot]");
    }

    // Execute risky trade with auto-rollback
    public boolean executeRiskyTrade(TradeOperation trade) {
        System.out.printf("\n=== Executing Trade: %s ===\n", trade.getDescription());

        // Save snapshot before risky operation
        InventoryMemento memento = createMemento("Before trade: " + trade.getTradeId());
        caretaker.push(memento);

        try {
            // Apply all changes
            for (var entry : trade.getItemChanges().entrySet()) {
                String sku = entry.getKey();
                int change = entry.getValue();

                Game game = getGame(sku);
                if (game == null) {
                    throw new RuntimeException("Game with SKU " + sku + " not found!");
                }

                int newQuantity = game.getQuantity() + change;
                if (newQuantity < 0) {
                    throw new RuntimeException(String.format(
                            "Insufficient quantity for %s. Available: %d, Requested: %d",
                            game.getName(), game.getQuantity(), -change));
                }

                System.out.printf("  %s: %d → %d\n", game.getName(), game.getQuantity(), newQuantity);
                game.updateQuantity(newQuantity);
            }

            System.out.println("\n✅ Trade completed successfully!");
            return true;

        } catch (Exception ex) {
            System.out.printf("\n❌ Trade failed: %s\n", ex.getMessage());
            System.out.println("Rolling back to previous state...");
            rollback();
            return false;
        }
    }

    // Manual rollback
    public void rollback() {
        if (caretaker.canRollback()) {
            InventoryMemento memento = caretaker.pop();
            memento.restore();
        } else {
            System.out.println("❌ No history available for rollback!");
        }
    }

    public boolean canRollback() {
        return caretaker.canRollback();
    }

    // Display inventory
    public void displayInventory() {
        System.out.println("\n=== CURRENT INVENTORY ===");

        if (games.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }

        double totalValue = 0;
        int totalItems = 0;

        // Video Games
        List<VideoGame> videoGames = games.stream()
                .filter(g -> g instanceof VideoGame)
                .map(g -> (VideoGame) g)
                .collect(Collectors.toList());

        if (!videoGames.isEmpty()) {
            System.out.println("\n--- VIDEO GAMES ---");
            for (VideoGame game : videoGames) {
                System.out.println("  " + game.displayInfo());
                totalValue += game.getQuantity() * game.getPrice();
                totalItems += game.getQuantity();
            }
        }

        // Board Games
        List<BoardGame> boardGames = games.stream()
                .filter(g -> g instanceof BoardGame)
                .map(g -> (BoardGame) g)
                .collect(Collectors.toList());

        if (!boardGames.isEmpty()) {
            System.out.println("\n--- BOARD GAMES ---");
            for (BoardGame game : boardGames) {
                System.out.println("  " + game.displayInfo());
                totalValue += game.getQuantity() * game.getPrice();
                totalItems += game.getQuantity();
            }
        }

        // Accessories
        List<Accessory> accessories = games.stream()
                .filter(g -> g instanceof Accessory)
                .map(g -> (Accessory) g)
                .collect(Collectors.toList());

        if (!accessories.isEmpty()) {
            System.out.println("\n--- ACCESSORIES ---");
            for (Accessory acc : accessories) {
                System.out.println("  " + acc.displayInfo());
                totalValue += acc.getQuantity() * acc.getPrice();
                totalItems += acc.getQuantity();
            }
        }

        System.out.println("\n══════════════════════════════════════════");
        System.out.printf("Total Products: %d\n", games.size());
        System.out.printf("Total Units in Stock: %d\n", totalItems);
        System.out.printf("Total Inventory Value: $%.2f\n", totalValue);
    }

    // Search methods
    public List<Game> searchByName(String searchTerm) {
        return games.stream()
                .filter(g -> g.getName().toLowerCase().contains(searchTerm.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Game> searchByPlatform(String platform) {
        List<Game> results = new ArrayList<>();

        for (Game game : games) {
            if (game instanceof VideoGame &&
                    ((VideoGame) game).getPlatform().toLowerCase().contains(platform.toLowerCase())) {
                results.add(game);
            } else if (game instanceof BoardGame &&
                    ((BoardGame) game).getCategory().toLowerCase().contains(platform.toLowerCase())) {
                results.add(game);
            } else if (game instanceof Accessory &&
                    ((Accessory) game).getCompatibleWith().toLowerCase().contains(platform.toLowerCase())) {
                results.add(game);
            }
        }

        return results;
    }

    public List<Game> searchByPriceRange(double minPrice, double maxPrice) {
        return games.stream()
                .filter(g -> g.getPrice() >= minPrice && g.getPrice() <= maxPrice)
                .sorted((g1, g2) -> Double.compare(g1.getPrice(), g2.getPrice()))
                .collect(Collectors.toList());
    }

    public List<Game> getLowStockGames(int threshold) {
        return games.stream()
                .filter(g -> g.getQuantity() < threshold)
                .sorted((g1, g2) -> Integer.compare(g1.getQuantity(), g2.getQuantity()))
                .collect(Collectors.toList());
    }

    public void displayHistory() {
        caretaker.displayHistory();
    }
}