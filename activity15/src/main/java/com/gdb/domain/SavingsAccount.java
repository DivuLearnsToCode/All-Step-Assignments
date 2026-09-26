package com.gdb.domain;

import com.gdb.exceptions.AccountException;
import com.gdb.exceptions.InsufficientBalanceException;
import com.gdb.exceptions.MinimumBalanceViolationException;

public class SavingsAccount extends Account {

    public SavingsAccount(int accountNumber, String name, int age, int tenureYears,
                           double initialBalance, String pin) {
        super(accountNumber, name, age, tenureYears, initialBalance, "Savings", pin);
        double minBalance = minBalance();
        if (initialBalance < minBalance) {
            throw new IllegalArgumentException(
                    "Savings account (tenure " + tenureYears + " yrs) requires minimum balance of Rs. " +
                            minBalance + ". Provided: Rs. " + initialBalance);
        }
    }

    private double minBalance() {
        return AccountRulesEngine.getInstance().getMinBalance("savings", getTenureYears());
    }

    @Override
    protected void processDebit(double amount) throws AccountException {
        if (amount > balance) {
            throw new InsufficientBalanceException(
                    "Insufficient balance. Available: Rs. " + balance + ", Requested: Rs. " + amount);
        }
        double newBalance = balance - amount;
        if (newBalance < minBalance()) {
            throw new MinimumBalanceViolationException(
                    "Cannot withdraw. Minimum balance of Rs. " + minBalance() +
                            " required. Available after withdrawal: Rs. " + newBalance);
        }
        setBalance(newBalance);
    }

    @Override
    public boolean canWithdraw(double amount) {
        return amount > 0 && amount <= balance && (balance - amount) >= minBalance();
    }

    public double getAnnualInterestRate() {
        return AccountRulesEngine.getInstance().getInterestRate("savings", getTenureYears());
    }

    public double getMinimumBalance() {
        return minBalance();
    }
}
