package com.example.vendingmachine.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Inventory<T> {
    private final Map<T, Integer> inventory = new HashMap<>();

    public int getQuantity(T item) { return inventory.getOrDefault(item, 0); }

    public void add(T item, int quantity) {
        int count = inventory.getOrDefault(item, 0);
        inventory.put(item, count + quantity);
    }

    public void deduct(T item) {
        if (hasItem(item)) {
            int count = inventory.get(item);
            inventory.put(item, count - 1);
        }
    }

    public boolean hasItem(T item) { return getQuantity(item) > 0; }

    public Set<T> getAllItems() { return inventory.keySet(); }

    public void clear() { inventory.clear(); }
}