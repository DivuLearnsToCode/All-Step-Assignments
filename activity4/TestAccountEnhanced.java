import java.util.ArrayList;
import java.util.List;

public class TestAccountEnhanced {

    public static void main(String[] args) {
        List<AccountEnhanced> accounts = new ArrayList<>();

        System.out.println("============================================================");
        System.out.println(" ENHANCED ACCOUNT TEST (BOOLEAN RETURNS)");
        System.out.println("============================================================");

        System.out.println(">>> Test 1: Valid Account Creation");
        AccountEnhanced acc1 = new AccountEnhanced(1001, "John Doe", 25, 1000.0, "Savings");
        accounts.add(acc1);
        printAccount(acc1);
        System.out.println();

        System.out.println(">>> Test 2: Invalid Age (under 18)");
        System.out.println("Creating account with age 16");
        AccountEnhanced acc2 = new AccountEnhanced(1002, "Young Kid", 16, 500.0, "Savings");
        accounts.add(acc2);
        System.out.println("Age auto-corrected to: " + acc2.getAge());
        printAccount(acc2);
        System.out.println();

        System.out.println(">>> Test 3: Invalid Account Type");
        System.out.println("Creating account with type \"Invalid\"");
        AccountEnhanced acc3 = new AccountEnhanced(1003, "Test User", 25, 500.0, "Invalid");
        accounts.add(acc3);
        System.out.println("Account type defaulted to: " + acc3.getAccountType());
        printAccount(acc3);
        System.out.println();

        System.out.println(">>> Test 4: Minimum Balance Enforcement on Creation");
        System.out.println("Creating Savings account with \u20B9300 (below minimum)");
        AccountEnhanced acc4 = new AccountEnhanced(1004, "Bob Wilson", 25, 300.0, "Savings");
        accounts.add(acc4);
        System.out.println("Balance auto-corrected to minimum: \u20B9" + acc4.getBalance());
        printAccount(acc4);
        System.out.println();

        System.out.println(">>> Test 5: Withdrawal with Minimum Balance");
        AccountEnhanced acc5 = new AccountEnhanced(1005, "Alice Brown", 30, 1000.0, "Current");
        acc5.setPin(1234);
        accounts.add(acc5);
        System.out.print("Initial: ");
        printAccount(acc5);
        boolean res = acc5.withdraw(200.0, 1234);
        System.out.println("Withdrawing \u20B9200.0: " + (res ? "SUCCESS" : "FAILED"));
        System.out.println("New balance: \u20B9" + acc5.getBalance());
        System.out.print("After withdrawal: ");
        printAccount(acc5);
        res = acc5.withdraw(900.0, 1234);
        System.out.println("Withdrawing \u20B9900.0 (would leave \u20B9-100): " + (res ? "SUCCESS" : "FAILED (Minimum balance violation)"));
        System.out.println("Current balance: \u20B9" + acc5.getBalance());
        System.out.println();

        System.out.println(">>> Test 6: Account Status Management");
        AccountEnhanced acc6 = new AccountEnhanced(1006, "Charlie Green", 35, 2000.0, "Savings");
        accounts.add(acc6);
        System.out.print("Initial: ");
        printAccount(acc6);
        boolean closed = acc6.closeAccount();
        System.out.println("Closing account: " + (closed ? "SUCCESS" : "FAILED"));
        System.out.print("After close: ");
        printAccount(acc6);
        boolean depositRes = acc6.deposit(500.0);
        System.out.println("Depositing \u20B9500.0 to closed account: " + (depositRes ? "SUCCESS" : "FAILED (Account inactive)"));
        boolean reopened = acc6.reopenAccount();
        System.out.println("Reopening account: " + (reopened ? "SUCCESS" : "FAILED"));
        System.out.print("After reopen: ");
        printAccount(acc6);
        System.out.println();

        System.out.println(">>> Test 7: PIN Protection");
        AccountEnhanced acc7 = new AccountEnhanced(1007, "Diana Prince", 28, 1500.0, "Savings");
        accounts.add(acc7);
        boolean pinSet = acc7.setPin(1234);
        System.out.println("Setting PIN 1234: " + (pinSet ? "SUCCESS" : "FAILED"));
        boolean w1 = acc7.withdraw(200.0, 1234);
        System.out.println("Withdrawing \u20B9200.0 with correct PIN (1234): " + (w1 ? "SUCCESS" : "FAILED"));
        System.out.println("New balance: \u20B9" + acc7.getBalance());
        boolean w2 = acc7.withdraw(100.0, 9999);
        System.out.println("Withdrawing \u20B9100.0 with incorrect PIN (9999): " + (w2 ? "SUCCESS" : "FAILED (Incorrect PIN)"));
        AccountEnhanced noPin = new AccountEnhanced(9999, "No Pin", 30, 1000.0, "Savings");
        boolean w3 = noPin.withdraw(100.0, 1234);
        System.out.println("Withdrawing \u20B9100.0 with PIN not set: " + (w3 ? "SUCCESS" : "FAILED (PIN not set)"));
        System.out.println();

        System.out.println(">>> Test 8: All Accounts Summary");
        for (AccountEnhanced acc : accounts) {
            printAccount(acc);
        }

        System.out.println("============================================================");
        System.out.println(" ENHANCED TEST COMPLETED!");
        System.out.println("============================================================");
    }

    private static void printAccount(AccountEnhanced acc) {
        System.out.println("Account #" + acc.getAccountNumber() + " | " + acc.getName() +
                " (" + acc.getAge() + " yrs) | " + acc.getAccountType() + " | \u20B9" +
                acc.getBalance() + " | " + acc.getStatus() + " | PIN: " + (acc.hasPin() ? "Yes" : "No"));
    }
}
