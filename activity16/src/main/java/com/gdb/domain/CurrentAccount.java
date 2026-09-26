package com.gdb.domain;

import com.gdb.exceptions.AccountException;
import com.gdb.exceptions.InsufficientBalanceException;

public class CurrentAccount extends Account {

    private static final double OVERDRAFT_LIMIT = 5000.0;
    private double overdraftUsed;

    public CurrentAccount(int accountNumber, String name, int age, int tenureYears,
                           double initialBalance, String pin) {
        super(accountNumber, name, age, tenureYears, initialBalance, "Current", pin);
        this.overdraftUsed = 0.0;
    }

    @Override
    protected void processDebit(double amount) throws AccountException {
        double newBalance = balance - amount;
        double overdraftNeeded = (newBalance < 0) ? -newBalance : 0.0;

        if (overdraftNeeded > OVERDRAFT_LIMIT) {
            throw new InsufficientBalanceException(
                    "Overdraft limit of Rs. " + OVERDRAFT_LIMIT + " exceeded. Requested overdraft: Rs. " + overdraftNeeded);
        }

        overdraftUsed = overdraftNeeded;
        setBalance(newBalance);
    }

    @Override
    public boolean canWithdraw(double amount) {
        if (amount <= 0) {
            return false;
        }
        double newBalance = balance - amount;
        double overdraftNeeded = (newBalance < 0) ? -newBalance : 0.0;
        return overdraftNeeded <= OVERDRAFT_LIMIT;
    }

    public double getOverdraftLimit() {
        return OVERDRAFT_LIMIT;
    }

    public double getOverdraftUsed() {
        return overdraftUsed;
    }
}
