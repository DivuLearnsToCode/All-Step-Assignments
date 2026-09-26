public class TransactionEngine {

    private final AbstractAccount[] portfolio;

    public TransactionEngine(AbstractAccount[] portfolio) {
        this.portfolio = portfolio;
    }

    public AbstractAccount[] getPortfolio() {
        return portfolio;
    }

    /**
     * Transfers funds from one account to another.
     * Withdrawal is attempted first; the destination is only credited if the
     * withdrawal succeeds. If the withdrawal throws, the destination is left
     * untouched and the exception is rethrown for the caller to handle.
     */
    public void transferFunds(AbstractAccount source, AbstractAccount destination,
                               double amount, int pin) throws AccountException {

        // Step 1: attempt to withdraw from the source using the provided PIN.
        source.withdraw(amount, pin);

        // Step 2: withdrawal succeeded -> credit the destination.
        try {
            destination.deposit(amount);
        } catch (AccountException depositFailure) {
            // Destination couldn't accept the funds (e.g. inactive/invalid amount).
            // Roll back the source withdrawal so no money is lost mid-transfer.
            try {
                source.deposit(amount);
            } catch (AccountException rollbackFailure) {
                throw new AccountException(
                        "Critical: withdrawal succeeded but deposit and rollback both failed. " +
                                "Source may be out of sync. Original error: " + depositFailure.getMessage());
            }
            throw depositFailure;
        }
    }

    /**
     * Processes one monthly banking cycle across the whole portfolio:
     * - SavingsAccount: monthly interest is applied to the balance.
     * - SalaryAccount: salary credit history for the cycle is checked (and reset).
     * Other account types are left untouched.
     */
    public void processMonthlyCycle() {
        for (AbstractAccount account : portfolio) {
            if (account instanceof SavingsAccount) {
                SavingsAccount savings = (SavingsAccount) account;
                savings.applyMonthlyInterest();
            } else if (account instanceof SalaryAccount) {
                SalaryAccount salary = (SalaryAccount) account;
                salary.checkSalaryCreditHistory();
            }
        }
    }
}
