package com.gdb.logging;

import com.gdb.command.TransactionCommand;
import com.gdb.database.SimulatedDatabase;

import java.util.ArrayList;
import java.util.List;

public class DatabaseLogDestination implements LogDestination {

    private static final String TABLE = "transaction_log";

    // 📝 STEP 11
    private final SimulatedDatabase db;

    // 📝 STEP 12
    public DatabaseLogDestination(SimulatedDatabase db) {
        this.db = db;
    }

    // 📝 STEP 13
    @Override
    public void write(TransactionCommand cmd) {
        db.insert(TABLE, cmd);
    }

    // 📝 STEP 14
    @Override
    public List<TransactionCommand> readAll() {
        List<TransactionCommand> commands = new ArrayList<>();
        for (Object row : db.selectAll(TABLE)) {
            commands.add((TransactionCommand) row);
        }
        return commands;
    }

    // 📝 STEP 15
    @Override
    public void clear() {
        db.deleteAll(TABLE);
    }

    // 📝 STEP 16
    @Override
    public String getDestinationName() {
        return "DATABASE";
    }
}
