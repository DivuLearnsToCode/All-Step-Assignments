import java.util.ArrayList;
import java.util.List;

public class TestAccountExceptions {

    public static void main(String[] args) {
        List<Account> accounts = new ArrayList<>();

        System.out.println("============================================================");
        System.out.println(" ACCOUNT TEST WITH EXCEPTIONS");
        System.out.println("============================================================");

        System.out.println(">>> Test 1: Valid Account Creation");
        Account acc1001 = null;
        try {
            acc1001 = new Account(1001, "John Doe", 25, 1000.0, "Savings");
            accounts.add(acc1001);
            System.out.println("SUCCESS: " + format(acc1001));
        } catch (IllegalArgumentException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }
        System.out.println();

        System.out.println(">>> Test 2: Invalid Age (under 18)");
        try {
            new Account(1002, "Kid", 16, 500.0, "Savings");
        } catch (IllegalArgumentException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }
        System.out.println();

        System.out.println(">>> Test 3: Invalid Account Type");
        try {
            new Account(1003, "Test User", 25, 500.0, "Invalid");
        } catch (IllegalArgumentException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }
        System.out.println();

        System.out.println(">>> Test 4: Minimum Balance on Creation");
        System.out.println("Creating Savings account with \u20B9300");
        try {
            new Account(1004, "Bob Wilson", 25, 300.0, "Savings");
        } catch (IllegalArgumentException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }
        System.out.println();

        System.out.println(">>> Test 5: Valid Deposit and Withdrawal");
        Account acc1005 = null;
        try {
            acc1005 = new Account(1005, "Alice Brown", 30, 1000.0, "Current");
            accounts.add(acc1005);
            System.out.println("Account: " + format(acc1005));
            acc1005.setPin(1234);
            System.out.println("Setting PIN 1234: SUCCESS");
            acc1005.deposit(500.0);
            System.out.println("Depositing \u20B9500.0: SUCCESS");
            System.out.println("Balance after deposit: \u20B9" + acc1005.getBalance());
            acc1005.withdraw(200.0, 1234);
            System.out.println("Withdrawing \u20B9200.0: SUCCESS");
            System.out.println("Balance after withdrawal: \u20B9" + acc1005.getBalance());
            System.out.println(format(acc1005));
        } catch (AccountException | IllegalArgumentException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }
        System.out.println();

        System.out.println(">>> Test 6: Invalid Deposit (Negative Amount)");
        System.out.println("Attempting to deposit \u20B9-100.0");
        try {
            acc1005.deposit(-100.0);
        } catch (AccountException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }
        System.out.println();

        System.out.println(">>> Test 7: Insufficient Balance");
        Account acc1006 = null;
        try {
            acc1006 = new Account(1006, "Charlie Green", 35, 500.0, "Savings");
            accounts.add(acc1006);
            acc1006.setPin(1234);
            System.out.println("Account: " + format(acc1006));
            System.out.println("Attempting to withdraw \u20B91000.0");
            acc1006.withdraw(1000.0, 1234);
        } catch (AccountException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }
        System.out.println();

        System.out.println(">>> Test 8: Minimum Balance Violation");
        Account acc1007 = null;
        try {
            acc1007 = new Account(1007, "Diana Prince", 28, 1000.0, "Savings");
            accounts.add(acc1007);
            acc1007.setPin(1234);
            System.out.println("Account: " + format(acc1007));
            System.out.println("Attempting to withdraw \u20B9600.0");
            acc1007.withdraw(600.0, 1234);
        } catch (AccountException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }
        System.out.println();

        System.out.println(">>> Test 9: Inactive Account Operations");
        try {
            Account acc1008 = new Account(1008, "Eve Wilson", 32, 2000.0, "Current");
            accounts.add(acc1008);
            System.out.println("Account: " + format(acc1008));
            acc1008.closeAccount();
            System.out.println("Closing account: SUCCESS");
            System.out.println("Attempting to deposit \u20B9100.0 on closed account");
            try {
                acc1008.deposit(100.0);
            } catch (AccountException e) {
                System.out.println("EXCEPTION: " + e.getMessage());
            }
            acc1008.reopenAccount();
            System.out.println("Reopening account: SUCCESS");
            acc1008.deposit(100.0);
            System.out.println("Depositing \u20B9100.0 after reopen: SUCCESS");
            System.out.println("Balance after deposit: \u20B9" + acc1008.getBalance());
        } catch (AccountException | IllegalStateException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }
        System.out.println();

        System.out.println(">>> Test 10: PIN Verification");
        try {
            Account acc1009 = new Account(1009, "Frank Miller", 40, 1500.0, "Savings");
            accounts.add(acc1009);
            System.out.println("Account: " + format(acc1009));
            acc1009.setPin(1234);
            System.out.println("Setting PIN 1234: SUCCESS");
            acc1009.withdraw(200.0, 1234);
            System.out.println("Withdrawing \u20B9200.0 with correct PIN: SUCCESS");
            System.out.println("Balance: \u20B9" + acc1009.getBalance());
            System.out.println("Attempting to withdraw \u20B9100.0 with incorrect PIN (9999)");
            try {
                acc1009.withdraw(100.0, 9999);
            } catch (AccountException e) {
                System.out.println("EXCEPTION: " + e.getMessage());
            }

            Account acc1010 = new Account(1010, "No Pin Guy", 33, 1000.0, "Savings");
            accounts.add(acc1010);
            System.out.println("Attempting to withdraw \u20B9100.0 without PIN set");
            try {
                acc1010.withdraw(100.0, 1234);
            } catch (AccountException e) {
                System.out.println("EXCEPTION: " + e.getMessage());
            }
        } catch (AccountException | IllegalArgumentException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }
        System.out.println();

        System.out.println(">>> Test 11: All Accounts Summary");
        for (Account acc : accounts) {
            System.out.println(format(acc));
        }

        System.out.println("============================================================");
        System.out.println(" TEST COMPLETED!");
        System.out.println("============================================================");
    }

    private static String format(Account acc) {
        return "Account #" + acc.getAccountNumber() + " | " + acc.getName() +
                " (" + acc.getAge() + " yrs) | " + acc.getAccountType() + " | \u20B9" +
                acc.getBalance() + " | " + acc.getStatus() + " | PIN: " + (acc.hasPin() ? "Yes" : "No");
    }
}
