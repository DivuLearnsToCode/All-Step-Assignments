/**
 * Centralizes account creation so callers never call "new SavingsAccount(...)",
 * "new CurrentAccount(...)" etc. directly. Callers ask for a type by name and
 * get back an IAccount -- this is the Factory Design Pattern: one place decides
 * which concrete class to instantiate, and the rest of the program only ever
 * depends on the interface.
 *
 * Step 2: createAccount now also accepts customer tenure (in years) so it can
 * be threaded through to account types whose rules depend on it -- currently
 * SavingsAccount, via AccountRulesEngine.
 */
public class AccountFactory {

    // Convenience overload: no term, no tenure (defaults both to 0 -> new customer).
    public static IAccount createAccount(String accountType, int accountNumber, String name,
                                          int age, double initialBalance, String pin) {
        return createAccount(accountType, accountNumber, name, age, initialBalance, pin, 0, 0);
    }

    // Convenience overload: term supplied (for FDs), tenure defaults to 0.
    public static IAccount createAccount(String accountType, int accountNumber, String name, int age,
                                          double initialBalance, String pin, int termMonths) {
        return createAccount(accountType, accountNumber, name, age, initialBalance, pin, termMonths, 0);
    }

    // Full overload: term (FD-specific) and tenure (Savings-specific) both supplied.
    public static IAccount createAccount(String accountType, int accountNumber, String name, int age,
                                          double initialBalance, String pin, int termMonths, int tenureYears) {
        if (accountType == null) {
            throw new IllegalArgumentException("Account type cannot be null");
        }

        switch (accountType.toUpperCase()) {
            case "SAVINGS":
                return new SavingsAccount(accountNumber, name, age, initialBalance, pin, tenureYears);

            case "CURRENT":
                return new CurrentAccount(accountNumber, name, age, initialBalance, pin);

            case "FIXED_DEPOSIT":
            case "FD":
                return new FixedDepositAccount(accountNumber, name, age, initialBalance, pin, termMonths);

            case "SALARY":
                return new SalaryAccount(accountNumber, name, age, initialBalance, pin);

            default:
                throw new IllegalArgumentException("Unknown account type: " + accountType);
        }
    }
}
