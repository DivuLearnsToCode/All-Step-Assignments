/**
 * Same public API as Activity 13's AccountRulesEngine, but the Savings rules
 * (min balance, interest rate) are no longer hardcoded Java constants -- they
 * are read from an external savings.properties file via
 * AccountRulesPropertiesLoader. Business users can now change a rate without
 * touching or recompiling any Java code.
 */
public class AccountRulesEngine {

    private static final String SAVINGS_CLASSPATH_RESOURCE = "config/rules/savings.properties";
    private static final String SAVINGS_FILESYSTEM_PATH = "src/main/resources/config/rules/savings.properties";

    private static final AccountRulesPropertiesLoader SAVINGS_RULES =
            new AccountRulesPropertiesLoader(SAVINGS_CLASSPATH_RESOURCE, SAVINGS_FILESYSTEM_PATH);

    // Current/FD rules are out of scope for this activity's properties file,
    // so they stay as constants for now (a natural next externalization step).
    private static final double OVERDRAFT_MULTIPLIER = 2.5;
    private static final double OVERDRAFT_FLOOR = 25000.0;

    private AccountRulesEngine() {
        // static utility class - no instances
    }

    /** Maps raw tenure in years to the tenure tier used as the properties key prefix. */
    private static String tenureBucket(int tenureYears) {
        if (tenureYears >= 5) {
            return "privilege";
        }
        if (tenureYears >= 3) {
            return "premium";
        }
        if (tenureYears >= 1) {
            return "standard";
        }
        return "new";
    }

    /** Step 3: minimum balance required for a Savings account, read from savings.properties. */
    public static double getSavingsMinBalance(int tenureYears) {
        String bucket = tenureBucket(tenureYears);
        return SAVINGS_RULES.getDouble("savings." + bucket + ".minBalance", 10000.0);
    }

    /** Step 3: annual interest rate (%) for a Savings account, read from savings.properties. */
    public static double getSavingsInterestRate(int tenureYears) {
        String bucket = tenureBucket(tenureYears);
        return SAVINGS_RULES.getDouble("savings." + bucket + ".interestRate", 2.70);
    }

    /** Overdraft limit for a Current account = 2.5x monthly turnover, floor Rs 25000. */
    public static double getCurrentOverdraftLimit(double monthlyTurnover) {
        double computedLimit = monthlyTurnover * OVERDRAFT_MULTIPLIER;
        return Math.max(computedLimit, OVERDRAFT_FLOOR);
    }

    /** FD interest rate (%) by deposit duration in months. */
    public static double getFDInterestRate(int months) {
        if (months >= 12) {
            return 6.5;
        }
        if (months >= 6) {
            return 5.75;
        }
        return 5.0;
    }
}
