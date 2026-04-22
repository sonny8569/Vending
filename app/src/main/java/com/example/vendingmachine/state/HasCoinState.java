package com.example.vendingmachine.state;

import com.example.vendingmachine.VendingMachine;
import com.example.vendingmachine.exception.NotFullPaidException;
import com.example.vendingmachine.exception.SoldOutException;
import com.example.vendingmachine.model.Item;

import java.util.Map;

public class HasCoinState implements State {
    private final VendingMachine machine;

    public HasCoinState(VendingMachine machine) { this.machine = machine; }

    @Override
    public void insertCoin(int amount) {
        machine.addBalance(amount);
        System.out.println("[입금] 추가 " + amount + "원 투입. (잔액: " + machine.getBalance() + "원)");
        machine.resetTimeoutTimer();
    }

    @Override
    public void selectItem(Item item) {
        machine.cancelTimeoutTimer();
        try {
            if (!machine.getInventory().hasItem(item)) {
                throw new SoldOutException(item.getName() + "은(는) 품절입니다.");
            }
            if (machine.getBalance() < item.getPrice()) {
                throw new NotFullPaidException("잔액 부족 (필요 금액: " + item.getPrice() + "원)");
            }

            // [핵심] 잔돈 반환 가능 여부 사전 검증
            int expectedChange = machine.getBalance() - item.getPrice();
            if (expectedChange > 0) {
                Map<Integer, Integer> changeResult = machine.getCoinVault().calculateChange(expectedChange);
                if (changeResult == null) {
                    System.out.println("[시스템 오류] 기기 내부 잔돈이 부족하여 결제할 수 없습니다.");
                    System.out.println("[안내] 투입하신 원금 전액을 반환합니다.");
                    returnChange(); // 전액 환불 후 NoCoinState로 강제 전이
                    return;
                }
            }

            System.out.println("[확인] 결제 가능. 상품 배출을 시작합니다.");
            machine.setSelectedItem(item);
            machine.setState(machine.getDispensingState());
            machine.dispenseItem();

        } catch (SoldOutException | NotFullPaidException e) {
            System.out.println("[거절] " + e.getMessage());
            machine.resetTimeoutTimer();
        }
    }

    @Override
    public void dispenseItem() {
        System.out.println("[알림] 상품을 먼저 선택해 주세요.");
    }

    @Override
    public void returnChange() {
        machine.cancelTimeoutTimer();
        int amount = machine.getBalance();
        machine.clearBalance();
        System.out.println("[반환] 투입 금액 " + amount + "원을 반환합니다.");
        machine.setState(machine.getNoCoinState());
    }
}