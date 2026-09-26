public class AccountEnhanced {

    private static final double MIN_BALANCE_SAVINGS = 500.0;
    private static final double MIN_BALANCE_CURRENT = 1000.0;
    private static final int MIN_AGE = 18;
    private static final int MIN_PIN = 1000;
    private static final int MAX_PIN = 9999;

    private int accountNumber;
    private String name;
    private int age;
    private double balance;
    private String accountType;
    private String status;
    private Integer pin;

    public AccountEnhanced(int accountNumber, String name, int age, double initialBalance, String accountType) {
        this.accountNumber = accountNumber;
        this.name = name;

        // Age validation - self-correct
        this.age = (age < MIN_AGE) ? MIN_AGE : age;

        // Account type validation - self-correct
        if (!"Savings".equals(accountType) && !"Current".equals(accountType)) {
            accountType = "Savings";
        }
        this.accountType = accountType;

        // Minimum balance validation - self-correct
        double minBalance = getMinimumBalance();
        this.balance = (initialBalance < minBalance) ? minBalance : initialBalance;

        this.status = "Active";
        this.pin = null;
    }

    public boolean deposit(double amount) {
        if (!"Active".equals(status)) {
            return false;
        }
        if (amount <= 0) {
            return false;
        }
        balance += amount;
        return true;
    }

    public boolean withdraw(double amount, int pin) {
        if (!"Active".equals(status)) {
            return false;
        }
        if (this.pin == null || this.pin != pin) {
            return false;
        }
        if (amount <= 0 || amount > balance) {
            return false;
        }
        balance -= amount;
        return true;
    }

    public boolean closeAccount() {
        if ("Inactive".equals(status)) {
            return false;
        }
        status = "Inactive";
        return true;
    }

    public boolean reopenAccount() {
        if ("Active".equals(status)) {
            return false;
        }
        status = "Active";
        return true;
    }

    public boolean setPin(int pin) {
        if (pin < MIN_PIN || pin > MAX_PIN) {
            return false;
        }
        this.pin = pin;
        return true;
    }

    public boolean verifyPin(int pin) {
        return this.pin != null && this.pin == pin;
    }

    public boolean hasPin() {
        return pin != null;
    }

    private double getMinimumBalance() {
        return "Savings".equals(accountType) ? MIN_BALANCE_SAVINGS : MIN_BALANCE_CURRENT;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }
}
