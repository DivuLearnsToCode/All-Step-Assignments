package com.gdb.repository;

import com.gdb.domain.Transaction;
import com.gdb.domain.TransactionType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryTransactionRepository implements TransactionRepository {

    // 📝 STEP 9
    private final List<Transaction> transactions = new ArrayList<>();

    // 📝 STEP 10
    @Override
    public synchronized void save(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction cannot be null");
        }
        transactions.add(transaction);
    }

    // 📝 STEP 11
    @Override
    public synchronized List<Transaction> findByAccount(int accountNumber) {
        return transactions.stream()
                .filter(t -> t.getAccountNumber() == accountNumber
                        || t.getFromAccount() == accountNumber
                        || t.getToAccount() == accountNumber)
                .collect(Collectors.toList());
    }

    // 📝 STEP 12
    @Override
    public synchronized List<Transaction> findAll() {
        return new ArrayList<>(transactions);
    }

    @Override
    public synchronized void clear() {
        transactions.clear();
    }

    // 📝 STEP 13
    // A repository does not know account balances, so balanceAfter is recorded as 0.0 here.
    // The service-level transfer (which does know the balance) is what normally creates these.
    @Override
    public synchronized Transaction transfer(int fromAccount, int toAccount, double amount) {
        Transaction txn = new Transaction(
                Transaction.generateId(),
                LocalDateTime.now(),
                fromAccount,
                TransactionType.TRANSFER,
                amount,
                0.0,
                "SUCCESS",
                "Transfer of Rs. " + amount + " to Account #" + toAccount,
                fromAccount,
                toAccount);
        save(txn);
        return txn;
    }
}
