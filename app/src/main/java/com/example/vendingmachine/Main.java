package com.example.vendingmachine;

import com.example.vendingmachine.model.Item;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        VendingMachine vm = new VendingMachine();

        System.out.println("=== 시나리오: 사장님의 잔돈 보충과 성공적인 결제 ===");

        // 1. 점검 모드 진입 (현재 기기 내부의 동전을 완전히 비움)
        vm.startMaintenance();
        vm.getCoinVault().clearAllCoins();

        // 2. 사장님이 동전 보충 (100원짜리 10개, 500원짜리 5개)
        System.out.println("\n[사장님 액션] 기기에 거스름돈을 채워 넣습니다.");
        vm.restockCoins(500, 5);
        vm.restockCoins(100, 10);
        vm.displayCoins();

        // 3. 기기 내부 동전 상태 확인
        System.out.println("\n========== [기기 내 잔돈 현황] ==========");
        vm.getCoinVault().printCoinStatus();
        System.out.println("=====================================");

        vm.endMaintenance();

        // 4. 손님 결제 시도 (2000원 넣고 1500원 콜라 구매 -> 500원 거스름돈 발생)
        System.out.println("\n[손님 액션] 2000원을 투입하고 1500원 콜라를 구매합니다.");
        vm.insertCoin(1000);
        vm.insertCoin(1000);

        Item cola = vm.findItemByName("콜라");
        if (cola != null) {
            vm.selectItem(cola); // 500원 반환 성공!
        }

        vm.stop();
    }
}