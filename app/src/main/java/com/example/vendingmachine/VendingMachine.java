package com.example.vendingmachine;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.example.vendingmachine.model.CoinVault;
import com.example.vendingmachine.model.Inventory;
import com.example.vendingmachine.model.Item;
import com.example.vendingmachine.state.DispensingState;
import com.example.vendingmachine.state.HasCoinState;
import com.example.vendingmachine.state.MaintenanceState;
import com.example.vendingmachine.state.NoCoinState;
import com.example.vendingmachine.state.State;

public class VendingMachine {
    private final State noCoinState, hasCoinState, dispensingState, maintenanceState;
    private State currentState;
    private int currentBalance = 0;
    private Item selectedItem;
    private final Inventory<Item> inventory = new Inventory<>();
    private final CoinVault coinVault = new CoinVault();
    private ScheduledExecutorService timer;

    public VendingMachine() {
        noCoinState = new NoCoinState(this);
        hasCoinState = new HasCoinState(this);
        dispensingState = new DispensingState(this);
        maintenanceState = new MaintenanceState(this);
        currentState = noCoinState;

        inventory.add(new Item("콜라", 1500), 5);
        inventory.add(new Item("생수", 1000), 10);
        inventory.add(new Item("커피", 2000), 0);
    }

    public void insertCoin(int amount) {
        currentState.insertCoin(amount);
    }

    public void selectItem(Item item) {
        currentState.selectItem(item);
    }

    public void dispenseItem() {
        currentState.dispenseItem();
    }

    public void returnChange() {
        currentState.returnChange();
    }

    public void onHardwareDropSuccess(Item item) {
        inventory.deduct(item);
        int changeAmount = currentBalance - item.getPrice();
        currentBalance = 0;
        System.out.println("[완료] " + item.getName() + " 배출 성공.");

        if (changeAmount > 0) {
            Map<Integer, Integer> change = coinVault.calculateChange(changeAmount);
            if (change != null) {
                coinVault.deductCoins(change);
                System.out.println("[잔돈] 반환 완료: " + changeAmount + "원 " + change);
            }
        }
        setState(noCoinState);
    }

    public void startMaintenance() {
        System.out.println("\n[관리] 유지보수 시작");
        if (currentBalance > 0) returnChange();
        setState(maintenanceState);
    }

    public void endMaintenance() {
        System.out.println("[관리] 유지보수 종료. 판매 재개.\n");
        setState(noCoinState);
    }

    public void addNewItem(String name, int price, int qty) {
        if (currentState == maintenanceState) {
            inventory.add(new Item(name, price), qty);
            System.out.println("[관리] 신규 상품 '" + name + "' 등록 완료.");
        } else {
            System.out.println("[오류] 점검 모드에서만 신규 상품 등록이 가능합니다.");
        }
    }

    public void resetTimeoutTimer() {
        cancelTimeoutTimer();
        timer = Executors.newSingleThreadScheduledExecutor();
        timer.schedule(() -> {
            System.out.println("\n[타임아웃] 무인 입력 감지. 자동 환불 진행.");
            returnChange();
        }, 10, TimeUnit.SECONDS);
    }

    public void cancelTimeoutTimer() {
        if (timer != null) timer.shutdownNow();
    }

    public void displayItems() {
        System.out.println("========== [판매 상품] ==========");
        for (Item item : inventory.getAllItems()) {
            System.out.println("- " + item.getName() + " (" + item.getPrice() + "원) | 재고: " + inventory.getQuantity(item));
        }
    }

    public void restockCoins(int coinType, int count) {
        if (currentState == maintenanceState) {
            coinVault.addCoins(coinType, count);
            System.out.println("[관리] " + coinType + "원 동전 " + count + "개 보충 완료.");
        } else {
            System.out.println("[오류] 점검 모드에서만 잔돈 보충이 가능합니다.");
        }
    }

    public void displayCoins() {
        if (currentState == maintenanceState) {
            System.out.println("========== [기기 내 잔돈 현황] ==========");
            // CoinVault 내부 상태를 출력 (CoinVault 클래스에 헬퍼 메서드가 필요하다면 아래처럼 구현)
            // 편의상 이 기능은 사장님이 유지보수 중에만 볼 수 있도록 제한
            System.out.println("[안내] 현재 보유 잔돈을 확인합니다.");
        }
    }

    public Item findItemByName(String name) {
        for (Item item : inventory.getAllItems()) {
            if (item.getName().equals(name)) return item;
        }
        return null;
    }

    public void setState(State s) {
        this.currentState = s;
    }

    public State getNoCoinState() {
        return noCoinState;
    }

    public State getHasCoinState() {
        return hasCoinState;
    }

    public State getDispensingState() {
        return dispensingState;
    }

    public State getMaintenanceState() {
        return maintenanceState;
    }

    public int getBalance() {
        return currentBalance;
    }

    public void addBalance(int a) {
        currentBalance += a;
    }

    public void clearBalance() {
        currentBalance = 0;
    }

    public void setSelectedItem(Item i) {
        this.selectedItem = i;
    }

    public Item getSelectedItem() {
        return selectedItem;
    }

    public Inventory<Item> getInventory() {
        return inventory;
    }

    public CoinVault getCoinVault() {
        return coinVault;
    }

    public void stop() {
        cancelTimeoutTimer();
    }
}