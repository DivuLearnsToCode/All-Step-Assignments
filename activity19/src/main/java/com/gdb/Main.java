package com.gdb;

import com.gdb.domain.IAccount;
import com.gdb.domain.Transaction;
import com.gdb.logging.FileLogDestination;
import com.gdb.logging.TransactionLogger;
import com.gdb.service.AccountService;

/** Activity 19 demo: the service wired to the FILE backend. */
public class Main {

    public static void main(String[] args) throws Exception {
        // 📝 STEP 20
        TransactionLogger logger = new TransactionLogger(new FileLogDestination());
        logger.clear(); // fresh log for a repeatable demo run
        AccountService service = new AccountService(logger);

        // 📝 STEP 21
        IAccount john = service.openAccount("SAVINGS", "John Doe", 25, 15000.0);
        john.setPin("1234");
        IAccount jane = service.openAccount("SAVINGS", "Jane Smith", 30, 10000.0);
        jane.setPin("5678");
        System.out.println("Opened: " + john.getSummary());
        System.out.println("Opened: " + jane.getSummary());

        Transaction t1 = service.deposit(1001, 5000.0);
        Transaction t2 = service.withdraw(1001, 2000.0, "1234");
        Transaction t3 = service.transfer(1001, 1002, 1000.0, "1234");
        System.out.println(t1);
        System.out.println(t2);
        System.out.println(t3);

        System.out.println("Logged to " + logger.getDestinationName() + ": "
                + service.getTransactionHistory().size() + " records");
    }
}
