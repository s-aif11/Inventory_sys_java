package memento;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import model.Game;
import manager.GameInventoryManager;

public class InventoryMemento {
    private LocalDateTime snapshotDate;
    private String description;
    private GameInventoryManager originator;
    private List<Game> state;

    public InventoryMemento(GameInventoryManager originator, List<Game> state, String description) {
        this.snapshotDate = LocalDateTime.now();
        this.description = description;
        this.originator = originator;

        // Deep clone the state
        this.state = new ArrayList<>();
        for (Game game : state) {
            this.state.add(game.clone());
        }
    }

    public LocalDateTime getSnapshotDate() { return snapshotDate; }
    public String getDescription() { return description; }

    public void restore() {
        List<Game> clonedState = new ArrayList<>();
        for (Game game : state) {
            clonedState.add(game.clone());
        }
        originator.restoreState(clonedState);
    }
}