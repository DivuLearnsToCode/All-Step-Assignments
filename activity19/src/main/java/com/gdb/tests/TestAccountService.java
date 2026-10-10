package com.gdb.tests;

import com.gdb.command.TransactionCommand;
import com.gdb.domain.IAccount;
import com.gdb.domain.Transaction;
import com.gdb.exceptions.AccountException;
import com.gdb.logging.MemoryLogDestination;
import com.gdb.logging.TransactionLogger;
import com.gdb.service.AccountService;

import java.util.List;

public class TestAccountService {

    public static void main(String[] args) throws Exception {
        System.out.println("============================================================");
        System.out.println("  ACTIVITY 19 - ACCOUNT SERVICE DEMO");
        System.out.println("============================================================");
        System.out.println();

        // 📝 STEP 12: memory backend keeps the test self-contained
        TransactionLogger logger = new TransactionLogger(new MemoryLogDestination());
        AccountService service = new AccountService(logger);

        // 📝 STEP 13
        IAccount john = service.openAccount("SAVINGS", "John Doe", 25, 15000.0);
        john.setPin("1234");
        IAccount jane = service.openAccount("SAVINGS", "Jane Smith", 30, 10000.0);
        jane.setPin("5678");
        System.out.println("[STEP 13] Opened: " + john.getSummary());
        System.out.println("[STEP 13] Opened: " + jane.getSummary());
        System.out.println();

        // 📝 STEP 14
        Transaction deposit = service.deposit(1001, 5000.0);
        System.out.println("[STEP 14] Deposit: " + deposit);

        // 📝 STEP 15
        Transaction withdrawal = service.withdraw(1001, 2000.0, "1234");
        System.out.println("[STEP 15] Withdrawal: " + withdrawal);

        // 📝 STEP 16
        Transaction transfer = service.transfer(1001, 1002, 1000.0, "1234");
        System.out.println("[STEP 16] Transfer: " + transfer);
        System.out.println();

        // 📝 STEP 17
        System.out.println("[STEP 17] Final Balances:");
        System.out.println("  John (Account #1001): Rs. " + service.getAccount(1001).getBalance());
        System.out.println("  Jane (Account #1002): Rs. " + service.getAccount(1002).getBalance());
        System.out.println();

        // 📝 STEP 18
        List<TransactionCommand> history = service.getTransactionHistory();
        System.out.println("[STEP 18] Transaction History (" + history.size() + " records):");
        for (int i = 0; i < history.size(); i++) {
            System.out.println("  [" + (i + 1) + "] " + history.get(i).getTransaction());
        }
        System.out.println();

        // 📝 STEP 19
        System.out.println("[STEP 19] Error Handling Checks:");
        try {
            service.deposit(9999, 100.0);
            System.out.println("  Deposit to missing account NOT caught [FAIL]");
        } catch (AccountException e) {
            boolean pass = e.getMessage().equals("Account not found: 9999");
            System.out.println("  Deposit to missing account caught: " + e.getMessage()
                    + " [" + (pass ? "PASS" : "FAIL") + "]");
        }
        try {
            service.withdraw(1001, 100.0, "9999");
            System.out.println("  Withdrawal with wrong PIN NOT caught [FAIL]");
        } catch (AccountException e) {
            boolean pass = e.getMessage().equals("Incorrect PIN");
            System.out.println("  Withdrawal with wrong PIN caught: " + e.getMessage()
                    + " [" + (pass ? "PASS" : "FAIL") + "]");
        }
    }
}
