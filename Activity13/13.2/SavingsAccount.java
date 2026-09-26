public class SavingsAccount extends AbstractAccount {

    // Step 1: tenure is now the input; minBalance/interestRate are derived
    // per-instance from AccountRulesEngine instead of being fixed constants.
    private final int tenureYears;
    private final double minBalance;
    private final double interestRate;

    // Backward-compatible entry point: no tenure supplied -> treated as a brand-new customer.
    public SavingsAccount(int accountNumber, String name, int age, double initialBalance, String pin) {
        this(accountNumber, name, age, initialBalance, pin, 0);
    }

    public SavingsAccount(int accountNumber, String name, int age, double initialBalance,
                           String pin, int tenureYears) {
        super(accountNumber, name, age, initialBalance, "Savings", pin);
        this.tenureYears = tenureYears;
        this.minBalance = AccountRulesEngine.getSavingsMinBalance(tenureYears);
        this.interestRate = AccountRulesEngine.getSavingsInterestRate(tenureYears);

        if (initialBalance < minBalance) {
            throw new IllegalArgumentException(
                    "Savings account (tenure " + tenureYears + " yrs) requires minimum balance of Rs " +
                            minBalance + ". Provided: Rs " + initialBalance);
        }
    }

    @Override
    public void processDebit(double amount) throws AccountException {
        if (amount > balance) {
            throw new InsufficientBalanceException(
                    "Insufficient balance. Available: Rs " + balance + ", Requested: Rs " + amount);
        }
        double newBalance = balance - amount;
        if (newBalance < minBalance) {
            throw new MinimumBalanceViolationException(
                    "Cannot withdraw. Minimum balance of Rs " + minBalance +
                            " required. Available after withdrawal: Rs " + newBalance);
        }
        setBalance(newBalance);
    }

    /** Applies one month of interest to the current balance, at interestRate / 12. */
    public double applyMonthlyInterest() {
        double interest = balance * (interestRate / 100.0 / 12.0);
        setBalance(balance + interest);
        return interest;
    }

    public double getAnnualInterestRate() {
        return interestRate;
    }

    public double getMinimumBalance() {
        return minBalance;
    }

    public int getTenureYears() {
        return tenureYears;
    }
}
