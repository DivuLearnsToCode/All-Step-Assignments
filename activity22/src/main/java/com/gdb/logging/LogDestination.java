package com.gdb.logging;

import com.gdb.command.TransactionCommand;

import java.util.List;

/** Bridge "implementor": where logged commands are physically stored. */
public interface LogDestination {

    // 📝 STEP 1
    void write(TransactionCommand cmd);

    // 📝 STEP 2
    List<TransactionCommand> readAll();

    // 📝 STEP 3
    void clear();

    // 📝 STEP 4
    String getDestinationName();
}
