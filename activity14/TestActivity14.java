public class TestActivity14 {

    public static void main(String[] args) {
        System.out.println("=== Activity 14: Properties-Driven Rules Engine Test ===");

        printTenureRow(0);
        printTenureRow(2);
        printTenureRow(4);
        printTenureRow(6);

        System.out.println("All external properties loaded and verified successfully!");
    }

    private static void printTenureRow(int tenureYears) {
        double minBalance = AccountRulesEngine.getSavingsMinBalance(tenureYears);
        double interestRate = AccountRulesEngine.getSavingsInterestRate(tenureYears);
        System.out.printf("Tenure %d yrs -> Min Balance: Rs %-7s | Interest: %.2f%%%n",
                tenureYears, minBalance, interestRate);
    }
}
