package com.gdb.tests;

import com.gdb.domain.Account;
import com.gdb.domain.AccountRulesEngine;
import com.gdb.domain.IAccount;
import com.gdb.exceptions.AccountException;
import com.gdb.exceptions.InsufficientBalanceException;
import com.gdb.factory.AccountFactory;
import com.gdb.service.TransferService;

public class TestTransfer {

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("  ACTIVITY 15 - TRANSFER WITH DAILY LIMITS");
        System.out.println("============================================================");
        System.out.println();

        // Triggers the AccountRulesEngine Singleton's one-time load + its own banner.
        AccountRulesEngine.getInstance();
        System.out.println();

        try {
            // 📝 STEP 9
            IAccount acc1raw = AccountFactory.createAccount(
                    "SAVINGS", 1001, "Rajesh Sharma", 30, 0, 100000.0, "1234");
            IAccount acc2raw = AccountFactory.createAccount(
                    "SAVINGS", 1002, "Priya Patel", 28, 0, 20000.0, "0000");
            Account acc1 = (Account) acc1raw;
            Account acc2 = (Account) acc2raw;

            System.out.print("[STEP 9] ");
            acc1.displayAccountInfo();
            System.out.print("[STEP 9] ");
            acc2.displayAccountInfo();
            System.out.println();

            TransferService transferService = new TransferService();

            // 📝 STEP 10
            transferService.transfer(acc1, acc2, 5000.0, "1234");
            System.out.println("[STEP 10] Transfer Rs. 5,000: SUCCESS | acc1 = Rs. " + acc1.getBalance() +
                    " | acc2 = Rs. " + acc2.getBalance());

            // 📝 STEP 11
            try {
                transferService.transfer(acc1, acc2, 100000.0, "1234");
            } catch (InsufficientBalanceException e) {
                System.out.println("[STEP 11] Caught InsufficientBalanceException: " + e.getMessage());
            }

            // 📝 STEP 12
            double dailyLimit = acc1.getDailyTransferLimit();
            System.out.println("[STEP 12] Daily limit for acc1: Rs. " + dailyLimit);

            int attempt = 0;
            while (true) {
                attempt++;
                try {
                    transferService.transfer(acc1, acc2, 20000.0, "1234");
                    System.out.println("  Transfer #" + attempt + " of Rs. 20,000: SUCCESS | used today = Rs. " +
                            acc1.getDailyTransferTotal());
                } catch (AccountException e) {
                    System.out.println("[STEP 12] Caught AccountException: " + e.getMessage());
                    break;
                }
            }

            // 📝 STEP 13
            System.out.println("[STEP 13] Used today: Rs. " + acc1.getDailyTransferTotal() +
                    " | Remaining: Rs. " + acc1.getRemainingDailyTransferLimit());

        } catch (AccountException e) {
            System.out.println("Unexpected failure: " + e.getMessage());
        }
    }
}
