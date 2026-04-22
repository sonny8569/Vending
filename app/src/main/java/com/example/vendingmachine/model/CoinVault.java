package com.example.vendingmachine.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class CoinVault {
    private final Map<Integer, Integer> coins = new TreeMap<>(Collections.reverseOrder());

    public CoinVault() {
        coins.put(500, 10);
        coins.put(100, 20);
    }

    public Map<Integer, Integer> calculateChange(int amount) {
        Map<Integer, Integer> changeResult = new HashMap<>();
        int remaining = amount;

        for (int coinType : coins.keySet()) {
            int count = coins.get(coinType);
            int needed = remaining / coinType;
            int actual = Math.min(needed, count);

            if (actual > 0) {
                changeResult.put(coinType, actual);
                remaining -= (coinType * actual);
            }
        }

        if (remaining > 0) return null;
        return changeResult;
    }

    public void deductCoins(Map<Integer, Integer> dispensedCoins) {
        for (Map.Entry<Integer, Integer> entry : dispensedCoins.entrySet()) {
            int coinType = entry.getKey();
            int deductCount = entry.getValue();
            coins.put(coinType, coins.get(coinType) - deductCount);
        }
    }
    public void printCoinStatus() {
        for (Map.Entry<Integer, Integer> entry : coins.entrySet()) {
            System.out.println("- " + entry.getKey() + "원: " + entry.getValue() + "개");
        }
    }
    public void clearAllCoins() {
        coins.clear();
    }

    public void addCoins(int coinType, int count) {
        coins.put(coinType, coins.getOrDefault(coinType, 0) + count);
    }
}