package memento;

import java.util.Stack;

public class InventoryCaretaker {
    private Stack<InventoryMemento> history = new Stack<>();

    public void push(InventoryMemento memento) {
        history.push(memento);
    }

    public InventoryMemento pop() {
        return history.pop();
    }

    public boolean canRollback() {
        return !history.isEmpty();
    }

    public int getHistoryCount() {
        return history.size();
    }

    public void displayHistory() {
        System.out.println("\n=== INVENTORY HISTORY ===");
        if (history.isEmpty()) {
            System.out.println("No history available.");
            return;
        }

        // Convert to array and reverse
        InventoryMemento[] historyArray = history.toArray(new InventoryMemento[0]);
        int index = 1;
        for (int i = historyArray.length - 1; i >= 0; i--) {
            InventoryMemento m = historyArray[i];
            System.out.printf("%d. %s\n", index++, m.getDescription());
            System.out.printf("   Time: %s\n\n", m.getSnapshotDate());
        }
    }
}