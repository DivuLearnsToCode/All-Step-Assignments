package com.gdb.service;

import com.gdb.command.DepositCommand;
import com.gdb.command.TransactionCommand;
import com.gdb.command.TransferCommand;
import com.gdb.command.WithdrawCommand;
import com.gdb.domain.Account;
import com.gdb.domain.IAccount;
import com.gdb.domain.Transaction;
import com.gdb.exceptions.AccountException;
import com.gdb.exceptions.InactiveAccountException;
import com.gdb.exceptions.InvalidPinException;
import com.gdb.factory.AccountFactory;
import com.gdb.logging.TransactionLogger;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Single entry point for banking operations. Callers never create commands,
 * execute them, or log them -- they just call deposit/withdraw/transfer.
 */
public class AccountService {

    // 📝 STEP 1
    private final Map<Integer, IAccount> accounts;
    private final TransactionLogger logger;
    // Kept per the spec. Transfers currently run through TransferCommand, which
    // builds its own (stateless) TransferService so the command stays serializable.
    private final TransferService transferService;
    private int nextAccountNumber = 1001;

    // 📝 STEP 2
    public AccountService(TransactionLogger logger) {
        this.accounts = new LinkedHashMap<>();
        this.logger = logger;
        this.transferService = new TransferService();
    }

    // 📝 STEP 3
    public IAccount openAccount(String type, String name, int age, double initialBalance) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (age <= 0) {
            throw new IllegalArgumentException("Age must be positive");
        }
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }

        // Tenure is 0 for a brand-new customer; the PIN is set right after opening.
        // (Fixed deposits default to a 12-month term.)
        IAccount account = AccountFactory.createAccount(
                type, nextAccountNumber, name.trim(), age, 0, initialBalance, null, 12);

        // Only consume the number once creation succeeded.
        accounts.put(nextAccountNumber, account);
        nextAccountNumber++;
        return account;
    }

    // 📝 STEP 4
    public void closeAccount(int accountNumber, String pin) throws AccountException {
        IAccount account = getAccount(accountNumber);
        account.verifyPin(pin);
        if (!account.isActive()) {
            throw new InactiveAccountException("Account #" + accountNumber + " is already closed");
        }
        account.closeAccount();
    }

    // 📝 STEP 5
    public Transaction deposit(int accountNumber, double amount) throws AccountException {
        Account account = (Account) getAccount(accountNumber);
        return run(new DepositCommand(account, amount));
    }

    // 📝 STEP 6
    public Transaction withdraw(int accountNumber, double amount, String pin) throws AccountException {
        Account account = (Account) getAccount(accountNumber);
        // WithdrawCommand takes the PIN as an int, so anything that is not a
        // 4-digit number can never be correct.
        if (pin == null || !pin.matches("\\d{4}")) {
            throw new InvalidPinException("Incorrect PIN");
        }
        return run(new WithdrawCommand(account, amount, Integer.parseInt(pin)));
    }

    // 📝 STEP 7
    public Transaction transfer(int fromAccount, int toAccount, double amount, String pin)
            throws AccountException {
        IAccount from = getAccount(fromAccount);
        IAccount to = getAccount(toAccount);
        return run(new TransferCommand(from, to, amount, pin));
    }

    // 📝 STEP 8
    public IAccount getAccount(int accountNumber) throws AccountException {
        IAccount account = accounts.get(accountNumber);
        if (account == null) {
            throw new AccountException("Account not found: " + accountNumber);
        }
        return account;
    }

    // 📝 STEP 9
    public List<IAccount> getAllAccounts() {
        return new ArrayList<>(accounts.values());
    }

    // 📝 STEP 10
    public List<TransactionCommand> getTransactionHistory() {
        return logger.readAll();
    }

    // 📝 STEP 11
    public int getNextAccountNumber() {
        return nextAccountNumber;
    }

    /** Execute, then log, then hand back the Transaction. A failed command is never logged. */
    private Transaction run(TransactionCommand command) throws AccountException {
        try {
            command.execute();
        } catch (AccountException | RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new AccountException(e.getMessage());
        }
        logger.log(command);
        return command.getTransaction();
    }
}
