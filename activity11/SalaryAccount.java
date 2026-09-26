import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SalaryAccount extends AbstractAccount {

    private final List<Double> salaryCreditHistory;
    private boolean creditedThisCycle;

    public SalaryAccount(int accountNumber, String name, int age, double initialBalance, String pin) {
        super(accountNumber, name, age, initialBalance, "Salary", pin);
        this.salaryCreditHistory = new ArrayList<>();
        this.creditedThisCycle = false;
    }

    @Override
    public void processDebit(double amount) throws AccountException {
        // Salary accounts carry no overdraft and no minimum balance requirement.
        if (amount > balance) {
            throw new InsufficientBalanceException(
                    "Insufficient balance. Available: Rs " + balance + ", Requested: Rs " + amount);
        }
        setBalance(balance - amount);
    }

    /** Records a salary credit for this cycle (e.g. from an employer transfer). */
    public void creditSalary(double amount) throws InvalidAmountException {
        deposit(amount);
        salaryCreditHistory.add(amount);
        creditedThisCycle = true;
    }

    /**
     * Checks whether a salary credit occurred this cycle, then resets the flag
     * so the next monthly cycle starts fresh. Used by the monthly banking cycle.
     */
    public boolean checkSalaryCreditHistory() {
        boolean received = creditedThisCycle;
        creditedThisCycle = false;
        return received;
    }

    public List<Double> getSalaryCreditHistory() {
        return Collections.unmodifiableList(salaryCreditHistory);
    }
}
