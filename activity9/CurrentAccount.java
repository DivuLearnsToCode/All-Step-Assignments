public class CurrentAccount extends AbstractAccount {

    private static final double OVERDRAFT_LIMIT = 5000.0;
    private double overdraftUsed;

    public CurrentAccount(int accountNumber, String name, int age, double initialBalance, int pin) {
        super(accountNumber, name, age, initialBalance, "Current", pin);
        this.overdraftUsed = 0.0;
    }

    @Override
    public void processDebit(double amount) throws AccountException {
        double newBalance = balance - amount;
        double overdraftNeeded = (newBalance < 0) ? -newBalance : 0.0;

        if (overdraftNeeded > OVERDRAFT_LIMIT) {
            throw new InsufficientBalanceException(
                    "Overdraft limit of Rs " + OVERDRAFT_LIMIT + " exceeded. Requested overdraft: Rs " + overdraftNeeded);
        }

        overdraftUsed = overdraftNeeded;
        setBalance(newBalance);
    }

    public double getOverdraftLimit() {
        return OVERDRAFT_LIMIT;
    }

    public double getOverdraftUsed() {
        return overdraftUsed;
    }
}
