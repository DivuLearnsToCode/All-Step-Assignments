package com.gdb.logging;

import com.gdb.command.TransactionCommand;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Appends executed commands to data/transactions.ser using native Java
 * serialization, and reads them all back. New command types need no changes
 * here -- anything implementing TransactionCommand can be logged.
 */
public class TransactionLog {

    private static final String DATA_DIR = "data";
    private static final String LOG_FILE = DATA_DIR + "/transactions.ser";

    // 📝 STEP 7
    public void log(TransactionCommand cmd) throws IOException {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(LOG_FILE);
        boolean fileExists = file.exists();

        // A normal ObjectOutputStream writes a stream header on open. Appending
        // a second header mid-file corrupts reading, so existing files use a
        // subclass that skips it.
        try (ObjectOutputStream out = fileExists
                ? new AppendableObjectOutputStream(new FileOutputStream(file, true))
                : new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(cmd);
            out.flush();
        }
    }

    // 📝 STEP 8
    public List<TransactionCommand> readAll() throws IOException, ClassNotFoundException {
        List<TransactionCommand> commands = new ArrayList<>();
        File file = new File(LOG_FILE);
        if (!file.exists()) {
            return commands;
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            while (true) {
                commands.add((TransactionCommand) in.readObject());
            }
        } catch (EOFException endOfLog) {
            // expected: reached the end of the log
        }
        return commands;
    }

    // 📝 STEP 9
    public void clear() {
        File file = new File(LOG_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    /** Skips the stream header so objects can be appended to an existing file. */
    private static class AppendableObjectOutputStream extends ObjectOutputStream {

        AppendableObjectOutputStream(OutputStream out) throws IOException {
            super(out);
        }

        @Override
        protected void writeStreamHeader() throws IOException {
            reset();
        }
    }
}
