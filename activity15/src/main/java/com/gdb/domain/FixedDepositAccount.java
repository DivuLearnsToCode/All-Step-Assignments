package com.gdb.domain;

import com.gdb.exceptions.AccountException;

public class FixedDepositAccount extends Account {

    private final int termMonths;

    public FixedDepositAccount(int accountNumber, String name, int age, int tenureYears,
                                double initialBalance, String pin, int termMonths) {
        super(accountNumber, name, age, tenureYears, initialBalance, "FixedDeposit", pin);
        this.termMonths = termMonths;
    }

    @Override
    protected void processDebit(double amount) throws AccountException {
        // Fixed deposits block any withdrawal before maturity.
        throw new AccountException("Premature withdrawal not allowed on Fixed Deposit accounts before maturity.");
    }

    @Override
    public boolean canWithdraw(double amount) {
        // No withdrawal is ever allowed pre-maturity, so no transfer is either
        // -- consistent with its daily transfer limit being 0 at every tenure.
        return false;
    }

    public int getTermMonths() {
        return termMonths;
    }
}
