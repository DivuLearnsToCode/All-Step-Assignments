package com.gdb.factory;

import com.gdb.domain.CurrentAccount;
import com.gdb.domain.FixedDepositAccount;
import com.gdb.domain.IAccount;
import com.gdb.domain.SalaryAccount;
import com.gdb.domain.SavingsAccount;

/**
 * Centralizes account creation so callers never instantiate a concrete
 * account class directly -- they ask for a type by name and get back an
 * IAccount, with customer tenure now threaded through to every type.
 */
public class AccountFactory {

    // Convenience overload: no FD term months.
    public static IAccount createAccount(String accountType, int accountNumber, String name, int age,
                                          int tenureYears, double initialBalance, String pin) {
        return createAccount(accountType, accountNumber, name, age, tenureYears, initialBalance, pin, 0);
    }

    public static IAccount createAccount(String accountType, int accountNumber, String name, int age,
                                          int tenureYears, double initialBalance, String pin, int termMonths) {
        if (accountType == null) {
            throw new IllegalArgumentException("Account type cannot be null");
        }

        switch (accountType.toUpperCase()) {
            case "SAVINGS":
                return new SavingsAccount(accountNumber, name, age, tenureYears, initialBalance, pin);

            case "CURRENT":
                return new CurrentAccount(accountNumber, name, age, tenureYears, initialBalance, pin);

            case "FIXED_DEPOSIT":
            case "FD":
                return new FixedDepositAccount(accountNumber, name, age, tenureYears, initialBalance, pin, termMonths);

            case "SALARY":
                return new SalaryAccount(accountNumber, name, age, tenureYears, initialBalance, pin);

            default:
                throw new IllegalArgumentException("Unknown account type: " + accountType);
        }
    }
}
