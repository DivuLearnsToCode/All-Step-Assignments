package com.gdb;

import com.gdb.domain.AccountRulesEngine;
import com.gdb.logging.FileLogDestination;
import com.gdb.logging.LogDestination;
import com.gdb.logging.TransactionLogger;
import com.gdb.repository.RepositoryFactory;
import com.gdb.service.AccountService;
import com.gdb.ui.AccountUI;

public class Main {

    public static void main(String[] args) {
        System.out.println("Booting Global Digital Bank...");

        // Load the rules now so their banner prints at startup, not mid-menu.
        AccountRulesEngine.getInstance();

        // 📝 STEP 15: destination -> logger -> service -> UI
        LogDestination destination = new FileLogDestination();
        TransactionLogger logger = new TransactionLogger(destination);
        AccountService service = new AccountService(
                RepositoryFactory.getAccountRepository(),
                RepositoryFactory.getTransactionRepository(),
                logger);
        AccountUI ui = new AccountUI(service);
        ui.start();
    }
}
