import java.util.ArrayList;
import java.util.List;

public class TestAccountSubclasses {

    public static void main(String[] args) {
        List<Account> accounts = new ArrayList<>();

        System.out.println("============================================================");
        System.out.println(" ACCOUNT SUBCLASSES TEST (SAVINGS & CURRENT)");
        System.out.println("============================================================");

        System.out.println(">>> Test 1: Creating Accounts");
        SavingsAccount acc1001 = new SavingsAccount(1001, "John Doe", 25, 1000.0);
        CurrentAccount acc1002 = new CurrentAccount(1002, "Jane Smith", 30, 2000.0);
        accounts.add(acc1001);
        accounts.add(acc1002);
        System.out.println("Savings Account: " + summary(acc1001));
        System.out.println("Current Account: " + summary(acc1002));
        System.out.println();

        System.out.println(">>> Test 2: Account Type and Minimum Balance");
        System.out.println("Savings Account - Type: " + acc1001.getAccountType() +
                ", Minimum Balance: \u20B9" + acc1001.getMinimumBalance());
        System.out.println("Current Account - Type: " + acc1002.getAccountType() +
                ", Minimum Balance: \u20B9" + acc1002.getMinimumBalance());
        System.out.println();

        System.out.println(">>> Test 3: Savings Account - Interest Calculation");
        System.out.println("Savings Account: " + acc1001);
        System.out.println("Interest Rate: " + acc1001.getInterestRate() + "% per annum");
        System.out.println("Interest for 1 year: \u20B9" + acc1001.calculateInterest(1));
        System.out.println("Interest for 2 years: \u20B9" + acc1001.calculateInterest(2));
        System.out.println("Interest for 5 years: \u20B9" + acc1001.calculateInterest(5));
        System.out.println("After 2 years with interest: Balance would be \u20B9" +
                (acc1001.getBalance() + acc1001.calculateInterest(2)));
        System.out.println();

        System.out.println(">>> Test 4: Current Account - Overdraft Feature");
        System.out.println("Current Account: " + acc1002);
        System.out.println("Overdraft Limit: \u20B9" + acc1002.getOverdraftLimit());
        System.out.println("Available Overdraft: \u20B9" + acc1002.getAvailableOverdraft());
        System.out.println("Overdraft Used: \u20B9" + acc1002.getOverdraftUsed());
        System.out.println("Is Using Overdraft: " + acc1002.isUsingOverdraft());
        System.out.println();
        try {
            acc1002.setPin(1234);
            System.out.println("Withdrawing \u20B91500.0 (goes below minimum balance of \u20B91000)");
            System.out.println("Balance before: \u20B9" + acc1002.getBalance());
            acc1002.withdraw(1500.0, 1234);
            System.out.println("Withdrawing: \u20B91500.0 - SUCCESS");
            System.out.println("Balance after: \u20B9" + acc1002.getBalance());
            System.out.println("Overdraft Used: \u20B9" + acc1002.getOverdraftUsed());
            System.out.println("Available Overdraft: \u20B9" + acc1002.getAvailableOverdraft());
            System.out.println("Is Using Overdraft: " + acc1002.isUsingOverdraft());
            System.out.println();

            System.out.println("Attempting to withdraw \u20B96000.0 (would exceed overdraft)");
            System.out.println("Available funds: \u20B9" + acc1002.getBalance() + " (balance) + \u20B9" +
                    acc1002.getAvailableOverdraft() + " (overdraft) = \u20B9" +
                    (acc1002.getBalance() + acc1002.getAvailableOverdraft()));
            acc1002.withdraw(6000.0, 1234);
        } catch (AccountException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }
        System.out.println();

        System.out.println("Repaying overdraft of \u20B9500.0");
        System.out.println("Balance before repayment: \u20B9" + acc1002.getBalance());
        System.out.println("Overdraft Used before: \u20B9" + acc1002.getOverdraftUsed());
        acc1002.repayOverdraft(500.0);
        System.out.println("Repaying \u20B9500.0 - SUCCESS");
        System.out.println("Balance after repayment: \u20B9" + acc1002.getBalance());
        System.out.println("Overdraft Used after: \u20B9" + acc1002.getOverdraftUsed());
        System.out.println("Is Using Overdraft: " + acc1002.isUsingOverdraft());
        System.out.println();

        System.out.println(">>> Test 5: Polymorphism - Treating Accounts Uniformly");
        System.out.println("Processing accounts polymorphically:");
        System.out.println();
        SavingsAccount acc1003 = new SavingsAccount(1003, "Bob Wilson", 35, 500.0);
        CurrentAccount acc1004 = new CurrentAccount(1004, "Alice Brown", 28, 1500.0);
        accounts.add(acc1003);
        accounts.add(acc1004);

        double totalBalance = 0.0;
        for (Account acc : accounts) {
            System.out.println(acc + " | Type: " + acc.getAccountType() +
                    ", Min Balance: \u20B9" + acc.getMinimumBalance());
            totalBalance += acc.getBalance();
        }
        System.out.println();
        System.out.println("Total accounts: " + accounts.size());
        System.out.println("Total balance across all accounts: \u20B9" + totalBalance);
        System.out.println();

        System.out.println(">>> Test 6: Validation - Invalid Creation Attempts");
        System.out.println("Attempting to create SavingsAccount with \u20B9300 (below minimum)");
        try {
            new SavingsAccount(9001, "Invalid", 25, 300.0);
        } catch (IllegalArgumentException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }
        System.out.println();

        System.out.println("Attempting to create CurrentAccount with \u20B9500 (below minimum)");
        try {
            new CurrentAccount(9002, "Invalid", 25, 500.0);
        } catch (IllegalArgumentException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }
        System.out.println();

        System.out.println("Attempting to create SavingsAccount with age 16");
        try {
            new SavingsAccount(9003, "Invalid", 16, 500.0);
        } catch (IllegalArgumentException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }
        System.out.println();

        System.out.println(">>> Test 7: Savings Account - PIN and Operations");
        SavingsAccount acc1005 = new SavingsAccount(1005, "Charlie Green", 40, 2000.0);
        accounts.add(acc1005);
        System.out.println("Savings Account: " + acc1005);
        try {
            acc1005.setPin(1234);
            System.out.println("Setting PIN 1234: SUCCESS");
            acc1005.deposit(500.0);
            System.out.println("Depositing \u20B9500.0: SUCCESS");
            System.out.println("Balance after deposit: \u20B9" + acc1005.getBalance());
            acc1005.withdraw(300.0, 1234);
            System.out.println("Withdrawing \u20B9300.0 with correct PIN: SUCCESS");
            System.out.println("Balance after withdrawal: \u20B9" + acc1005.getBalance());
            System.out.println("Attempting to withdraw \u20B92000.0 (would violate minimum balance)");
            acc1005.withdraw(2000.0, 1234);
        } catch (AccountException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }
        System.out.println();

        System.out.println(">>> Test 8: Current Account - Active Status Operations");
        CurrentAccount acc1006 = new CurrentAccount(1006, "Diana Prince", 35, 3000.0);
        accounts.add(acc1006);
        System.out.println("Current Account: " + acc1006);
        try {
            acc1006.closeAccount();
            System.out.println("Closing account: SUCCESS");
            System.out.println("Attempting to deposit \u20B9100.0 on closed account");
            try {
                acc1006.deposit(100.0);
            } catch (AccountException e) {
                System.out.println("EXCEPTION: " + e.getMessage());
            }
            acc1006.reopenAccount();
            System.out.println("Reopening account: SUCCESS");
            acc1006.deposit(100.0);
            System.out.println("Depositing \u20B9100.0 after reopen: SUCCESS");
            System.out.println("Balance after deposit: \u20B9" + acc1006.getBalance());
        } catch (AccountException | IllegalStateException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }
        System.out.println();

        System.out.println(">>> Test 9: All Accounts Summary");
        for (Account acc : accounts) {
            System.out.println(summary(acc));
        }

        System.out.println("============================================================");
        System.out.println(" TEST COMPLETED!");
        System.out.println("============================================================");
    }

    private static String summary(Account acc) {
        return acc + " | PIN: " + (acc.hasPin() ? "Yes" : "No");
    }
}
