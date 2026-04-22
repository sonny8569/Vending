package com.example.vendingmachine.state;

import com.example.vendingmachine.VendingMachine;
import com.example.vendingmachine.model.Item;

public class NoCoinState implements State {
    private final VendingMachine machine;

    public NoCoinState(VendingMachine machine) {
        this.machine = machine;
    }

    @Override
    public void insertCoin(int amount) {
        if (amount <= 0) {
            System.out.println("[시스템] 올바르지 않은 화폐입니다: " + amount + "원");
            return;
        }
        machine.addBalance(amount);
        System.out.println("[입력] " + amount + "원 투입. (총액: " + machine.getBalance() + "원)");
        machine.setState(machine.getHasCoinState());
        machine.resetTimeoutTimer();
    }

    @Override
    public void selectItem(Item item) {
        System.out.println("[안내] 먼저 동전이나 지폐를 투입해 주세요.");

    }

    @Override
    public void dispenseItem() {
        System.out.println("[오류] 결제가 진행되지 않았습니다.");
    }

    @Override
    public void returnChange() {
        System.out.println("[안내] 반환할 잔돈이 없습니다.");
    }
}