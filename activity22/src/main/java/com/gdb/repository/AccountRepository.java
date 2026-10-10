package com.gdb.repository;

import com.gdb.domain.IAccount;

import java.util.List;

/** Storage contract for accounts. Callers never know what sits behind it. */
public interface AccountRepository {

    void save(IAccount account);

    /** @return the account, or null if none has this number */
    IAccount findById(int accountNumber);

    List<IAccount> findAll();

    void update(IAccount account);

    void delete(int accountNumber);

    boolean exists(int accountNumber);

    /** Hands out the next account number and advances the sequence. */
    int nextAccountNumber();

    /** Shows the number nextAccountNumber() would return, without consuming it. */
    int peekNextAccountNumber();
}
