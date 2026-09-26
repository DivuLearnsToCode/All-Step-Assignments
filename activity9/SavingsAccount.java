public class SavingsAccount extends AbstractAccount {

    private static final double MINIMUM_BALANCE = 500.0;

    public SavingsAccount(int accountNumber, String name, int age, double initialBalance, int pin) {
        super(accountNumber, name, age, initialBalance, "Savings", pin);
    }

    @Override
    public void processDebit(double amount) throws AccountException {
        double newBalance = balance - amount;
        if (newBalance < MINIMUM_BALANCE) {
            throw new MinimumBalanceViolationException(
                    "Cannot withdraw. Minimum balance of Rs " + MINIMUM_BALANCE +
                            " required. Available after withdrawal: Rs " + newBalance);
        }
        setBalance(newBalance);
    }

    public double getMinimumBalance() {
        return MINIMUM_BALANCE;
    }
}
