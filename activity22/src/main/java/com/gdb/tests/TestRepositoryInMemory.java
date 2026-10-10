package com.gdb.tests;

import com.gdb.domain.Account;
import com.gdb.domain.IAccount;
import com.gdb.domain.Transaction;
import com.gdb.domain.TransactionType;
import com.gdb.factory.AccountFactory;
import com.gdb.logging.MemoryLogDestination;
import com.gdb.logging.TransactionLogger;
import com.gdb.repository.AccountRepository;
import com.gdb.repository.RepositoryFactory;
import com.gdb.repository.TransactionRepository;
import com.gdb.service.AccountService;

import java.time.LocalDateTime;
import java.util.List;

public class TestRepositoryInMemory {

    public static void main(String[] args) throws Exception {
        System.out.println("============================================================");
        System.out.println("  ACTIVITY 21 - REPOSITORY PATTERN IN-MEMORY TESTS");
        System.out.println("============================================================");

        RepositoryFactory.reset();
        AccountRepository accountRepo = RepositoryFactory.getAccountRepository();
        TransactionRepository txnRepo = RepositoryFactory.getTransactionRepository();

        testAccountRepository(accountRepo);
        testTransactionRepository(txnRepo);
        testServiceIntegration(accountRepo, txnRepo);

        System.out.println("============================================================");
        System.out.println("  ALL ACTIVITY 21 IN-MEMORY REPOSITORY TESTS PASSED!");
        System.out.println("============================================================");
    }

    private static void testAccountRepository(AccountRepository repo) {
        System.out.println("[TEST 1] Account Repository CRUD:");

        check(repo.nextAccountNumber() == 1001, "first account number should be 1001");
        IAccount rajesh = AccountFactory.createAccount("SAVINGS", 1001, "Rajesh Sharma", 30, 0, 50000.0, "1234");
        repo.save(rajesh);
        System.out.println("  Saved: " + rajesh.getSummary());

        check(repo.nextAccountNumber() == 1002, "second account number should be 1002");
        IAccount priya = AccountFactory.createAccount("SAVINGS", 1002, "Priya Patel", 28, 0, 20000.0, "5678");
        repo.save(priya);

        IAccount found = repo.findById(1001);
        check(found != null, "account 1001 should be found");
        System.out.println("  Found: " + found.getSummary());
        check(repo.findById(9999) == null, "unknown account should return null");
        check(repo.exists(1001) && !repo.exists(9999), "exists() should match what was saved");

        try {
            ((Account) found).deposit(15000.0);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
        repo.update(found);
        System.out.println("  Updated balance: Rs. " + repo.findById(1001).getBalance());
        check(repo.findById(1001).getBalance() == 65000.0, "balance should be 65000.0 after update");

        int total = repo.findAll().size();
        System.out.println("  Total accounts in repository: " + total);
        check(total == 2, "repository should hold 2 accounts");

        // findAll must hand out a copy, not the internal collection
        repo.findAll().clear();
        check(repo.findAll().size() == 2, "clearing the returned list must not affect storage");

        // delete is checked on a throwaway account so the counts above stay as shown
        IAccount temp = AccountFactory.createAccount("SAVINGS", 5000, "Temp User", 40, 0, 20000.0, "0000");
        repo.save(temp);
        repo.delete(5000);
        check(!repo.exists(5000), "deleted account should be gone");

        System.out.println("  -> PASSED");
        System.out.println();
    }

    private static void testTransactionRepository(TransactionRepository repo) {
        System.out.println("[TEST 2] Transaction Repository Operations:");

        Transaction deposit = new Transaction(Transaction.generateId(), LocalDateTime.now(), 1001,
                TransactionType.DEPOSIT, 15000.0, 65000.0, "SUCCESS", "Deposit of Rs. 15000.0", 0, 0);
        repo.save(deposit);
        repo.transfer(1001, 1002, 5000.0);
        System.out.println("  Saved " + repo.findAll().size() + " transaction records.");
        check(repo.findAll().size() == 2, "2 records should be saved");

        int for1001 = repo.findByAccount(1001).size();
        System.out.println("  Found transactions for #1001: " + for1001);
        check(for1001 == 2, "#1001 appears in both records");
        check(repo.findByAccount(1002).size() == 1, "#1002 appears only as the transfer destination");
        check(repo.findByAccount(7777).isEmpty(), "unknown account has no history");

        List<Transaction> copy = repo.findAll();
        copy.clear();
        check(repo.findAll().size() == 2, "findAll must return a copy");

        System.out.println("  -> PASSED");
        System.out.println();
    }

    private static void testServiceIntegration(AccountRepository accountRepo, TransactionRepository txnRepo)
            throws Exception {
        System.out.println("[TEST 3] Service Integration with Repositories:");

        txnRepo.clear();
        AccountService service = new AccountService(accountRepo, txnRepo,
                new TransactionLogger(new MemoryLogDestination()));

        IAccount opened = service.openAccount("SAVINGS", "Amit Verma", 35, 20000.0);
        opened.setPin("4321");
        System.out.println("  Opened account #" + opened.getAccountNumber() + " via AccountService.");
        check(opened.getAccountNumber() == 1003, "service should continue the repository's numbering");
        check(accountRepo.exists(1003), "the account must be stored in the repository");

        Transaction deposit = service.deposit(1003, 5000.0);
        System.out.println("  Deposit executed. New Balance: Rs. " + deposit.getBalanceAfter());
        check(deposit.getBalanceAfter() == 25000.0, "balance after deposit should be 25000.0");
        check(accountRepo.findById(1003).getBalance() == 25000.0, "repository should see the new balance");
        check(txnRepo.findByAccount(1003).size() == 1, "the ledger should hold the deposit");

        System.out.println("  -> PASSED");
        System.out.println();
    }

    /** Always throws on failure (unlike the assert keyword, it does not need -ea). */
    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
