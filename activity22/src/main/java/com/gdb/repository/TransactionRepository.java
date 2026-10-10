package com.gdb.repository;

import com.gdb.domain.Transaction;

import java.util.List;

/** Storage contract for the transaction ledger. */
public interface TransactionRepository {

    void save(Transaction transaction);

    /** Every transaction where the account is the owner, the source or the destination. */
    List<Transaction> findByAccount(int accountNumber);

    List<Transaction> findAll();

    void clear();

    /** Builds and records a TRANSFER entry linking the two accounts. */
    Transaction transfer(int fromAccount, int toAccount, double amount);
}
