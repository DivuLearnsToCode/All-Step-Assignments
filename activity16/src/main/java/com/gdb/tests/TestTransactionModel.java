package com.gdb.tests;

import com.gdb.domain.Account;
import com.gdb.domain.AccountRulesEngine;
import com.gdb.domain.Transaction;
import com.gdb.factory.AccountFactory;
import com.gdb.service.TransferService;

public class TestTransactionModel {

    public static void main(String[] args) throws Exception {
        System.out.println("============================================================");
        System.out.println("  ACTIVITY 16 - TRANSACTION MODEL TEST");
        System.out.println("============================================================");
        System.out.println();

        // Triggers the AccountRulesEngine Singleton's one-time load + its own banner.
        AccountRulesEngine.getInstance();
        System.out.println();

        Account acc1 = (Account) AccountFactory.createAccount(
                "SAVINGS", 1001, "Rajesh Sharma", 30, 0, 50000.0, "1234");
        Account acc2 = (Account) AccountFactory.createAccount(
                "SAVINGS", 1002, "Priya Patel", 28, 0, 20000.0, "0000");

        // 📝 STEP 10
        Transaction depositTxn = acc1.depositWithTransaction(5000.0);
        System.out.println("[STEP 10] Deposit Transaction: " + depositTxn);

        // 📝 STEP 11
        Transaction withdrawTxn = acc1.withdrawWithTransaction(2000.0, 1234);
        System.out.println("[STEP 11] Withdrawal Transaction: " + withdrawTxn);

        // 📝 STEP 12
        TransferService transferService = new TransferService();
        Transaction transferTxn = transferService.transferWithTransaction(acc1, acc2, 1000.0, "1234");
        System.out.println("[STEP 12] Transfer Transaction: " + transferTxn);

        // 📝 STEP 13: legacy deposit(amount) still works untouched, returning void as before.
        acc1.deposit(1000.0);
        System.out.print("[STEP 13] Legacy Deposit +1000: ");
        acc1.displayAccountInfo();
    }
}
