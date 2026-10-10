package com.gdb.repository;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Decides which repository implementation the application uses, from
 * config/persistence.properties. Instances are created once and shared.
 */
public class RepositoryFactory {

    private static final String CLASSPATH_RESOURCE = "config/persistence.properties";
    private static final String FILESYSTEM_PATH = "src/main/resources/config/persistence.properties";
    private static final String DEFAULT_MODE = "memory";

    private static AccountRepository accountRepository;
    private static TransactionRepository transactionRepository;

    private RepositoryFactory() {
    }

    // 📝 STEP 14
    public static synchronized AccountRepository getAccountRepository() {
        if (accountRepository == null) {
            requireMemoryMode();
            accountRepository = new InMemoryAccountRepository();
        }
        return accountRepository;
    }

    public static synchronized TransactionRepository getTransactionRepository() {
        if (transactionRepository == null) {
            requireMemoryMode();
            transactionRepository = new InMemoryTransactionRepository();
        }
        return transactionRepository;
    }

    /** Forgets the cached repositories (used by tests that want a clean slate). */
    public static synchronized void reset() {
        accountRepository = null;
        transactionRepository = null;
    }

    public static String getPersistenceMode() {
        return loadProperties().getProperty("persistence.mode", DEFAULT_MODE).trim().toLowerCase();
    }

    private static void requireMemoryMode() {
        String mode = getPersistenceMode();
        if (!"memory".equals(mode)) {
            throw new IllegalStateException("Unsupported persistence.mode: " + mode);
        }
    }

    private static Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream in = RepositoryFactory.class.getClassLoader().getResourceAsStream(CLASSPATH_RESOURCE)) {
            if (in != null) {
                props.load(in);
                return props;
            }
        } catch (IOException e) {
            // fall through to the file system
        }
        try (InputStream in = new FileInputStream(FILESYSTEM_PATH)) {
            props.load(in);
        } catch (IOException e) {
            // no file anywhere: defaults apply
        }
        return props;
    }
}
