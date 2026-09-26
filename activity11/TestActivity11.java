public class TestActivity11 {

    public static void main(String[] args) {
        System.out.println("=== Activity 11: Interface & Factory Pattern Test ===");

        boolean allPassed = true;

        IAccount savings = null;
        IAccount current = null;
        IAccount fixedDeposit = null;
        IAccount salary = null;

        // ===== Create a SAVINGS account through the factory =====
        try {
            savings = AccountFactory.createAccount("SAVINGS", 2001, "Rajesh Sharma", 30, 5000.0, "1111");
            System.out.println("Factory created: SAVINGS account for " + savings.getName());
        } catch (IllegalArgumentException e) {
            allPassed = false;
            System.out.println("Factory FAILED for SAVINGS: " + e.getMessage());
        }

        // ===== Create a CURRENT account through the factory =====
        try {
            current = AccountFactory.createAccount("CURRENT", 2002, "Priya Patel", 34, 2000.0, "2222");
            System.out.println("Factory created: CURRENT account for " + current.getName());
        } catch (IllegalArgumentException e) {
            allPassed = false;
            System.out.println("Factory FAILED for CURRENT: " + e.getMessage());
        }

        // ===== Create a FIXED_DEPOSIT account through the factory =====
        try {
            fixedDeposit = AccountFactory.createAccount("FIXED_DEPOSIT", 2003, "Amit Kumar", 45, 100000.0, "3333", 12);
            System.out.println("Factory created: FIXED_DEPOSIT account for " + fixedDeposit.getName());
        } catch (IllegalArgumentException e) {
            allPassed = false;
            System.out.println("Factory FAILED for FIXED_DEPOSIT: " + e.getMessage());
        }

        // ===== Create a SALARY account through the factory =====
        try {
            salary = AccountFactory.createAccount("SALARY", 2004, "Sneha Verma", 27, 1000.0, "4444");
            System.out.println("Factory created: SALARY account for " + salary.getName());
        } catch (IllegalArgumentException e) {
            allPassed = false;
            System.out.println("Factory FAILED for SALARY: " + e.getMessage());
        }

        // ===== Sanity check: an unknown type must be rejected =====
        try {
            AccountFactory.createAccount("CRYPTO", 2005, "Nobody", 30, 100.0, "5555");
            allPassed = false; // should never reach here
            System.out.println("Factory did NOT reject an unknown account type [FAIL]");
        } catch (IllegalArgumentException expected) {
            // expected: unknown type correctly rejected, nothing to print
        }

        if (savings == null || current == null || fixedDeposit == null || salary == null) {
            allPassed = false;
        }

        System.out.println(allPassed
                ? "All accounts successfully created through AccountFactory!"
                : "Some factory operations FAILED.");
    }
}
