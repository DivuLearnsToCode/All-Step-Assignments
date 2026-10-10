package com.gdb.domain;

import com.gdb.exceptions.AccountException;
import com.gdb.exceptions.InvalidAmountException;
import com.gdb.exceptions.InvalidPinException;

import java.io.Serializable;

/**
 * The contract every bank account type must satisfy. TransferService and
 * other callers depend only on this interface, never on a concrete class.
 */
public interface IAccount extends Serializable {

    int getAccountNumber();
    String getName();
    int getAge();
    double getBalance();
    String getAccountType();
    String getStatus();
    boolean isActive();

    void deposit(double amount) throws InvalidAmountException;
    void withdraw(double amount, String pin) throws AccountException;
    void verifyPin(String pin) throws InvalidPinException;

    /** True if a withdrawal/transfer of this amount would currently succeed, without side effects. */
    boolean canWithdraw(double amount);

    void displayAccountInfo();

    /** The one-line account description as a String (what displayAccountInfo prints). */
    String getSummary();

    /** Sets (or replaces) the account PIN. Must be exactly 4 digits. */
    void setPin(String pin);

    void closeAccount();
}
