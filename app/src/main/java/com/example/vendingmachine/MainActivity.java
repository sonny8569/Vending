package com.example.vendingmachine;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vendingmachine.model.Item;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Thread(this::runComprehensiveTest).start();
    }

    private void runComprehensiveTest() {
        System.out.println("\n========== [자판기 통합 테스트 시작] ==========");
        VendingMachine vm = new VendingMachine();

        try {
            System.out.println("\n=== [케이스 1] 점검 모드: 초기 잔돈 세팅 및 신규 상품 등록 ===");
            vm.startMaintenance();
            vm.getCoinVault().clearAllCoins(); // 기존 동전 완전 초기화
            vm.restockCoins(500, 2);           // 500원 2개
            vm.restockCoins(100, 5);           // 100원 5개 보충 (총 1500원어치 잔돈)

            vm.addNewItem("에너지바", 2500, 5); // 신규 상품 등록

            System.out.print("[확인] ");
            vm.displayCoins();
            vm.endMaintenance();
            vm.displayItems();                 // 전체 메뉴판 확인

            System.out.println("\n=== [케이스 2] 단순 변심 환불 ===");
            vm.insertCoin(1000);
            vm.insertCoin(500);
            System.out.println("[액션] 상품을 고르지 않고 반환 레버 작동");
            vm.returnChange();

            System.out.println("\n=== [케이스 3] 정상 구매 및 그리디 알고리즘 잔돈 반환 ===");
            vm.insertCoin(1000);
            vm.insertCoin(1000);
            Item cola = vm.findItemByName("콜라");
            System.out.println("[액션] 2000원 투입 후 1500원 콜라 선택");
            if (cola != null) vm.selectItem(cola);

            System.out.println("\n=== [케이스 4] 예외: 잔액 부족 ===");
            vm.insertCoin(1000);
            Item energyBar = vm.findItemByName("에너지바");
            System.out.println("[액션] 1000원 투입 후 2500원 에너지바 선택 시도");
            if (energyBar != null) vm.selectItem(energyBar);
            vm.returnChange();

            System.out.println("\n=== [케이스 5] 예외: 품절 상품 선택 ===");
            vm.insertCoin(3000);
            Item coffee = vm.findItemByName("커피");
            System.out.println("[액션] 3000원 투입 후 품절된 커피(재고 0) 선택 시도");
            if (coffee != null) vm.selectItem(coffee);

            System.out.println("\n=== [케이스 6] 예외: 잔돈 부족으로 인한 결제 롤백 (전액 환불) ===");
            System.out.println("[액션] 현재 잔액 3000원 유지 상태에서 1500원 콜라 선택 시도 (1500원 거스름돈 필요)");
            if (cola != null) vm.selectItem(cola);

            System.out.println("\n=== [케이스 7] 예외: 무인 입력 타임아웃 감지 (10초 대기) ===");
            vm.insertCoin(1000);
            System.out.println("[액션] 돈만 넣고 11초간 방치...");
            Thread.sleep(11000);

            System.out.println("\n========== [자판기 통합 테스트 완료] ==========");

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            vm.stop();
        }
    }
}