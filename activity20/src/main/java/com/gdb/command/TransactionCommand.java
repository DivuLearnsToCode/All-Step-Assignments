package com.gdb.command;

import com.gdb.domain.Transaction;

import java.io.Serializable;

/**
 * Command Pattern: one money-movement operation packaged as an object.
 * Extends Serializable so executed commands can be written to a log file.
 */
public interface TransactionCommand extends Serializable {

    // 📝 STEP 1
    void execute() throws Exception;

    // 📝 STEP 2
    Transaction getTransaction();
}
