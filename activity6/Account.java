public class Account {

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

    public Account(int accountNumber, String name, int age, double initialBalance, String accountType)
            throws IllegalArgumentException {

        if (age < MIN_AGE) {
            throw new IllegalArgumentException(
                    "Customer must be at least " + MIN_AGE + " years old. Provided: " + age);
        }

        if (!"Savings".equals(accountType) && !"Current".equals(accountType)) {
            throw new IllegalArgumentException(
                    "Account type must be 'Savings' or 'Current'. Provided: " + accountType);
        }

        double minBalance = "Savings".equals(accountType) ? MIN_BALANCE_SAVINGS : MIN_BALANCE_CURRENT;
        if (initialBalance < minBalance) {
            throw new IllegalArgumentException(
                    accountType + " account requires minimum balance of \u20B9" + minBalance +
                            ". Provided: \u20B9" + initialBalance);
        }

        this.accountNumber = accountNumber;
        this.name = name;
        this.age = age;
        this.balance = initialBalance;
        this.accountType = accountType;
        this.status = "Active";
        this.pin = null;
    }

    public void deposit(double amount) throws InvalidAmountException, InactiveAccountException {
        validateActive();
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be positive. Provided: \u20B9" + amount);
        }
        balance += amount;
    }

    public void withdraw(double amount, int pin)
            throws InvalidAmountException, InsufficientBalanceException,
            MinimumBalanceViolationException, InactiveAccountException, InvalidPinException {

        validateActive();

        if (this.pin == null) {
            throw new InvalidPinException("PIN not set for this account");
        }
        if (this.pin != pin) {
            throw new InvalidPinException("Incorrect PIN");
        }
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be positive. Provided: \u20B9" + amount);
        }
        if (amount > balance) {
            throw new InsufficientBalanceException(
                    "Insufficient balance. Available: \u20B9" + balance + ", Requested: \u20B9" + amount);
        }

        double minBalance = getMinimumBalance();
        double newBalance = balance - amount;
        if (newBalance < minBalance) {
            throw new MinimumBalanceViolationException(
                    "Cannot withdraw. Minimum balance of \u20B9" + minBalance +
                            " required. Available after withdrawal: \u20B9" + newBalance);
        }

        balance = newBalance;
    }

    public void closeAccount() throws IllegalStateException {
        if ("Inactive".equals(status)) {
            throw new IllegalStateException("Account is already closed");
        }
        status = "Inactive";
    }

    public void reopenAccount() throws IllegalStateException {
        if ("Active".equals(status)) {
            throw new IllegalStateException("Account is already active");
        }
        status = "Active";
    }

    public void setPin(int pin) throws IllegalArgumentException {
        if (pin < MIN_PIN || pin > MAX_PIN) {
            throw new IllegalArgumentException("PIN must be a 4-digit number");
        }
        this.pin = pin;
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

    private void validateActive() throws InactiveAccountException {
        if (!"Active".equals(status)) {
            throw new InactiveAccountException("Account is inactive. Please reopen the account or contact support.");
        }
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
}
