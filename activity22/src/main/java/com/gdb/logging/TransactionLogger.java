package com.gdb.logging;

import com.gdb.command.TransactionCommand;

import java.util.List;

/**
 * Bridge "abstraction": callers log through this class and never know which
 * backend is behind it. The backend can be swapped at runtime.
 */
public class TransactionLogger {

    // 📝 STEP 17
    protected LogDestination destination;

    // 📝 STEP 18
    public TransactionLogger(LogDestination destination) {
        this.destination = destination;
    }

    // 📝 STEP 19
    public void setDestination(LogDestination destination) {
        this.destination = destination;
    }

    // 📝 STEP 20
    public void log(TransactionCommand cmd) {
        destination.write(cmd);
    }

    // 📝 STEP 21
    public List<TransactionCommand> readAll() {
        return destination.readAll();
    }

    // 📝 STEP 22
    public void clear() {
        destination.clear();
    }

    // 📝 STEP 23
    public String getDestinationName() {
        return destination.getDestinationName();
    }
}
