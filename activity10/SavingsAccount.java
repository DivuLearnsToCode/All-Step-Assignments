public class SavingsAccount extends AbstractAccount {

    private static final double MINIMUM_BALANCE = 500.0;
    private static final double ANNUAL_INTEREST_RATE = 4.0; // 4% per annum

    public SavingsAccount(int accountNumber, String name, int age, double initialBalance, int pin) {
        super(accountNumber, name, age, initialBalance, "Savings", pin);
        if (initialBalance < MINIMUM_BALANCE) {
            throw new IllegalArgumentException(
                    "Savings account requires minimum balance of Rs " + MINIMUM_BALANCE +
                            ". Provided: Rs " + initialBalance);
        }
    }

    @Override
    public void processDebit(double amount) throws AccountException {
        if (amount > balance) {
            throw new InsufficientBalanceException(
                    "Insufficient balance. Available: Rs " + balance + ", Requested: Rs " + amount);
        }
        double newBalance = balance - amount;
        if (newBalance < MINIMUM_BALANCE) {
            throw new MinimumBalanceViolationException(
                    "Cannot withdraw. Minimum balance of Rs " + MINIMUM_BALANCE +
                            " required. Available after withdrawal: Rs " + newBalance);
        }
        setBalance(newBalance);
    }

    /** Applies one month of interest to the current balance, at ANNUAL_INTEREST_RATE / 12. */
    public double applyMonthlyInterest() {
        double interest = balance * (ANNUAL_INTEREST_RATE / 100.0 / 12.0);
        setBalance(balance + interest);
        return interest;
    }

    public double getAnnualInterestRate() {
        return ANNUAL_INTEREST_RATE;
    }

    public double getMinimumBalance() {
        return MINIMUM_BALANCE;
    }
}
