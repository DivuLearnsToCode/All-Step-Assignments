package com.gdb.tests;

import com.gdb.command.DepositCommand;
import com.gdb.command.TransactionCommand;
import com.gdb.command.TransferCommand;
import com.gdb.command.WithdrawCommand;
import com.gdb.domain.Account;
import com.gdb.factory.AccountFactory;
import com.gdb.logging.TransactionLog;

import java.util.List;

public class TestCommandLogging {

    public static void main(String[] args) throws Exception {
        System.out.println("============================================================");
        System.out.println("  ACTIVITY 17 - COMMAND PATTERN + FILE LOGGING");
        System.out.println("============================================================");
        System.out.println();

        // 📝 STEP 10: fresh log + two accounts
        TransactionLog log = new TransactionLog();
        log.clear();

        Account acc1 = (Account) AccountFactory.createAccount(
                "SAVINGS", 1001, "Rajesh Sharma", 30, 0, 15000.0, "1234");
        Account acc2 = (Account) AccountFactory.createAccount(
                "SAVINGS", 1002, "Priya Patel", 28, 0, 20000.0, "0000");
        System.out.println();

        // 📝 STEP 11
        TransactionCommand deposit = new DepositCommand(acc1, 5000.0);
        deposit.execute();
        log.log(deposit);
        System.out.println("[STEP 11] Logged: " + deposit.getTransaction());

        // 📝 STEP 12
        TransactionCommand withdraw = new WithdrawCommand(acc1, 2000.0, 1234);
        withdraw.execute();
        log.log(withdraw);
        System.out.println("[STEP 12] Logged: " + withdraw.getTransaction());

        // 📝 STEP 13
        TransactionCommand transfer = new TransferCommand(acc1, acc2, 3000.0, "1234");
        transfer.execute();
        log.log(transfer);
        System.out.println("[STEP 13] Logged: " + transfer.getTransaction());
        System.out.println();

        // 📝 STEP 14
        List<TransactionCommand> history = log.readAll();
        System.out.println("[STEP 14] Read " + history.size() + " commands from transaction log:");
        for (int i = 0; i < history.size(); i++) {
            System.out.println("  [" + (i + 1) + "] " + history.get(i).getTransaction());
        }
        System.out.println();

        // 📝 STEP 15: a brand-new TransactionLog instance proves the data lives in the file
        List<TransactionCommand> persisted = new TransactionLog().readAll();
        boolean pass = persisted.size() == 3;
        System.out.println("[STEP 15] Fresh reader verified " + persisted.size()
                + " persisted commands [" + (pass ? "PASS" : "FAIL") + "]");
    }
}
