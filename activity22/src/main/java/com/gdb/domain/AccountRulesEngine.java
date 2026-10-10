package com.gdb.domain;

import com.gdb.config.AccountRulesPropertiesLoader;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Singleton. Loads every account type's rules file once, on first use, and
 * answers all rule lookups (minBalance, interestRate, dailyTransferLimit, ...)
 * from the in-memory result -- callers never touch a properties file directly.
 */
public final class AccountRulesEngine {

    private static final String[] ACCOUNT_TYPES = {"savings", "current", "fixeddeposit", "salary"};
    private static final String CLASSPATH_DIR = "config/rules/";
    private static final String FILESYSTEM_DIR = "src/main/resources/config/rules/";

    private static final AccountRulesEngine INSTANCE = new AccountRulesEngine();

    public static AccountRulesEngine getInstance() {
        return INSTANCE;
    }

    // accountType -> tenure bucket -> feature name -> value
    private final Map<String, Map<String, Map<String, Double>>> rules = new LinkedHashMap<>();
    private final LocalDateTime loadedAt;

    private AccountRulesEngine() {
        System.out.println("\uD83D\uDCC2 Loading account rules from properties files...");
        System.out.println("--------------------------------------------------");

        for (String type : ACCOUNT_TYPES) {
            AccountRulesPropertiesLoader loader = new AccountRulesPropertiesLoader(
                    CLASSPATH_DIR + type + ".properties",
                    FILESYSTEM_DIR + type + ".properties");
            Map<String, Map<String, Double>> byBucket = loader.getBucketFeatureMap();
            rules.put(type, byBucket);
            System.out.println("\u2705 Loaded rules for: " + type.toUpperCase() +
                    " (" + byBucket.size() + " tenure buckets)");
        }

        System.out.println("--------------------------------------------------");
        loadedAt = LocalDateTime.now();
        System.out.println("\u2705 All rules loaded successfully!");
    }

    private static String normalize(String accountType) {
        return accountType == null ? "" : accountType.trim().toLowerCase().replace(" ", "").replace("_", "");
    }

    private static String tenureBucket(int tenureYears) {
        if (tenureYears >= 5) return "privilege";
        if (tenureYears >= 3) return "premium";
        if (tenureYears >= 1) return "standard";
        return "new";
    }

    // 📝 STEP 8
    public Double getAdditionalFeature(String accountType, int tenureYears, String featureKey) {
        Map<String, Map<String, Double>> byBucket = rules.get(normalize(accountType));
        if (byBucket == null) {
            return null;
        }
        Map<String, Double> features = byBucket.get(tenureBucket(tenureYears));
        if (features == null) {
            return null;
        }
        return features.get(featureKey);
    }

    public double getDailyTransferLimit(String accountType, int tenureYears) {
        Double value = getAdditionalFeature(accountType, tenureYears, "dailyTransferLimit");
        return value == null ? 0.0 : value;
    }

    public double getMinBalance(String accountType, int tenureYears) {
        Double value = getAdditionalFeature(accountType, tenureYears, "minBalance");
        return value == null ? 0.0 : value;
    }

    public double getInterestRate(String accountType, int tenureYears) {
        Double value = getAdditionalFeature(accountType, tenureYears, "interestRate");
        return value == null ? 0.0 : value;
    }

    public LocalDateTime getLoadedAt() {
        return loadedAt;
    }
}
