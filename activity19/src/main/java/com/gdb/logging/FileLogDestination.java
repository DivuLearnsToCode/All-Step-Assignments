package com.gdb.logging;

import com.gdb.command.TransactionCommand;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

public class FileLogDestination implements LogDestination {

    // 📝 STEP 5
    private TransactionLog log;

    // 📝 STEP 6
    public FileLogDestination() {
        this.log = new TransactionLog();
    }

    // 📝 STEP 7
    // LogDestination methods declare no checked exceptions, so file errors are
    // rethrown unchecked rather than leaking IOException into every backend.
    @Override
    public void write(TransactionCommand cmd) {
        try {
            log.log(cmd);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to write to file log", e);
        }
    }

    // 📝 STEP 8
    @Override
    public List<TransactionCommand> readAll() {
        try {
            return log.readAll();
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read file log", e);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Log file contains an unknown class", e);
        }
    }

    // 📝 STEP 9
    @Override
    public void clear() {
        log.clear();
    }

    // 📝 STEP 10
    @Override
    public String getDestinationName() {
        return "FILE";
    }
}
