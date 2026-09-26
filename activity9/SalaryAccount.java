public class SalaryAccount extends AbstractAccount {

    private String employerName;

    public SalaryAccount(int accountNumber, String name, int age, double initialBalance,
                          int pin, String employerName) {
        super(accountNumber, name, age, initialBalance, "Salary", pin);
        this.employerName = employerName;
    }

    @Override
    public void processDebit(double amount) throws AccountException {
        if (amount > balance) {
            throw new InsufficientBalanceException(
                    "Insufficient balance. Available: Rs " + balance + ", Requested: Rs " + amount);
        }
        setBalance(balance - amount);
    }

    public String getEmployerName() {
        return employerName;
    }
}
