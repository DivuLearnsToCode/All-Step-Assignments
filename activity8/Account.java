public abstract class Account {

    private static final int MIN_AGE = 18;
    private static final int MIN_PIN = 1000;
    private static final int MAX_PIN = 9999;

    private int accountNumber;
    private String name;
    private int age;
    private double balance;
    private String status;
    private Integer pin;
    private double dailyWithdrawalTotal;

    // ===== Abstract Methods =====
    public abstract double getMinimumBalance();
    public abstract String getAccountType();

    // ===== Constructor =====
    public Account(int accountNumber, String name, int age, double initialBalance)
            throws IllegalArgumentException {

        if (age < MIN_AGE) {
            throw new IllegalArgumentException(
                    "Customer must be at least " + MIN_AGE + " years old. Provided: " + age);
        }

        double minBalance = getMinimumBalance();
        if (initialBalance < minBalance) {
            throw new IllegalArgumentException(
                    getAccountType() + " account requires minimum balance of \u20B9" + minBalance +
                            ". Provided: \u20B9" + initialBalance);
        }

        this.accountNumber = accountNumber;
        this.name = name;
        this.age = age;
        this.balance = initialBalance;
        this.status = "Active";
        this.pin = null;
        this.dailyWithdrawalTotal = 0.0;
    }

    // ===== Business Methods =====
    public void deposit(double amount) throws InvalidAmountException, InactiveAccountException {
        validateActive();
        validateAmount(amount);
        balance += amount;
    }

    public void withdraw(double amount, int pin)
            throws InvalidAmountException, InsufficientBalanceException,
            MinimumBalanceViolationException, InactiveAccountException, InvalidPinException {

        validateActive();
        validatePin(pin);
        validateAmount(amount);

        if (amount > balance) {
            throw new InsufficientBalanceException(
                    "Insufficient balance. Available: \u20B9" + balance + ", Requested: \u20B9" + amount);
        }

        double newBalance = balance - amount;
        if (newBalance < getMinimumBalance()) {
            throw new MinimumBalanceViolationException(
                    "Cannot withdraw. Minimum balance of \u20B9" + getMinimumBalance() +
                            " required. Available after withdrawal: \u20B9" + newBalance);
        }

        balance = newBalance;
        updateDailyWithdrawalTotal(amount);
    }

    // ===== Account Status Management =====
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

    // ===== PIN Management =====
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

    // ===== Helper / Protected Methods (usable by subclasses) =====
    protected void validateActive() throws InactiveAccountException {
        if (!"Active".equals(status)) {
            throw new InactiveAccountException("Account is inactive. Please reopen the account or contact support.");
        }
    }

    protected void validatePin(int pin) throws InvalidPinException {
        if (this.pin == null) {
            throw new InvalidPinException("PIN not set for this account");
        }
        if (this.pin != pin) {
            throw new InvalidPinException("Incorrect PIN");
        }
    }

    protected void validateAmount(double amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Amount must be positive. Provided: \u20B9" + amount);
        }
    }

    protected void setBalance(double balance) {
        this.balance = balance;
    }

    protected void updateDailyWithdrawalTotal(double amount) {
        this.dailyWithdrawalTotal += amount;
    }

    // ===== Getters =====
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

    public String getStatus() {
        return status;
    }

    public double getDailyWithdrawalTotal() {
        return dailyWithdrawalTotal;
    }

    @Override
    public String toString() {
        return "Account #" + accountNumber + " | " + name + " (" + age + " yrs) | " +
                getAccountType() + " | \u20B9" + balance + " | " + status;
    }
}
