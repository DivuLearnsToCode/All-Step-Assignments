package com.gdb.ui;

import com.gdb.command.TransactionCommand;
import com.gdb.domain.IAccount;
import com.gdb.domain.Transaction;
import com.gdb.service.AccountService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Menu-driven console front end. All banking work goes through AccountService;
 * this class only gathers input and prints results.
 */
public class AccountUI {

    // 📝 STEP 1
    private final AccountService service;
    private final Scanner scanner;

    // 📝 STEP 2
    public AccountUI(AccountService service) {
        this.service = service;
        this.scanner = new Scanner(System.in);
    }

    // 📝 STEP 3
    public void start() {
        while (true) {
            try {
                displayMainMenu();
                int choice = readInt("Enter your choice: ");
                switch (choice) {
                    case 1: handleOpenAccount(); break;
                    case 2: handleDeposit(); break;
                    case 3: handleWithdraw(); break;
                    case 4: handleTransfer(); break;
                    case 5: handleCloseAccount(); break;
                    case 6: handleViewAccount(); break;
                    case 7: handleViewTransactions(); break;
                    case 8:
                        System.out.println("Thank you! Goodbye.");
                        return;
                    default:
                        System.out.println("Invalid choice. Please enter a number from 1 to 8.");
                }
            } catch (NoSuchElementException endOfInput) {
                // stdin closed (Ctrl+D or piped input ran out): exit instead of looping forever
                System.out.println();
                System.out.println("Input ended. Goodbye.");
                return;
            } catch (Exception e) {
                // Any business or validation failure becomes a message, never a crash.
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }

    // 📝 STEP 4
    private void displayMainMenu() {
        System.out.println();
        System.out.println("========================================");
        System.out.println("   GLOBAL DIGITAL BANK");
        System.out.println("========================================");
        System.out.println("1. Open Account");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Transfer");
        System.out.println("5. Close Account");
        System.out.println("6. View Account Details");
        System.out.println("7. View Transaction History");
        System.out.println("8. Exit");
        System.out.println("========================================");
    }

    // 📝 STEP 5
    private void handleOpenAccount() {
        System.out.println();
        System.out.println("--- Open Account ---");
        String type = readString("Account Type (Savings/Current/FixedDeposit/Salary): ");
        String name = readString("Name: ");
        int age = readInt("Age: ");
        double balance = readDouble("Initial Balance: ");

        IAccount account = service.openAccount(type, name, age, balance);
        System.out.println("SUCCESS: " + account.getSummary());

        // The account exists but cannot be used until it has a PIN, so keep asking.
        while (true) {
            String pin = readString("Set 4-digit PIN: ");
            try {
                account.setPin(pin);
                System.out.println("PIN set successfully.");
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }

    // 📝 STEP 6
    private void handleDeposit() throws Exception {
        System.out.println();
        System.out.println("--- Deposit ---");
        int accountNumber = readInt("Account Number: ");
        double amount = readDouble("Amount to deposit: ");

        Transaction txn = service.deposit(accountNumber, amount);
        System.out.println("SUCCESS: Deposited Rs. " + txn.getAmount() + " to #" + accountNumber
                + ". New balance: Rs. " + txn.getBalanceAfter());
    }

    // 📝 STEP 7
    private void handleWithdraw() throws Exception {
        System.out.println();
        System.out.println("--- Withdraw ---");
        int accountNumber = readInt("Account Number: ");
        double amount = readDouble("Amount to withdraw: ");
        String pin = readString("PIN: ");

        Transaction txn = service.withdraw(accountNumber, amount, pin);
        System.out.println("SUCCESS: Withdrew Rs. " + txn.getAmount() + " from #" + accountNumber
                + ". New balance: Rs. " + txn.getBalanceAfter());
    }

    // 📝 STEP 8
    private void handleTransfer() throws Exception {
        System.out.println();
        System.out.println("--- Transfer ---");
        int from = readInt("From Account Number: ");
        int to = readInt("To Account Number: ");
        double amount = readDouble("Amount to transfer: ");
        String pin = readString("PIN of source account: ");

        Transaction txn = service.transfer(from, to, amount, pin);
        System.out.println("SUCCESS: Transferred Rs. " + txn.getAmount() + " from #" + from + " to #" + to
                + ". New balance of #" + from + ": Rs. " + txn.getBalanceAfter());
    }

    // 📝 STEP 9
    private void handleCloseAccount() throws Exception {
        System.out.println();
        System.out.println("--- Close Account ---");
        int accountNumber = readInt("Account Number: ");
        String pin = readString("PIN: ");

        service.closeAccount(accountNumber, pin);
        System.out.println("SUCCESS: Account #" + accountNumber + " has been closed.");
    }

    // 📝 STEP 10
    private void handleViewAccount() throws Exception {
        System.out.println();
        System.out.println("--- Account Details ---");
        int accountNumber = readInt("Account Number: ");

        System.out.println(service.getAccount(accountNumber).getSummary());
    }

    // 📝 STEP 11
    private void handleViewTransactions() {
        System.out.println();
        System.out.println("--- Transaction History ---");
        List<TransactionCommand> history = service.getTransactionHistory();
        if (history.isEmpty()) {
            System.out.println("No transactions recorded yet.");
            return;
        }
        for (int i = 0; i < history.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + history.get(i).getTransaction());
        }
    }

    // 📝 STEP 12
    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter a whole number.");
            }
        }
    }

    // 📝 STEP 13
    private double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                double value = Double.parseDouble(line);
                // parseDouble accepts "NaN" and "Infinity", which would corrupt a balance
                if (Double.isNaN(value) || Double.isInfinite(value)) {
                    throw new NumberFormatException();
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount. Please enter a number such as 2500 or 2500.50.");
            }
        }
    }

    // 📝 STEP 14
    private String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}
