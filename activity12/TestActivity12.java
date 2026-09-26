public class TestActivity12 {

    public static void main(String[] args) {
        System.out.println("=== Activity 12: Factory-Driven System Suite ===");

        boolean test1 = testSavingsCreationAndDeposit();
        System.out.println("[Test 1] Savings Account Creation & Deposit: " + (test1 ? "[PASS]" : "[FAIL]"));

        boolean test2 = testCurrentOverdraftWithdrawal();
        System.out.println("[Test 2] Current Account Overdraft Withdrawal: " + (test2 ? "[PASS]" : "[FAIL]"));

        boolean test3 = testFixedDepositPrematureWithdrawalBlock();
        System.out.println("[Test 3] Fixed Deposit Premature Withdrawal Block: " + (test3 ? "[PASS]" : "[FAIL]"));

        boolean test4 = testInvalidTypeRejection();
        System.out.println("[Test 4] Invalid Type Rejection: " + (test4 ? "[PASS]" : "[FAIL]"));

        boolean allPassed = test1 && test2 && test3 && test4;
        System.out.println(allPassed
                ? "Factory-driven architecture successfully verified!"
                : "Factory-driven architecture verification FAILED.");
    }

    // ===== Test 1: Savings account, created via the factory, used only via IAccount =====
    private static boolean testSavingsCreationAndDeposit() {
        IAccount savings = AccountFactory.createAccount("SAVINGS", 3001, "Meera Iyer", 29, 1000.0, "1111");

        try {
            savings.deposit(500.0);
        } catch (InvalidAmountException e) {
            return false;
        }
        if (savings.getBalance() != 1500.0) {
            return false;
        }

        // Minimum balance rule: withdrawing down to Rs 300 (below the Rs 500 minimum) must be blocked.
        boolean minimumBalanceEnforced = false;
        try {
            savings.withdraw(1200.0, "1111");
        } catch (MinimumBalanceViolationException expected) {
            minimumBalanceEnforced = true;
        } catch (AccountException unexpected) {
            return false;
        }

        return minimumBalanceEnforced && savings.getBalance() == 1500.0;
    }

    // ===== Test 2: Current account overdraft, allowed up to the limit and blocked beyond it =====
    private static boolean testCurrentOverdraftWithdrawal() {
        IAccount current = AccountFactory.createAccount("CURRENT", 3002, "Karan Mehta", 33, 2000.0, "2222");

        try {
            current.withdraw(6000.0, "2222"); // dips into overdraft, within the Rs 5000 limit
        } catch (AccountException e) {
            return false;
        }
        if (current.getBalance() != -4000.0) {
            return false;
        }

        // Requesting more overdraft than the limit allows must be rejected.
        boolean overdraftLimitEnforced = false;
        try {
            current.withdraw(2000.0, "2222"); // would need Rs 6000 of overdraft, over the Rs 5000 limit
        } catch (InsufficientBalanceException expected) {
            overdraftLimitEnforced = true;
        } catch (AccountException unexpected) {
            return false;
        }

        return overdraftLimitEnforced && current.getBalance() == -4000.0;
    }

    // ===== Test 3: Fixed deposit blocks any withdrawal before maturity =====
    private static boolean testFixedDepositPrematureWithdrawalBlock() {
        IAccount fixedDeposit = AccountFactory.createAccount(
                "FIXED_DEPOSIT", 3003, "Ananya Roy", 41, 50000.0, "3333", 12);

        boolean withdrawalBlocked = false;
        try {
            fixedDeposit.withdraw(1000.0, "3333");
        } catch (AccountException expected) {
            withdrawalBlocked = true;
        }

        return withdrawalBlocked && fixedDeposit.getBalance() == 50000.0;
    }

    // ===== Test 4: An unrecognized account type must be rejected by the factory =====
    private static boolean testInvalidTypeRejection() {
        try {
            AccountFactory.createAccount("CRYPTO", 3004, "Nobody", 30, 100.0, "4444");
            return false; // should never reach here
        } catch (IllegalArgumentException expected) {
            return true;
        }
    }
}
