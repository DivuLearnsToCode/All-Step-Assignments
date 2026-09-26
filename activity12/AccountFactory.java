/**
 * Centralizes account creation so callers never call "new SavingsAccount(...)",
 * "new CurrentAccount(...)" etc. directly. Callers ask for a type by name and
 * get back an IAccount -- this is the Factory Design Pattern: one place decides
 * which concrete class to instantiate, and the rest of the program only ever
 * depends on the interface.
 */
public class AccountFactory {

    // Overload without a term -- convenient for every type except FIXED_DEPOSIT.
    public static IAccount createAccount(String accountType, int accountNumber, String name,
                                          int age, double initialBalance, String pin) {
        return createAccount(accountType, accountNumber, name, age, initialBalance, pin, 0);
    }

    public static IAccount createAccount(String accountType, int accountNumber, String name, int age,
                                          double initialBalance, String pin, int termMonths) {
        if (accountType == null) {
            throw new IllegalArgumentException("Account type cannot be null");
        }

        switch (accountType.toUpperCase()) {
            case "SAVINGS":
                return new SavingsAccount(accountNumber, name, age, initialBalance, pin);

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
