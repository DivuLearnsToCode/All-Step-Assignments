public abstract class AbstractAccount implements IAccount {

    private static final String PIN_PATTERN = "\\d{4}";

    protected int accountNumber;
    protected String name;
    protected int age;
    protected double balance;
    protected String accountType;
    protected String status;
    protected String pin;

    public AbstractAccount(int accountNumber, String name, int age, double initialBalance,
                            String accountType, String pin) {
        if (pin == null || !pin.matches(PIN_PATTERN)) {
            throw new IllegalArgumentException("PIN must be a 4-digit number");
        }
        this.accountNumber = accountNumber;
        this.name = name;
        this.age = age;
        this.balance = initialBalance;
        this.accountType = accountType;
        this.status = "Active";
        this.pin = pin;
    }

    // ===== Shared Concrete Methods (from IAccount) =====

    // NOTE: IAccount.deposit() declares only InvalidAmountException, so this
    // implementation can no longer also throw InactiveAccountException the
    // way earlier activities did -- a deposit is now accepted regardless of
    // account status. See the write-up for why that's a deliberate trade-off
    // of matching the interface contract exactly, not an oversight.
    @Override
    public void deposit(double amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be positive. Provided: Rs " + amount);
        }
        balance += amount;
    }

    protected void validatePin(String pin) throws InvalidPinException {
        if (this.pin == null) {
            throw new InvalidPinException("PIN not set for this account");
        }
        if (!this.pin.equals(pin)) {
            throw new InvalidPinException("Incorrect PIN");
        }
    }

    public void changePin(String oldPin, String newPin) throws InvalidPinException, IllegalArgumentException {
        validatePin(oldPin);
        if (newPin == null || !newPin.matches(PIN_PATTERN)) {
            throw new IllegalArgumentException("PIN must be a 4-digit number");
        }
        this.pin = newPin;
    }

    @Override
    public void displayAccountInfo() {
        System.out.println("Account #" + accountNumber + " | " + name + " (" + age + " yrs) | " +
                accountType + " | Rs " + balance + " | " + status);
    }

    // ===== Abstract Method (implemented differently by each subclass) =====
    public abstract void processDebit(double amount) throws AccountException;

    // ===== Template Method: fixed withdrawal workflow (from IAccount) =====
    @Override
    public final void withdraw(double amount, String pin)
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

    // ===== Getters / helpers usable by subclasses and callers (from IAccount) =====
    protected void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public int getAccountNumber() {
        return accountNumber;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public double getBalance() {
        return balance;
    }

    @Override
    public String getAccountType() {
        return accountType;
    }

    @Override
    public String getStatus() {
        return status;
    }

    public boolean hasPin() {
        return pin != null;
    }
}
