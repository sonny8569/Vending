package com.example.vendingmachine.state;

import com.example.vendingmachine.VendingMachine;
import com.example.vendingmachine.model.Item;

public class DispensingState implements State {
    private final VendingMachine machine;

    public DispensingState(VendingMachine machine) { this.machine = machine; }

    @Override
    public void insertCoin(int amount) {
        System.out.println("[안내] 상품 배출 중입니다. 잠시 대기해 주세요.");
    }

    @Override
    public void selectItem(Item item) {
        System.out.println("[안내] 이미 상품을 선택하셨습니다. 배출 중입니다.");
    }

    @Override
    public void dispenseItem() {
        System.out.println("[동작] 모터 가동... " + machine.getSelectedItem().getName() + " 낙하 중...");
        machine.onHardwareDropSuccess(machine.getSelectedItem());
    }

    @Override
    public void returnChange() {
        System.out.println("[오류] 상품 배출 중에는 취소할 수 없습니다.");
    }
}