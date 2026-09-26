/**
 * The contract every bank account type must satisfy, regardless of how it is
 * implemented internally. Code that only depends on IAccount (like
 * AccountFactory's callers) never needs to know whether it's holding a
 * SavingsAccount, a CurrentAccount, or anything else -- this is what lets
 * banking operations be "decoupled" from the concrete account classes.
 */
public interface IAccount {

    // ===== Getters =====
    int getAccountNumber();
    String getName();
    double getBalance();
    String getAccountType();
    String getStatus();

    // ===== Core Operations =====
    void deposit(double amount) throws InvalidAmountException;
    void withdraw(double amount, String pin) throws AccountException;

    // ===== Display =====
    void displayAccountInfo();
}
