public abstract class AbstractAccount {

    private static final int MIN_PIN = 1000;
    private static final int MAX_PIN = 9999;

    protected int accountNumber;
    protected String name;
    protected int age;
    protected double balance;
    protected String accountType;
    protected String status;
    protected Integer pin;

    public AbstractAccount(int accountNumber, String name, int age, double initialBalance,
                            String accountType, int pin) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.age = age;
        this.balance = initialBalance;
        this.accountType = accountType;
        this.status = "Active";
        this.pin = pin;
    }

    // ===== Shared Concrete Methods =====
    public void deposit(double amount) throws InvalidAmountException, InactiveAccountException {
        if (!"Active".equals(status)) {
            throw new InactiveAccountException("Account is inactive. Please reopen the account or contact support.");
        }
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be positive. Provided: Rs " + amount);
        }
        balance += amount;
    }

    protected void validatePin(int pin) throws InvalidPinException {
        if (this.pin == null) {
            throw new InvalidPinException("PIN not set for this account");
        }
        if (this.pin != pin) {
            throw new InvalidPinException("Incorrect PIN");
        }
    }

    public void changePin(int oldPin, int newPin) throws InvalidPinException, IllegalArgumentException {
        validatePin(oldPin);
        if (newPin < MIN_PIN || newPin > MAX_PIN) {
            throw new IllegalArgumentException("PIN must be a 4-digit number");
        }
        this.pin = newPin;
    }

    public void displayAccountInfo() {
        System.out.println("Account #" + accountNumber + " | " + name + " (" + age + " yrs) | " +
                accountType + " | Rs " + balance + " | " + status);
    }

    // ===== Abstract Method (implemented differently by each subclass) =====
    public abstract void processDebit(double amount) throws AccountException;

    // ===== Template Method: fixed withdrawal workflow =====
    public final void withdraw(double amount, int pin)
            throws InvalidPinException, InactiveAccountException, InvalidAmountException, AccountException {

        // Step 1: Validate PIN
        validatePin(pin);

        // Step 2: Validate account status
        if (!"Active".equals(status)) {
            throw new InactiveAccountException("Account is inactive. Please reopen the account or contact support.");
        }

        // Step 3: Validate amount
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be positive. Provided: Rs " + amount);
        }

        // Step 4: Delegate the actual debit logic to the subclass
        processDebit(amount);
    }

    // ===== Account status management =====
    public void closeAccount() {
        status = "Inactive";
    }

    public void reopenAccount() {
        status = "Active";
    }

    // ===== Getters usable by subclasses / callers =====
    protected void setBalance(double balance) {
        this.balance = balance;
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

    public boolean hasPin() {
        return pin != null;
    }
}
