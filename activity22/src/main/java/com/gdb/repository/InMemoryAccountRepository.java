package com.gdb.repository;

import com.gdb.domain.IAccount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryAccountRepository implements AccountRepository {

    // 📝 STEP 1
    private final Map<Integer, IAccount> accounts = new HashMap<>();
    private int nextAccountNumber = 1001;

    // Methods are synchronized so concurrent callers cannot corrupt the HashMap
    // or hand out the same account number twice.

    // 📝 STEP 2
    @Override
    public synchronized void save(IAccount account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null");
        }
        accounts.put(account.getAccountNumber(), account);
    }

    // 📝 STEP 3
    @Override
    public synchronized IAccount findById(int accountNumber) {
        return accounts.get(accountNumber);
    }

    // 📝 STEP 4
    @Override
    public synchronized List<IAccount> findAll() {
        return new ArrayList<>(accounts.values());
    }

    // 📝 STEP 5
    @Override
    public synchronized void update(IAccount account) {
        if (account != null && accounts.containsKey(account.getAccountNumber())) {
            accounts.put(account.getAccountNumber(), account);
        }
    }

    // 📝 STEP 6
    @Override
    public synchronized void delete(int accountNumber) {
        accounts.remove(accountNumber);
    }

    // 📝 STEP 7
    @Override
    public synchronized boolean exists(int accountNumber) {
        return accounts.containsKey(accountNumber);
    }

    // 📝 STEP 8
    @Override
    public synchronized int nextAccountNumber() {
        return nextAccountNumber++;
    }

    @Override
    public synchronized int peekNextAccountNumber() {
        return nextAccountNumber;
    }
}
