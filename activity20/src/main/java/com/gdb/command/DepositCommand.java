package com.gdb.command;

import com.gdb.domain.Account;
import com.gdb.domain.Transaction;

// 📝 STEP 3
public class DepositCommand implements TransactionCommand {

    private static final long serialVersionUID = 1L;

    private final Account account;
    private final double amount;
    private Transaction transaction;

    public DepositCommand(Account account, double amount) {
        this.account = account;
        this.amount = amount;
    }

    @Override
    public void execute() throws Exception {
        transaction = account.depositWithTransaction(amount);
    }

    @Override
    public Transaction getTransaction() {
        return transaction;
    }
}
