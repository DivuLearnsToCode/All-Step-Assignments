package com.gdb.tests;

import com.gdb.command.DepositCommand;
import com.gdb.command.TransactionCommand;
import com.gdb.command.TransferCommand;
import com.gdb.command.WithdrawCommand;
import com.gdb.database.SimulatedDatabase;
import com.gdb.domain.Account;
import com.gdb.factory.AccountFactory;
import com.gdb.logging.DatabaseLogDestination;
import com.gdb.logging.FileLogDestination;
import com.gdb.logging.LogDestination;
import com.gdb.logging.MemoryLogDestination;
import com.gdb.logging.TransactionLogger;

import java.util.Arrays;
import java.util.List;

public class TestBridgeLogging {

    public static void main(String[] args) throws Exception {
        System.out.println("============================================================");
        System.out.println("  ACTIVITY 18 - BRIDGE PATTERN (FILE + DB)");
        System.out.println("============================================================");
        System.out.println();

        // 📝 STEP 24: three backends, each starting empty
        LogDestination file = new FileLogDestination();
        LogDestination database = new DatabaseLogDestination(new SimulatedDatabase());
        LogDestination memory = new MemoryLogDestination();
        file.clear();
        database.clear();
        memory.clear();

        Account acc1 = (Account) AccountFactory.createAccount(
                "SAVINGS", 1001, "Rajesh Sharma", 30, 0, 15000.0, "1234");
        Account acc2 = (Account) AccountFactory.createAccount(
                "SAVINGS", 1002, "Priya Patel", 28, 0, 20000.0, "0000");

        List<TransactionCommand> commands = Arrays.asList(
                new DepositCommand(acc1, 5000.0),
                new WithdrawCommand(acc1, 2000.0, 1234),
                new TransferCommand(acc1, acc2, 3000.0, "1234"));
        for (TransactionCommand cmd : commands) {
            cmd.execute();
        }
        System.out.println();

        TransactionLogger logger = new TransactionLogger(file);

        // 📝 STEP 25
        System.out.println("[STEP 25] Logging to " + logger.getDestinationName() + " destination...");
        for (TransactionCommand cmd : commands) {
            logger.log(cmd);
        }
        System.out.println("  " + logger.getDestinationName() + " log count: " + logger.readAll().size());
        System.out.println();

        // 📝 STEP 26: same logger object, different backend
        logger.setDestination(database);
        System.out.println("[STEP 26] Switched to " + logger.getDestinationName() + " destination...");
        for (TransactionCommand cmd : commands) {
            logger.log(cmd);
        }
        System.out.println("  " + logger.getDestinationName() + " log count: " + logger.readAll().size());
        System.out.println();

        // 📝 STEP 27
        logger.setDestination(memory);
        System.out.println("[STEP 27] Switched to " + logger.getDestinationName() + " destination...");
        for (TransactionCommand cmd : commands) {
            logger.log(cmd);
        }
        System.out.println("  " + logger.getDestinationName() + " log count: " + logger.readAll().size());
        System.out.println();

        // 📝 STEP 28: each backend holds only its own 3 entries
        System.out.println("[STEP 28] Verifying Data Isolation:");
        int fileCount = file.readAll().size();
        int dbCount = database.readAll().size();
        int memCount = memory.readAll().size();
        System.out.println("  FILE count: " + fileCount + " [EXPECTED: 3]");
        System.out.println("  DATABASE count: " + dbCount + " [EXPECTED: 3]");
        System.out.println("  MEMORY count: " + memCount + " [EXPECTED: 3]");
        System.out.println();

        // 📝 STEP 29
        if (fileCount == 3 && dbCount == 3 && memCount == 3) {
            System.out.println("[STEP 29] All Bridge Pattern log backends verified successfully!");
        } else {
            System.out.println("[STEP 29] Verification FAILED");
        }
    }
}
