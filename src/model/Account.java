package model;
import exception.BankException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.annotation.Id;
import model.annotation.Positive;
import java.io.Serializable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Objects;
public abstract class Account implements Transactable, InterestBearing, Comparable<Account>, Serializable {
    private static final long serialVersionUID = 1L;
    @Id private final String accountNumber;
    private String ownerName;
    @Positive private long balance;
    private boolean active;
    private static long accountCounter = 0;
    private static synchronized String generateAccountNumber() { return String.format("AC%04d", ++accountCounter); }
    protected Account(String ownerName, long openingBalance) {
        this.accountNumber = generateAccountNumber();
        this.ownerName = Objects.requireNonNull(ownerName, "ownerName");
        this.balance = Math.max(0L, openingBalance);
        this.active = true;
    }
    protected Account(String ownerName) { this(ownerName, 0); }
    @Override public synchronized void deposit(long amount) throws InvalidAmountException {
        if (amount <= 0) throw new InvalidAmountException("Deposit amount must be greater than zero.");
        if (!active) throw new InvalidAmountException("Cannot deposit into an inactive account.");
        try { balance = Math.addExact(balance, amount); }
        catch (ArithmeticException e) { throw new InvalidAmountException("Deposit exceeds the supported balance limit."); }
    }
    @Override public synchronized boolean withdraw(long amount) throws InsufficientFundsException, InvalidAmountException {
        if (amount <= 0) throw new InvalidAmountException("Withdrawal amount must be greater than zero.");
        if (!active) throw new InvalidAmountException("Cannot withdraw from an inactive account.");
        if (!canWithdraw(amount)) throw new InsufficientFundsException("Withdrawal denied for account " + accountNumber + ".", withdrawalShortfall(amount));
        balance -= amount;
        return true;
    }
    protected long withdrawalShortfall(long amount) { return Math.max(0L, amount - Math.max(0L, balance)); }
    public abstract double interestRate();
    public abstract boolean canWithdraw(long amount);
    public void transfer(Account to, long amount) throws BankException {
        if (to == null) throw new BankException("Destination account was not provided.");
        if (to == this) throw new BankException("Cannot transfer money to the same account.");
        Account first = this.accountNumber.compareTo(to.accountNumber) < 0 ? this : to;
        Account second = first == this ? to : this;
        synchronized (first) {
            synchronized (second) {
                withdraw(amount);
                try { to.deposit(amount); }
                catch (BankException e) {
                    // Destination deposit validation happens before any balance change.
                    try { deposit(amount); } catch (BankException rollbackFailure) { e.addSuppressed(rollbackFailure); }
                    throw e;
                }
            }
        }
    }
    public String getAccountNumber() { return accountNumber; }
    public String getOwnerName() { return ownerName; }
    public synchronized long getBalance() { return balance; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        // Keep generated account numbers unique after loading persisted state.
        try {
            long number = Long.parseLong(accountNumber.substring(2));
            synchronized (Account.class) { if (number > accountCounter) accountCounter = number; }
        } catch (RuntimeException ignored) { /* Keep loading older or custom account IDs. */ }
    }
    @Override public int compareTo(Account other) { return this.accountNumber.compareTo(other.accountNumber); }
    @Override public String toString() { return "Account{" + "accountNumber='" + accountNumber + '\'' + ", ownerName='" + ownerName + '\'' + ", balance=" + getBalance() + ", type=" + getClass().getSimpleName() + '}'; }
    @Override public boolean equals(Object obj) { if (this == obj) return true; if (!(obj instanceof Account other)) return false; return accountNumber.equals(other.accountNumber); }
    @Override public int hashCode() { return Objects.hash(accountNumber); }
}
