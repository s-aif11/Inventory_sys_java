import java.util.List;
import java.util.Scanner;
import model.*;
import manager.GameInventoryManager;
import memento.InventoryMemento;
import java.util.Stack;

public class Main {
    private static GameInventoryManager inventory = new GameInventoryManager();
    private static Stack<InventoryMemento> history = new Stack<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initializeSampleGames();

        boolean exit = false;

        while (!exit) {
            displayHeader();
            displayMainMenu();

            System.out.print("\nEnter your choice (0-9): ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1": viewAllGames(); break;
                case "2": addNewGame(); break;
                case "3": updateGame(); break;
                case "4": processSale(); break;
                case "5": processRestock(); break;
                case "6": tradeGames(); break;
                case "7": rollbackLastOperation(); break;
                case "8": viewInventoryHistory(); break;
                case "9": searchGames(); break;
                case "0":
                    exit = true;
                    System.out.println("\nThank you for using GameStore Inventory System!");
                    System.out.println("Press Enter to exit...");
                    scanner.nextLine();
                    break;
                default:
                    System.out.println("\n❌ Invalid choice! Press Enter to continue...");
                    scanner.nextLine();
                    break;
            }
        }
        scanner.close();
    }

    private static void displayHeader() {
        System.out.println("==============================================");
        System.out.println("       GAME STORE INVENTORY SYSTEM");
        System.out.println("       Memento Pattern + LSP Principle");
        System.out.println("==============================================\n");
    }

    private static void displayMainMenu() {
        System.out.println("MAIN MENU:");
        System.out.println("1. View All Games");
        System.out.println("2. Add New Game");
        System.out.println("3. Update Game Information");
        System.out.println("4. Process Sale");
        System.out.println("5. Restock Games");
        System.out.println("6. Trade Games (Risky Operation)");
        System.out.println("7. Rollback Last Operation");
        System.out.println("8. View Inventory History");
        System.out.println("9. Search Games");
        System.out.println("0. Exit");
        System.out.println("==============================================");
    }

    private static void initializeSampleGames() {
        System.out.println("Loading sample games...\n");

        // Video Games
        inventory.addGame(new VideoGame("VG001", "Cyberpunk 2077", 25, 59.99,
                "CD Projekt Red", "RPG", "PS5"));
        inventory.addGame(new VideoGame("VG002", "Elden Ring", 30, 69.99,
                "FromSoftware", "Action RPG", "PS5"));
        inventory.addGame(new VideoGame("VG003", "The Legend of Zelda: Tears", 20, 69.99,
                "Nintendo", "Adventure", "Switch"));
        inventory.addGame(new VideoGame("VG004", "Call of Duty: Modern Warfare III", 35, 69.99,
                "Infinity Ward", "FPS", "Xbox Series X"));

        // Board Games
        inventory.addGame(new BoardGame("BG001", "Catan", 15, 49.99, 3, 4, "Strategy"));
        inventory.addGame(new BoardGame("BG002", "Ticket to Ride", 18, 44.99, 2, 5, "Family"));
        inventory.addGame(new BoardGame("BG003", "Carcassonne", 22, 39.99, 2, 5, "Tile Placement"));
        inventory.addGame(new BoardGame("BG004", "Pandemic", 14, 59.99, 2, 4, "Cooperative"));
        inventory.addGame(new BoardGame("BG005", "Codenames", 25, 24.99, 2, 8, "Party"));

        // Accessories
        inventory.addGame(new Accessory("ACC001", "DualSense Controller", 40, 69.99, "PS5", "Controller"));
        inventory.addGame(new Accessory("ACC002", "Xbox Wireless Headset", 25, 99.99, "Xbox Series X", "Audio"));
        inventory.addGame(new Accessory("ACC003", "Nintendo Switch Pro Controller", 30, 69.99, "Switch", "Controller"));

        System.out.println("✅ 12 sample games loaded successfully!\n");
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
    }

    private static void viewAllGames() {
        clearScreen();
        displayHeader();
        System.out.println("INVENTORY OVERVIEW:\n");
        inventory.displayInventory();
        waitForUser();
    }

    private static void addNewGame() {
        clearScreen();
        displayHeader();
        System.out.println("ADD NEW GAME:\n");

        System.out.println("Select game type:");
        System.out.println("1. Video Game");
        System.out.println("2. Board Game");
        System.out.println("3. Accessory");
        System.out.print("\nChoice: ");

        String typeChoice = scanner.nextLine();

        try {
            System.out.print("\nEnter SKU: ");
            String sku = scanner.nextLine();

            if (inventory.getGame(sku) != null) {
                System.out.printf("\n❌ SKU %s already exists!\n", sku);
                waitForUser();
                return;
            }

            System.out.print("Enter Name: ");
            String name = scanner.nextLine();

            System.out.print("Enter Quantity: ");
            int quantity = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter Price: ");
            double price = Double.parseDouble(scanner.nextLine());

            Game newGame = null;

            switch (typeChoice) {
                case "1":
                    System.out.print("Developer: ");
                    String developer = scanner.nextLine();
                    System.out.print("Genre: ");
                    String genre = scanner.nextLine();
                    System.out.print("Platform (PS5/Xbox/Switch/PC): ");
                    String platform = scanner.nextLine();
                    newGame = new VideoGame(sku, name, quantity, price, developer, genre, platform);
                    break;

                case "2":
                    System.out.print("Min Players: ");
                    int minPlayers = Integer.parseInt(scanner.nextLine());
                    System.out.print("Max Players: ");
                    int maxPlayers = Integer.parseInt(scanner.nextLine());
                    System.out.print("Category: ");
                    String category = scanner.nextLine();
                    newGame = new BoardGame(sku, name, quantity, price, minPlayers, maxPlayers, category);
                    break;

                case "3":
                    System.out.print("Compatible With: ");
                    String compatible = scanner.nextLine();
                    System.out.print("Type (Controller/Headset/Charger/etc): ");
                    String accType = scanner.nextLine();
                    newGame = new Accessory(sku, name, quantity, price, compatible, accType);
                    break;

                default:
                    System.out.println("Invalid type selected.");
                    waitForUser();
                    return;
            }

            saveInventoryState("Before adding new game: " + name);
            inventory.addGame(newGame);
            System.out.printf("\n✅ Successfully added %s to inventory!\n", name);
            System.out.println(newGame.displayInfo());

        } catch (NumberFormatException e) {
            System.out.println("\n❌ Invalid number format!");
        } catch (Exception e) {
            System.out.printf("\n❌ Error: %s\n", e.getMessage());
        }

        waitForUser();
    }

    private static void updateGame() {
        clearScreen();
        displayHeader();
        System.out.println("UPDATE GAME INFORMATION:\n");

        System.out.print("Enter Game SKU to update: ");
        String sku = scanner.nextLine();

        Game game = inventory.getGame(sku);
        if (game == null) {
            System.out.printf("\n❌ Game with SKU %s not found!\n", sku);
            waitForUser();
            return;
        }

        System.out.printf("\nCurrent Information:\n%s\n", game.displayInfo());

        try {
            saveInventoryState("Before updating " + game.getName());

            System.out.print("\nEnter new quantity (press Enter to keep current): ");
            String qtyInput = scanner.nextLine();
            if (!qtyInput.isEmpty()) {
                int newQty = Integer.parseInt(qtyInput);
                if (newQty < 0) {
                    System.out.println("❌ Quantity cannot be negative!");
                    waitForUser();
                    return;
                }
                game.updateQuantity(newQty);
            }

            System.out.print("Enter new price (press Enter to keep current): ");
            String priceInput = scanner.nextLine();
            if (!priceInput.isEmpty()) {
                double newPrice = Double.parseDouble(priceInput);
                if (newPrice < 0) {
                    System.out.println("❌ Price cannot be negative!");
                    waitForUser();
                    return;
                }
                game.setPrice(newPrice);
            }

            System.out.printf("\n✅ Successfully updated %s!\n", game.getName());
            System.out.println("\nUpdated Information:");
            System.out.println(game.displayInfo());

        } catch (NumberFormatException e) {
            System.out.println("\n❌ Invalid number format!");
        } catch (Exception e) {
            System.out.printf("\n❌ Error: %s\n", e.getMessage());
        }

        waitForUser();
    }

    private static void processSale() {
        clearScreen();
        displayHeader();
        System.out.println("PROCESS SALE:\n");

        System.out.print("Enter Game SKU to sell: ");
        String sku = scanner.nextLine();

        Game game = inventory.getGame(sku);
        if (game == null) {
            System.out.printf("\n❌ Game with SKU %s not found!\n", sku);
            waitForUser();
            return;
        }

        System.out.printf("\nGame: %s\n", game.getName());
        System.out.printf("Available Quantity: %d\n", game.getQuantity());
        System.out.printf("Price: $%.2f\n", game.getPrice());

        System.out.print("\nEnter quantity to sell: ");

        try {
            int saleQty = Integer.parseInt(scanner.nextLine());

            if (saleQty <= 0) {
                System.out.println("\n❌ Quantity must be positive!");
                waitForUser();
                return;
            }

            if (saleQty > game.getQuantity()) {
                System.out.printf("\n❌ Not enough stock! Only %d available.\n", game.getQuantity());
                waitForUser();
                return;
            }

            saveInventoryState(String.format("Before selling %d of %s", saleQty, game.getName()));

            int newQty = game.getQuantity() - saleQty;
            game.updateQuantity(newQty);

            double totalSale = saleQty * game.getPrice();

            System.out.println("\n✅ Sale processed successfully!");
            System.out.printf("Sold: %d x %s\n", saleQty, game.getName());
            System.out.printf("Unit Price: $%.2f\n", game.getPrice());
            System.out.printf("Total: $%.2f\n", totalSale);
            System.out.printf("Remaining stock: %d\n", newQty);

        } catch (NumberFormatException e) {
            System.out.println("\n❌ Invalid quantity format!");
        } catch (Exception e) {
            System.out.printf("\n❌ Error: %s\n", e.getMessage());
        }

        waitForUser();
    }

    private static void processRestock() {
        clearScreen();
        displayHeader();
        System.out.println("RESTOCK GAMES:\n");

        System.out.print("Enter Game SKU to restock: ");
        String sku = scanner.nextLine();

        Game game = inventory.getGame(sku);
        if (game == null) {
            System.out.printf("\n❌ Game with SKU %s not found!\n", sku);
            waitForUser();
            return;
        }

        System.out.printf("\nGame: %s\n", game.getName());
        System.out.printf("Current Quantity: %d\n", game.getQuantity());

        System.out.print("\nEnter quantity to add: ");

        try {
            int restockQty = Integer.parseInt(scanner.nextLine());

            if (restockQty <= 0) {
                System.out.println("\n❌ Quantity must be positive!");
                waitForUser();
                return;
            }

            saveInventoryState(String.format("Before restocking %d of %s", restockQty, game.getName()));

            int newQty = game.getQuantity() + restockQty;
            game.updateQuantity(newQty);

            System.out.println("\n✅ Restock successful!");
            System.out.printf("Added: %d x %s\n", restockQty, game.getName());
            System.out.printf("Old quantity: %d\n", newQty - restockQty);
            System.out.printf("New total: %d\n", newQty);

        } catch (NumberFormatException e) {
            System.out.println("\n❌ Invalid quantity format!");
        } catch (Exception e) {
            System.out.printf("\n❌ Error: %s\n", e.getMessage());
        }

        waitForUser();
    }

    private static void tradeGames() {
        clearScreen();
        displayHeader();
        System.out.println("TRADE GAMES (Risky Operation):\n");

        System.out.println("This operation allows trading multiple games at once.");
        System.out.println("The system will automatically rollback if any trade fails.\n");

        System.out.print("Enter trade description: ");
        String description = scanner.nextLine();

        if (description.isEmpty()) {
            System.out.println("\n❌ Description is required!");
            waitForUser();
            return;
        }

        TradeOperation tradeOperation = new TradeOperation(
                "TRADE" + System.currentTimeMillis(), description);

        boolean addingItems = true;
        while (addingItems) {
            System.out.print("\nEnter Game SKU (or 'done' to finish): ");
            String sku = scanner.nextLine();

            if (sku.equalsIgnoreCase("done")) break;

            Game game = inventory.getGame(sku);
            if (game == null) {
                System.out.println("❌ Game not found! Try again.");
                continue;
            }

            System.out.printf("Game: %s, Available: %d\n", game.getName(), game.getQuantity());
            System.out.print("Quantity (+ to add, - to remove from inventory): ");

            try {
                int qtyChange = Integer.parseInt(scanner.nextLine());
                tradeOperation.addItemChange(sku, qtyChange);
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid quantity format! Try again.");
                continue;
            }

            System.out.print("Add another item? (y/n): ");
            addingItems = scanner.nextLine().equalsIgnoreCase("y");
        }

        if (tradeOperation.getItemChanges().isEmpty()) {
            System.out.println("\n❌ No items added to trade!");
            waitForUser();
            return;
        }

        // Display trade summary
        System.out.println("\n══════════════════════════════════════════");
        System.out.println("TRADE SUMMARY:");
        System.out.printf("ID: %s\n", tradeOperation.getTradeId());
        System.out.printf("Description: %s\n", tradeOperation.getDescription());
        System.out.printf("Items: %d\n", tradeOperation.getItemChanges().size());
        System.out.println("------------------------------------------");

        for (var entry : tradeOperation.getItemChanges().entrySet()) {
            Game game = inventory.getGame(entry.getKey());
            String action = entry.getValue() > 0 ? "Adding" : "Removing";
            System.out.printf("  %s %d x %s\n", action, Math.abs(entry.getValue()), game.getName());
        }

        System.out.print("\nExecute this trade? (y/n): ");
        if (!scanner.nextLine().equalsIgnoreCase("y")) {
            System.out.println("\nTrade cancelled.");
            waitForUser();
            return;
        }

        System.out.println("\nExecuting trade...");
        boolean success = inventory.executeRiskyTrade(tradeOperation);

        if (success) {
            System.out.println("\n✅ Trade completed successfully!");
        } else {
            System.out.println("\n❌ Trade failed and was rolled back.");
        }

        waitForUser();
    }

    private static void rollbackLastOperation() {
        clearScreen();
        displayHeader();
        System.out.println("ROLLBACK LAST OPERATION:\n");

        if (history.isEmpty()) {
            System.out.println("❌ No operations to rollback!");
            waitForUser();
            return;
        }

        InventoryMemento lastMemento = history.peek();
        System.out.printf("Last operation: %s\n", lastMemento.getDescription());
        System.out.printf("Saved at: %s\n", lastMemento.getSnapshotDate());

        System.out.print("\nAre you sure you want to rollback? (y/n): ");
        if (!scanner.nextLine().equalsIgnoreCase("y")) {
            System.out.println("Rollback cancelled.");
            waitForUser();
            return;
        }

        history.pop();
        inventory.rollback();
        System.out.println("\n✅ Successfully rolled back to previous state!");

        waitForUser();
    }

    private static void viewInventoryHistory() {
        clearScreen();
        displayHeader();
        System.out.println("INVENTORY HISTORY:\n");
        inventory.displayHistory();
        waitForUser();
    }

    private static void searchGames() {
        clearScreen();
        displayHeader();
        System.out.println("SEARCH GAMES:\n");

        System.out.println("Search by:");
        System.out.println("1. Name");
        System.out.println("2. Platform/Type");
        System.out.println("3. Price Range");
        System.out.println("4. Low Stock (less than 10)");
        System.out.print("\nChoice: ");

        String choice = scanner.nextLine();
        List<Game> results = null;

        try {
            switch (choice) {
                case "1":
                    System.out.print("Enter search term: ");
                    String term = scanner.nextLine();
                    results = inventory.searchByName(term);
                    break;

                case "2":
                    System.out.print("Enter platform/type (PS5/Xbox/Switch/Board/Accessory): ");
                    String platform = scanner.nextLine();
                    results = inventory.searchByPlatform(platform);
                    break;

                case "3":
                    System.out.print("Minimum price: ");
                    double min = Double.parseDouble(scanner.nextLine());
                    System.out.print("Maximum price: ");
                    double max = Double.parseDouble(scanner.nextLine());
                    results = inventory.searchByPriceRange(min, max);
                    break;

                case "4":
                    results = inventory.getLowStockGames(10);
                    break;

                default:
                    System.out.println("❌ Invalid choice!");
                    waitForUser();
                    return;
            }

            System.out.printf("\nFound %d game(s):\n\n", results.size());

            if (results.isEmpty()) {
                System.out.println("No games found.");
            } else {
                for (Game game : results) {
                    System.out.println(game.displayInfo());
                    System.out.println();
                }
            }

        } catch (NumberFormatException e) {
            System.out.println("\n❌ Invalid input format!");
        } catch (Exception e) {
            System.out.printf("\n❌ Error: %s\n", e.getMessage());
        }

        waitForUser();
    }

    private static void saveInventoryState(String description) {
        InventoryMemento memento = inventory.createMemento(description);
        history.push(memento);

        // Keep only last 10 states
        if (history.size() > 10) {
            Stack<InventoryMemento> temp = new Stack<>();
            for (int i = 0; i < 10; i++) {
                temp.push(history.pop());
            }
            history = temp;
        }
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void waitForUser() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
}