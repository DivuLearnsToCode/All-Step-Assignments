package com.gdb.command;

import com.gdb.domain.Account;
import com.gdb.domain.Transaction;

// 📝 STEP 4
public class WithdrawCommand implements TransactionCommand {

    private static final long serialVersionUID = 1L;

    private final Account account;
    private final double amount;
    private final int pin;
    private Transaction transaction;

    public WithdrawCommand(Account account, double amount, int pin) {
        this.account = account;
        this.amount = amount;
        this.pin = pin;
    }

    @Override
    public void execute() throws Exception {
        transaction = account.withdrawWithTransaction(amount, pin);
    }

    @Override
    public Transaction getTransaction() {
        return transaction;
    }
}
