package model;

import java.util.HashMap;
import java.util.Map;

public class TradeOperation {
    private String tradeId;
    private String description;
    private Map<String, Integer> itemChanges;

    public TradeOperation(String tradeId, String description) {
        this.tradeId = tradeId;
        this.description = description;
        this.itemChanges = new HashMap<>();
    }

    public String getTradeId() { return tradeId; }
    public String getDescription() { return description; }
    public Map<String, Integer> getItemChanges() { return itemChanges; }

    public void addItemChange(String sku, int quantityChange) {
        itemChanges.put(sku, quantityChange);
    }
}