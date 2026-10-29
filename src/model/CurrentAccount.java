package model;
import java.io.Serializable;
public class CurrentAccount extends Account implements Serializable {
    private static final long serialVersionUID = 1L;
    private final long overdraftLimit;
    public CurrentAccount(String ownerName, long openingBalance, long overdraftLimit) { super(ownerName, openingBalance); this.overdraftLimit = Math.max(0L, overdraftLimit); }
    @Override public double interestRate() { return 0.0; }
    @Override public boolean canWithdraw(long amount) { return amount > 0 && getBalance() >= -overdraftLimit && amount <= getBalance() + overdraftLimit; }
    @Override protected long withdrawalShortfall(long amount) { return Math.max(0L, amount - Math.max(0L, getBalance() + overdraftLimit)); }
    public long getOverdraftLimit() { return overdraftLimit; }
}
