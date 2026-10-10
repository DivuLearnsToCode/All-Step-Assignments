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
import com.gdb.repository.AccountRepository;
import com.gdb.repository.InMemoryAccountRepository;
import com.gdb.repository.InMemoryTransactionRepository;
import com.gdb.repository.TransactionRepository;

import java.util.List;

/**
 * Single entry point for banking operations. Storage is delegated to the
 * repositories it is given, so the persistence technology can change without
 * touching anything in this class.
 */
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionLogger logger;
    // Kept from Activity 19. Transfers run through TransferCommand, which builds its
    // own (stateless) TransferService so the command stays serializable.
    private final TransferService transferService;

    /** Dependency injection: the service is handed its storage. */
    public AccountService(AccountRepository accountRepository,
                          TransactionRepository transactionRepository,
                          TransactionLogger logger) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.logger = logger;
        this.transferService = new TransferService();
    }

    /** Convenience for tests and demos: private in-memory storage. */
    public AccountService(TransactionLogger logger) {
        this(new InMemoryAccountRepository(), new InMemoryTransactionRepository(), logger);
    }

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

        // The repository owns the numbering. Like a database sequence, a number is
        // not handed back if account creation then fails.
        int accountNumber = accountRepository.nextAccountNumber();
        IAccount account = AccountFactory.createAccount(
                type, accountNumber, name.trim(), age, 0, initialBalance, null, 12);
        accountRepository.save(account);
        return account;
    }

    public void closeAccount(int accountNumber, String pin) throws AccountException {
        IAccount account = getAccount(accountNumber);
        account.verifyPin(pin);
        if (!account.isActive()) {
            throw new InactiveAccountException("Account #" + accountNumber + " is already closed");
        }
        account.closeAccount();
        accountRepository.update(account);
    }

    public Transaction deposit(int accountNumber, double amount) throws AccountException {
        Account account = (Account) getAccount(accountNumber);
        Transaction txn = run(new DepositCommand(account, amount));
        accountRepository.update(account);
        return txn;
    }

    public Transaction withdraw(int accountNumber, double amount, String pin) throws AccountException {
        Account account = (Account) getAccount(accountNumber);
        if (pin == null || !pin.matches("\\d{4}")) {
            throw new InvalidPinException("Incorrect PIN");
        }
        Transaction txn = run(new WithdrawCommand(account, amount, Integer.parseInt(pin)));
        accountRepository.update(account);
        return txn;
    }

    public Transaction transfer(int fromAccount, int toAccount, double amount, String pin)
            throws AccountException {
        IAccount from = getAccount(fromAccount);
        IAccount to = getAccount(toAccount);
        Transaction txn = run(new TransferCommand(from, to, amount, pin));
        accountRepository.update(from);
        accountRepository.update(to);
        return txn;
    }

    public IAccount getAccount(int accountNumber) throws AccountException {
        IAccount account = accountRepository.findById(accountNumber);
        if (account == null) {
            throw new AccountException("Account not found: " + accountNumber);
        }
        return account;
    }

    public List<IAccount> getAllAccounts() {
        return accountRepository.findAll();
    }

    public List<TransactionCommand> getTransactionHistory() {
        return logger.readAll();
    }

    /** Ledger entries (from the transaction repository) that involve this account. */
    public List<Transaction> getLedger(int accountNumber) {
        return transactionRepository.findByAccount(accountNumber);
    }

    public int getNextAccountNumber() {
        return accountRepository.peekNextAccountNumber();
    }

    /** Execute, then log, then record in the ledger, then hand back the Transaction. */
    private Transaction run(TransactionCommand command) throws AccountException {
        try {
            command.execute();
        } catch (AccountException | RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new AccountException(e.getMessage());
        }
        logger.log(command);
        transactionRepository.save(command.getTransaction());
        return command.getTransaction();
    }
}
