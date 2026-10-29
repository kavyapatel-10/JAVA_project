public class CurrentAccount extends Account {


private long overdraftLimit;

public CurrentAccount(String ownerName, long openingBalance,
                      long overdraftLimit) {
    super(ownerName, openingBalance);
    this.overdraftLimit = Math.max(0, overdraftLimit);
}

@Override
public double interestRate() {
    return 0.0;
}

@Override
public boolean canWithdraw(long amount) {
    return amount > 0
            && balance >= -overdraftLimit
            && amount <= balance + overdraftLimit;
}

public long getOverdraftLimit() {
    return overdraftLimit;
}


}
