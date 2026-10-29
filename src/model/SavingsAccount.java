package model;
import java.io.Serializable;
public class SavingsAccount extends Account implements Premium, Serializable {
    private static final long serialVersionUID = 1L;
    private final long minBalance;
    public SavingsAccount(String ownerName, long openingBalance, long minBalance) { super(ownerName, openingBalance); this.minBalance = Math.max(0L, minBalance); }
    @Override public double interestRate() { return 4.0; }
    @Override public boolean canWithdraw(long amount) { return amount > 0 && getBalance() - amount >= minBalance; }
    @Override protected long withdrawalShortfall(long amount) { return Math.max(0L, amount - Math.max(0L, getBalance() - minBalance)); }
    public long getMinBalance() { return minBalance; }
}
