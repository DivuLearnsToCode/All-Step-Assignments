package com.gdb.service;

import com.gdb.domain.Account;
import com.gdb.domain.IAccount;
import com.gdb.domain.Transaction;
import com.gdb.domain.TransactionType;
import com.gdb.exceptions.AccountException;
import com.gdb.exceptions.InactiveAccountException;
import com.gdb.exceptions.InsufficientBalanceException;

import java.time.LocalDateTime;

public class TransferService {

    // 📝 STEP 1
    public void transfer(IAccount from, IAccount to, double amount, String pin) throws AccountException {

        // 1. Null check
        if (from == null || to == null) {
            throw new AccountException("Source and destination accounts are required");
        }

        // 2. Both accounts must be active
        if (!from.isActive() || !to.isActive()) {
            throw new InactiveAccountException("Both accounts must be active to transfer funds");
        }

        // 3. PIN check
        from.verifyPin(pin);

        // 4. Sufficient balance check (no side effects)
        if (!from.canWithdraw(amount)) {
            throw new InsufficientBalanceException("Insufficient balance for transfer of Rs. " + amount);
        }

        // 5. Daily transfer limit check
        Account source = (Account) from;
        source.resetDailyTransferIfNeeded();
        if (!source.canTransfer(amount)) {
            double remaining = source.getRemainingDailyTransferLimit();
            throw new AccountException("Daily transfer limit exceeded. Remaining today: Rs. " + remaining);
        }

        // 6. Debit the source
        from.withdraw(amount, pin);

        // 7. Credit the destination
        to.deposit(amount);

        // 8. Record the transfer against today's running total
        source.updateDailyTransferTotal(amount);
    }

    // 📝 STEP 9: legacy transfer(from, to, amount, pin) is untouched -- this is a pure addition.
    public Transaction transferWithTransaction(IAccount from, IAccount to, double amount, String pin)
            throws AccountException {
        transfer(from, to, amount, pin);
        return new Transaction(
                Transaction.generateId(),
                LocalDateTime.now(),
                from.getAccountNumber(),
                TransactionType.TRANSFER,
                amount,
                from.getBalance(),
                "SUCCESS",
                "Transfer of Rs. " + amount + " to Account #" + to.getAccountNumber(),
                from.getAccountNumber(),
                to.getAccountNumber());
    }
}
