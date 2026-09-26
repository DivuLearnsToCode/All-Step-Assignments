public class TestActivity10 {

    public static void main(String[] args) {
        System.out.println("=== Activity 10: Banking Operations Suite ===");

        boolean allPassed = true;

        // ===== Step 1: Create Account Portfolio =====
        SavingsAccount savings = new SavingsAccount(1001, "John Doe", 28, 10000.0, 1111);
        CurrentAccount current = new CurrentAccount(1002, "Jane Smith", 32, 5000.0, 2222);
        SalaryAccount salary = new SalaryAccount(1003, "Priya Rao", 26, 2000.0, 3333);

        AbstractAccount[] portfolio = { savings, current, salary };
        TransactionEngine engine = new TransactionEngine(portfolio);

        // A salary credit this cycle, so the monthly check below has history to find.
        try {
            salary.creditSalary(45000.0);
        } catch (AccountException e) {
            allPassed = false;
            System.out.println("Setup EXCEPTION: " + e.getMessage());
        }

        // ===== Step 2: Fund Transfer (success case) =====
        double transferAmount = 3000.0;
        try {
            engine.transferFunds(savings, current, transferAmount, 1111);
            System.out.println("Transfer Rs " + (int) transferAmount + " from Savings to Current: SUCCESS");
        } catch (AccountException e) {
            allPassed = false;
            System.out.println("Transfer Rs " + (int) transferAmount + " from Savings to Current: FAILED (" + e.getMessage() + ")");
        }
        System.out.println("Savings Balance: Rs " + savings.getBalance() + " | Current Balance: Rs " + current.getBalance());

        if (savings.getBalance() != 7000.0 || current.getBalance() != 8000.0) {
            allPassed = false;
        }

        // ===== Fund Transfer (failure case: wrong PIN) =====
        double savingsBeforeFailedTransfer = savings.getBalance();
        double currentBeforeFailedTransfer = current.getBalance();
        try {
            engine.transferFunds(savings, current, 500.0, 9999);
            // If we get here, no exception was thrown - that's a failure of this test.
            allPassed = false;
            System.out.println("Failed Transfer (Wrong PIN): Exception NOT thrown [FAIL]");
        } catch (AccountException e) {
            boolean balancesUnchanged = savings.getBalance() == savingsBeforeFailedTransfer
                    && current.getBalance() == currentBeforeFailedTransfer;
            if (balancesUnchanged) {
                System.out.println("Failed Transfer (Wrong PIN): Exception caught, no balance changed [PASS]");
            } else {
                allPassed = false;
                System.out.println("Failed Transfer (Wrong PIN): Exception caught, but balances changed [FAIL]");
            }
        }

        // ===== Step 3: Process Monthly Banking Cycle =====
        engine.processMonthlyCycle();
        System.out.println("Monthly Interest Cycle processed for all qualifying accounts.");

        System.out.println(allPassed ? "All banking operations passed!" : "Some banking operations FAILED.");
    }
}
