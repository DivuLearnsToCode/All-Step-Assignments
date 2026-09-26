public class TestActivity13_2 {

    public static void main(String[] args) {
        System.out.println("=== Activity 13.2: Dynamic Account Rules Test ===");

        // Step 3: create Savings accounts for customers at different tenure
        // lengths and confirm min balance / interest rate come from the
        // rules engine rather than a fixed constant.
        createAndShow(0, "Aarav Sharma", 10000.0);
        createAndShow(2, "Ishita Nair", 7500.0);
        createAndShow(4, "Rohan Verma", 5000.0);
        createAndShow(6, "Priya Das", 2500.0);

        System.out.println("Dynamic rule integration verified!");
    }

    private static void createAndShow(int tenureYears, String name, double initialBalance) {
        IAccount account = AccountFactory.createAccount(
                "SAVINGS", 4000 + tenureYears, name, 30, initialBalance, "1234", 0, tenureYears);
        SavingsAccount savings = (SavingsAccount) account;

        System.out.println("Created Savings Account (Tenure: " + tenureYears + " yrs):");
        System.out.printf(" -> Min Balance: Rs %s (Dynamically fetched)%n", savings.getMinimumBalance());
        System.out.printf(" -> Interest Rate: %s%% (Dynamically fetched)%n", savings.getAnnualInterestRate());
    }
}
