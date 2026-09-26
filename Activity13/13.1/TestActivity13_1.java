public class TestActivity13_1 {

    public static void main(String[] args) {
        System.out.println("=== Activity 13.1: Hardcoded Rules Engine Test ===");

        printTenureRow(0);
        printTenureRow(2);
        printTenureRow(4);
        printTenureRow(6);

        System.out.println("Rules Engine lookup completed successfully!");
    }

    private static void printTenureRow(int tenureYears) {
        double minBalance = AccountRulesEngine.getSavingsMinBalance(tenureYears);
        double interestRate = AccountRulesEngine.getSavingsInterestRate(tenureYears);
        System.out.printf("Tenure %d yrs -> Min Balance: Rs %-7s | Interest: %s%%%n",
                tenureYears, minBalance, interestRate);
    }
}
