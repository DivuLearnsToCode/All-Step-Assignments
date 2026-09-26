import java.util.HashMap;
import java.util.Map;

/**
 * Centralizes account rules (minimum balance, interest rate, overdraft limit,
 * FD interest) in in-memory lookup tables, keyed by tenure tier, instead of
 * scattering hardcoded constants across each account class.
 */
public class AccountRulesEngine {

    // ===== Tenure tiers =====
    private static final String NEW = "NEW";             // 0 to 1 year
    private static final String STANDARD = "STANDARD";   // 1 to 3 years
    private static final String PREMIUM = "PREMIUM";     // 3 to 5 years
    private static final String PRIVILEGE = "PRIVILEGE"; // 5+ years

    // ===== Lookup tables (Savings) =====
    private static final Map<String, Double> SAVINGS_MIN_BALANCE = new HashMap<>();
    private static final Map<String, Double> SAVINGS_INTEREST_RATE = new HashMap<>();

    static {
        SAVINGS_MIN_BALANCE.put(NEW, 10000.0);
        SAVINGS_MIN_BALANCE.put(STANDARD, 7500.0);
        SAVINGS_MIN_BALANCE.put(PREMIUM, 5000.0);
        SAVINGS_MIN_BALANCE.put(PRIVILEGE, 2500.0);

        SAVINGS_INTEREST_RATE.put(NEW, 2.70);
        SAVINGS_INTEREST_RATE.put(STANDARD, 3.00);
        SAVINGS_INTEREST_RATE.put(PREMIUM, 3.50);
        SAVINGS_INTEREST_RATE.put(PRIVILEGE, 4.00);
    }

    // ===== Current account rules =====
    private static final double OVERDRAFT_MULTIPLIER = 2.5;
    private static final double OVERDRAFT_FLOOR = 25000.0;

    // ===== Fixed Deposit rules (by minimum term in months) =====
    private static final Map<Integer, Double> FD_INTEREST_RATE = new HashMap<>();

    static {
        FD_INTEREST_RATE.put(0, 5.0);   // < 6 months
        FD_INTEREST_RATE.put(6, 5.75);  // 6 to 11 months
        FD_INTEREST_RATE.put(12, 6.5);  // 12+ months
    }

    private AccountRulesEngine() {
        // static utility class - no instances
    }

    /** Maps raw tenure in years to a tenure tier bucket. */
    private static String tenureBucket(int tenureYears) {
        if (tenureYears >= 5) {
            return PRIVILEGE;
        }
        if (tenureYears >= 3) {
            return PREMIUM;
        }
        if (tenureYears >= 1) {
            return STANDARD;
        }
        return NEW;
    }

    /** Step 2: minimum balance required for a Savings account, by tenure. */
    public static double getSavingsMinBalance(int tenureYears) {
        return SAVINGS_MIN_BALANCE.get(tenureBucket(tenureYears));
    }

    /** Step 3: annual interest rate (%) for a Savings account, by tenure. */
    public static double getSavingsInterestRate(int tenureYears) {
        return SAVINGS_INTEREST_RATE.get(tenureBucket(tenureYears));
    }

    /** Step 4.1: overdraft limit for a Current account = 2.5x monthly turnover, floor Rs 25000. */
    public static double getCurrentOverdraftLimit(double monthlyTurnover) {
        double computedLimit = monthlyTurnover * OVERDRAFT_MULTIPLIER;
        return Math.max(computedLimit, OVERDRAFT_FLOOR);
    }

    /** Step 4.2: FD interest rate (%) by deposit duration in months. */
    public static double getFDInterestRate(int months) {
        if (months >= 12) {
            return FD_INTEREST_RATE.get(12);
        }
        if (months >= 6) {
            return FD_INTEREST_RATE.get(6);
        }
        return FD_INTEREST_RATE.get(0);
    }
}
