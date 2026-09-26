package com.gdb.domain;

import java.time.LocalDateTime;

/**
 * A historical record of one money-movement operation (deposit, withdrawal,
 * or transfer). Only DEPOSIT / WITHDRAW / TRANSFER are modeled today; the
 * shape is generic enough that INTEREST_CREDITED, ACCOUNT_CLOSED, etc. can
 * be added later just by extending TransactionType and following the same
 * "do the operation, then build a Transaction" pattern used in Account and
 * TransferService.
 */
public class Transaction {

    // 📝 STEP 6: monotonically increasing counter so ids stay unique even if
    // two transactions land in the same millisecond.
    private static int counter = 0;

    // 📝 STEP 2
    private String transactionId;
    private LocalDateTime timestamp;
    private int accountNumber;
    private TransactionType type;
    private double amount;
    private double balanceAfter;
    private String status;
    private String description;
    private int fromAccount;
    private int toAccount;

    // 📝 STEP 3
    public Transaction(String transactionId, LocalDateTime timestamp, int accountNumber, TransactionType type,
                        double amount, double balanceAfter, String status, String description,
                        int fromAccount, int toAccount) {
        this.transactionId = transactionId;
        this.timestamp = timestamp;
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.status = status;
        this.description = description;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
    }

    // 📝 STEP 6
    public static synchronized String generateId() {
        return "TXN-" + System.currentTimeMillis() + "-" + (++counter);
    }

    // 📝 STEP 4: getters & setters

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(double balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(int fromAccount) {
        this.fromAccount = fromAccount;
    }

    public int getToAccount() {
        return toAccount;
    }

    public void setToAccount(int toAccount) {
        this.toAccount = toAccount;
    }

    // 📝 STEP 5
    @Override
    public String toString() {
        return "[" + transactionId + "] " + type + " | Rs. " + amount +
                " | Balance After: Rs. " + balanceAfter + " | Status: " + status + " | " + description;
    }
}
