public class TestAccount {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println(" GLOBAL DIGITAL BANK - ACCOUNT TEST");
        System.out.println("==================================================");

        System.out.println(">>> 1. Creating Account");
        Account acc1 = new Account(1001, "John Doe", 25, 1000.0, "Savings");
        System.out.println("Account created!");
        printAccount(acc1);

        System.out.println(">>> 2. Deposit Money");
        boolean depositResult = acc1.deposit(500.0);
        System.out.println("Depositing \u20B9500.0: " + (depositResult ? "SUCCESS" : "FAILED"));
        System.out.println("New balance: \u20B9" + acc1.getBalance());
        depositResult = acc1.deposit(-100.0);
        System.out.println("Depositing \u20B9-100.0: " + (depositResult ? "SUCCESS" : "FAILED (Invalid amount)"));

        System.out.println(">>> 3. Withdraw Money");
        boolean withdrawResult = acc1.withdraw(200.0);
        System.out.println("Withdrawing \u20B9200.0: " + (withdrawResult ? "SUCCESS" : "FAILED"));
        System.out.println("New balance: \u20B9" + acc1.getBalance());
        withdrawResult = acc1.withdraw(2000.0);
        System.out.println("Withdrawing \u20B92000.0: " + (withdrawResult ? "SUCCESS" : "FAILED (Insufficient balance)"));
        System.out.println("Current balance: \u20B9" + acc1.getBalance());

        System.out.println(">>> 4. Creating Another Account");
        Account acc2 = new Account(1002, "Jane Smith", 30, 2000.0, "Current");
        printAccount(acc2);

        System.out.println(">>> 5. All Accounts");
        printAccount(acc1);
        printAccount(acc2);

        System.out.println("==================================================");
        System.out.println(" TEST COMPLETED!");
        System.out.println("==================================================");
    }

    private static void printAccount(Account acc) {
        System.out.println("Account #" + acc.getAccountNumber() + " | " + acc.getName() +
                " (" + acc.getAge() + " yrs) | " + acc.getAccountType() + " | \u20B9" +
                acc.getBalance() + " | " + acc.getStatus());
    }
}
