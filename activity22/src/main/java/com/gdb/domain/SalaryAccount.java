package com.gdb.domain;

import com.gdb.exceptions.AccountException;
import com.gdb.exceptions.InsufficientBalanceException;

public class SalaryAccount extends Account {

    public SalaryAccount(int accountNumber, String name, int age, int tenureYears,
                          double initialBalance, String pin) {
        super(accountNumber, name, age, tenureYears, initialBalance, "Salary", pin);
    }

    @Override
    protected void processDebit(double amount) throws AccountException {
        // Salary accounts carry no overdraft and no minimum balance requirement.
        if (amount > balance) {
            throw new InsufficientBalanceException(
                    "Insufficient balance. Available: Rs. " + balance + ", Requested: Rs. " + amount);
        }
        setBalance(balance - amount);
    }

    @Override
    public boolean canWithdraw(double amount) {
        return amount > 0 && amount <= balance;
    }
}
