public class FixedDepositAccount extends AbstractAccount {

    private int termMonths;

    public FixedDepositAccount(int accountNumber, String name, int age, double initialBalance,
                                int pin, int termMonths) {
        super(accountNumber, name, age, initialBalance, "FixedDeposit", pin);
        this.termMonths = termMonths;
    }

    @Override
    public void processDebit(double amount) throws AccountException {
        // Fixed deposits block any withdrawal before maturity
        throw new AccountException("Premature withdrawal not allowed on Fixed Deposit accounts before maturity.");
    }

    public int getTermMonths() {
        return termMonths;
    }
}
