package com.gdb.command;

import com.gdb.domain.IAccount;
import com.gdb.domain.Transaction;
import com.gdb.service.TransferService;

// 📝 STEP 5 & 6
public class TransferCommand implements TransactionCommand {

    private static final long serialVersionUID = 1L;

    private final IAccount from;
    private final IAccount to;
    private final double amount;
    private final String pin;
    private Transaction transaction;

    public TransferCommand(IAccount from, IAccount to, double amount, String pin) {
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.pin = pin;
    }

    @Override
    public void execute() throws Exception {
        // TransferService is stateless, so it is created per execution instead of
        // being stored as a (non-serializable) field on the command.
        transaction = new TransferService().transferWithTransaction(from, to, amount, pin);
    }

    @Override
    public Transaction getTransaction() {
        return transaction;
    }
}
