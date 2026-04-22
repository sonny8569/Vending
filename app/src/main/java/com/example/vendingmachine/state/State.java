package com.example.vendingmachine.state;

import com.example.vendingmachine.model.Item;

public interface State {
    void insertCoin(int amount);
    void selectItem(Item item);
    void dispenseItem();
    void returnChange();
}