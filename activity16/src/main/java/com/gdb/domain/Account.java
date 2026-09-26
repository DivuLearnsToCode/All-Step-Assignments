package com.gdb.domain;

import com.gdb.exceptions.AccountException;
import com.gdb.exceptions.InactiveAccountException;
import com.gdb.exceptions.InvalidAmountException;
import com.gdb.exceptions.InvalidPinException;

import java.time.LocalDate;
import java.time.LocalDateTime;

public abstract class Account implements IAccount {

    private static final String PIN_PATTERN = "\\d{4}";

    protected int accountNumber;
    protected String name;
    protected int age;
    protected int tenureYears;
    protected double balance;
    protected String accountType;
    protected String status;
    protected String pin;

    // 📝 STEP 2.1 (provided): today's running transfer total and the day it belongs to.
    protected double dailyTransferTotal = 0.0;
    protected LocalDateTime lastTransferDate = LocalDateTime.now();

    protected Account(int accountNumber, String name, int age, int tenureYears, double initialBalance,
                       String accountType, String pin) {
        if (pin == null || !pin.matches(PIN_PATTERN)) {
            throw new IllegalArgumentException("PIN must be a 4-digit number");
        }
        this.accountNumber = accountNumber;
        this.name = name;
        this.age = age;
        this.tenureYears = tenureYears;
        this.balance = initialBalance;
        this.accountType = accountType;
        this.status = "Active";
        this.pin = pin;
    }

    @Override
    public void deposit(double amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be positive. Provided: Rs. " + amount);
        }
        balance += amount;
    }

    @Override
    public void verifyPin(String pin) throws InvalidPinException {
        if (this.pin == null || !this.pin.equals(pin)) {
            throw new InvalidPinException("Incorrect PIN");
        }
    }

    @Override
    public final void withdraw(double amount, String pin) throws AccountException {
        verifyPin(pin);
        if (!isActive()) {
            throw new InactiveAccountException("Account is inactive. Please reopen the account or contact support.");
        }
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be positive. Provided: Rs. " + amount);
        }
        processDebit(amount);
    }

    /** Subclass-specific debit rules (min balance, overdraft, "never allowed", etc.). */
    protected abstract void processDebit(double amount) throws AccountException;

    @Override
    public abstract boolean canWithdraw(double amount);

    @Override
    public boolean isActive() {
        return "Active".equals(status);
    }

    public void closeAccount() {
        status = "Inactive";
    }

    public void reopenAccount() {
        status = "Active";
    }

    @Override
    public void displayAccountInfo() {
        System.out.println("Account #" + accountNumber + " | " + name + " (" + age + " yrs, Tenure: " +
                tenureYears + " yrs) | " + accountType + " | Rs. " + balance + " | " + status);
    }

    // 📝 STEP 3
    public double getDailyTransferLimit() {
        return AccountRulesEngine.getInstance().getDailyTransferLimit(getAccountType(), getTenureYears());
    }

    // 📝 STEP 4
    public double getRemainingDailyTransferLimit() {
        resetDailyTransferIfNeeded();
        return Math.max(0.0, getDailyTransferLimit() - dailyTransferTotal);
    }

    // 📝 STEP 5
    public boolean canTransfer(double amount) {
        resetDailyTransferIfNeeded();
        return dailyTransferTotal + amount <= getDailyTransferLimit();
    }

    // 📝 STEP 6
    public void updateDailyTransferTotal(double amount) {
        resetDailyTransferIfNeeded();
        dailyTransferTotal += amount;
        lastTransferDate = LocalDateTime.now();
    }

    // 📝 STEP 7
    public void resetDailyTransferIfNeeded() {
        if (lastTransferDate == null || !lastTransferDate.toLocalDate().equals(LocalDate.now())) {
            dailyTransferTotal = 0.0;
            lastTransferDate = LocalDateTime.now();
        }
    }

    protected void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public int getAccountNumber() {
        return accountNumber;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getAge() {
        return age;
    }

    public int getTenureYears() {
        return tenureYears;
    }

    @Override
    public double getBalance() {
        return balance;
    }

    @Override
    public String getAccountType() {
        return accountType;
    }

    @Override
    public String getStatus() {
        return status;
    }

    public double getDailyTransferTotal() {
        return dailyTransferTotal;
    }

    public LocalDateTime getLastTransferDate() {
        return lastTransferDate;
    }

    // 📝 STEP 7: legacy deposit(amount) is untouched -- this is a pure addition.
    public Transaction depositWithTransaction(double amount) throws InvalidAmountException {
        deposit(amount);
        return new Transaction(
                Transaction.generateId(),
                LocalDateTime.now(),
                accountNumber,
                TransactionType.DEPOSIT,
                amount,
                balance,
                "SUCCESS",
                "Deposit of Rs. " + amount,
                0,
                0);
    }

    // 📝 STEP 8: legacy withdraw(amount, String pin) is untouched -- this is a pure addition.
    public Transaction withdrawWithTransaction(double amount, int pin) throws AccountException {
        String pinAsString = String.format("%04d", pin);
        withdraw(amount, pinAsString);
        return new Transaction(
                Transaction.generateId(),
                LocalDateTime.now(),
                accountNumber,
                TransactionType.WITHDRAW,
                amount,
                balance,
                "SUCCESS",
                "Withdrawal of Rs. " + amount,
                0,
                0);
    }
}
