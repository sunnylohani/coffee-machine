package com.dunzo.coffeemachine.dao;

import com.dunzo.coffeemachine.model.InventoryResponse;
import com.dunzo.coffeemachine.util.Constants;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
public class Inventory {

    private final Map<String, Integer> totalItemsQty;
    private final Set<String> itemsSet;
    private final Map<String, Integer> initialItemsQty;

    public Inventory(Map<String, Integer> totalItemsQty) {
        this.totalItemsQty = new ConcurrentHashMap<>(totalItemsQty);
        this.itemsSet = totalItemsQty.keySet();
        this.initialItemsQty = totalItemsQty;
        log.debug("Inventory initialized");
    }

    public synchronized InventoryResponse transact(Map<String, Integer> ingredientsQty) {
        // Acquire lock on inventory
        List<String> unavailableItems = checkItemsAvailability(ingredientsQty.keySet());
        if (!unavailableItems.isEmpty()) {
            log.debug("Unavailable items: {}", unavailableItems);
            return new InventoryResponse(InventoryResponse.Result.FAILURE,
                    String.format("%s is/are not available", String.join(",", unavailableItems)));
        }
        List<String> insufficientItems = checkItemsQuantity(ingredientsQty);
        if (!insufficientItems.isEmpty()) {
            log.debug("Insufficient items: {}", insufficientItems);
            return new InventoryResponse(InventoryResponse.Result.FAILURE,
                    String.format("%s is/are not sufficient", String.join(",", insufficientItems)));
        }

        ingredientsQty.forEach((k, v) -> totalItemsQty.put(k, totalItemsQty.get(k) - v));
        return new InventoryResponse(InventoryResponse.Result.SUCCESS, null);
    }

    private List<String> checkItemsAvailability(Set<String> requiredItems) {
        return requiredItems.stream()
                .filter(item -> !itemsSet.contains(item))
                .collect(Collectors.toList());
    }

    private List<String> checkItemsQuantity(Map<String, Integer> ingredientsQty) {
        return ingredientsQty.entrySet().stream()
                .filter(entry -> totalItemsQty.get(entry.getKey()) < entry.getValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public List<String> getLowRunningItems() {
        return totalItemsQty.entrySet().stream()
                .filter(entry -> entry.getValue() < Constants.INGREDIENTS_LOW_THRESHOLD)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public synchronized void refillItem(String item) {
        if (initialItemsQty.containsKey(item)) {
            totalItemsQty.put(item, initialItemsQty.get(item));
            log.info("Ingredient {} refilled", item);
        } else {
            log.error("Ingredient {} not present. Cannot be refilled", item);
        }
    }

    // Only for testing
    public Integer getCurrentQuantity(String item) {
        return totalItemsQty.get(item);
    }
}
