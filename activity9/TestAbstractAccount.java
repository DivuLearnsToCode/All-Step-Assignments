public class TestAbstractAccount {

    public static void main(String[] args) {
        System.out.println("=== Activity 9: Abstract Account & Template Pattern ===");

        // ----- Savings: normal withdrawal -----
        SavingsAccount savings = new SavingsAccount(2001, "Test Saver", 28, 10000.0, 1234);
        try {
            savings.withdraw(2000.0, 1234);
            System.out.println("[Savings] Withdraw 2000: SUCCESS | Balance: Rs " + savings.getBalance());
        } catch (AccountException e) {
            System.out.println("[Savings] Withdraw 2000: FAILED | " + e.getMessage());
        }

        // ----- Savings: withdrawal that would breach minimum balance -----
        try {
            savings.withdraw(7600.0, 1234); // 8000 - 7600 = 400, below Rs 500 minimum
            System.out.println("[Savings] Withdraw below min balance: FAILED TO THROW");
        } catch (MinimumBalanceViolationException e) {
            System.out.println("[Savings] Withdraw below min balance: Caught MinimumBalanceViolationException [PASS]");
        } catch (AccountException e) {
            System.out.println("[Savings] Withdraw below min balance: Unexpected " + e.getClass().getSimpleName());
        }

        // ----- Current: debit that dips into overdraft -----
        CurrentAccount current = new CurrentAccount(2002, "Test Current", 35, 2000.0, 4321);
        try {
            current.withdraw(5000.0, 4321); // 2000 - 5000 = -3000, within Rs 5000 overdraft limit
            System.out.println("[Current] Overdraft debit: SUCCESS | Balance: Rs " + current.getBalance());
        } catch (AccountException e) {
            System.out.println("[Current] Overdraft debit: FAILED | " + e.getMessage());
        }

        // ----- Fixed Deposit: premature withdrawal always blocked -----
        FixedDepositAccount fd = new FixedDepositAccount(2003, "Test FD", 40, 50000.0, 1111, 12);
        try {
            fd.withdraw(1000.0, 1111);
            System.out.println("[FixedDeposit] Premature debit: FAILED TO THROW");
        } catch (AccountException e) {
            System.out.println("[FixedDeposit] Premature debit: Caught AccountException [PASS]");
        }

        System.out.println("Template method pattern executed successfully!");
    }
}
