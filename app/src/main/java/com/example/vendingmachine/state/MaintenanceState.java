package com.example.vendingmachine.state;

import com.example.vendingmachine.VendingMachine;
import com.example.vendingmachine.model.Item;

public class MaintenanceState implements State {
    private final VendingMachine machine;

    public MaintenanceState(VendingMachine machine) { this.machine = machine; }

    @Override
    public void insertCoin(int amount) {
        System.out.println("[시스템] 현재 기기 점검 중입니다. (반환: " + amount + "원)");
    }

    @Override
    public void selectItem(Item item) {
        System.out.println("[시스템] 점검 중에는 상품을 선택할 수 없습니다.");
    }

    @Override
    public void dispenseItem() {
        System.out.println("[시스템] 기기 점검 중입니다.");
    }

    @Override
    public void returnChange() {
        System.out.println("[시스템] 점검 모드입니다.");
    }
}