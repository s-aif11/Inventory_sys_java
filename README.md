# GameStore Inventory System (Java)

A console-based inventory management system for a game store, built in Java
around the **Memento design pattern** for undo/rollback support.

> A separate C#/.NET implementation of the same concept exists at
> [`Inventory-System`](https://github.com/s-aif11/Inventory-System).

## Features

- **Catalog management** — add, update, sell, and restock items across three
  game types: `VideoGame`, `BoardGame`, and `Accessory`.
- **Undo/rollback** — every mutating action snapshots the inventory first, so
  the last operation can be rolled back on demand.
- **Inventory history** — view a log of past snapshots with descriptions and
  timestamps.
- **Trade operations** — batch multiple item quantity changes into a single
  "risky" transaction that rolls back automatically if it fails partway
  through.
- **Search** — by name, platform/type, price range, or low-stock threshold.

## Design pattern

Implements the **Memento** pattern:

- `Game` (abstract, `Cloneable`) — the base item type; `VideoGame`,
  `BoardGame`, and `Accessory` extend it with their own fields.
- `GameInventoryManager` — the *Originator*: owns the live inventory and can
  produce/restore snapshots of it.
- `InventoryMemento` — the *Memento*: an immutable, deep-cloned snapshot of
  inventory state at a point in time, tagged with a description and
  timestamp.
- `Main` — the *Caretaker*: keeps a `Stack<InventoryMemento>` of the last 10
  snapshots and drives rollback from the menu.
- `TradeOperation` — bundles multiple SKU quantity changes into one named
  transaction for the trade-games feature.

## Project structure

```
src/
├── Main.java                  # Console menu + application flow
├── manager/
│   └── GameInventoryManager.java   # Originator — inventory CRUD + rollback
├── memento/
│   ├── InventoryMemento.java       # Memento — snapshot of inventory state
│   └── InventoryCaretaker.java     # Snapshot history bookkeeping
└── model/
    ├── Game.java               # Abstract base item
    ├── VideoGame.java
    ├── BoardGame.java
    ├── Accessory.java
    └── TradeOperation.java     # Multi-item trade transaction
```

## Getting started

Requires a JDK (8+).

```bash
cd src
javac -d out Main.java model/*.java manager/*.java memento/*.java
java -cp out Main
```

Or open the project in IntelliJ IDEA / any IDE with Java support and run
`Main.java` directly.

On launch, the system preloads 12 sample items (video games, board games,
and accessories) so you can explore every menu option immediately.

## Usage

The app runs as an interactive text menu:

```
1. View All Games        6. Trade Games (Risky Operation)
2. Add New Game          7. Rollback Last Operation
3. Update Game Info      8. View Inventory History
4. Process Sale          9. Search Games
5. Restock Games         0. Exit
```

Every add/update/sale/restock/trade first saves a memento, so option 7
always undoes the most recent change.
