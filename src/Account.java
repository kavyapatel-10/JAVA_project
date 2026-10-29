import java.util.Objects;

public class Account {


private final String accountNumber;
private String ownerName;
private long balance;
private boolean active;

private static long accountCounter = 0;

private static String generateAccountNumber() {
    accountCounter++;
    return String.format("AC%04d", accountCounter);
}

// Constructor with opening balance
public Account(String ownerName, long openingBalance) {
    this.accountNumber = generateAccountNumber();
    this.ownerName = ownerName;
    this.balance = Math.max(0, openingBalance);
    this.active = true;
}

// Constructor without opening balance
public Account(String ownerName) {
    this(ownerName, 0);
}

// Deposit money
public void deposit(long amount) {
    if (active && amount > 0) {
        balance += amount;
    }
}

// Withdraw money
public boolean withdraw(long amount) {
    if (active && amount > 0 && balance >= amount) {
        balance -= amount;
        return true;
    }

    return false;
}

// Getters
public String getAccountNumber() {
    return accountNumber;
}

public String getOwnerName() {
    return ownerName;
}

public long getBalance() {
    return balance;
}

public boolean isActive() {
    return active;
}

// Practical 3: Override toString()
@Override
public String toString() {
    return "Account{" +
            "accountNumber='" + accountNumber + '\'' +
            ", ownerName='" + ownerName + '\'' +
            ", balance=" + balance +
            '}';
}

// Practical 3: Override equals()
@Override
public boolean equals(Object o) {
    if (this == o) {
        return true;
    }

    if (!(o instanceof Account)) {
        return false;
    }

    Account account = (Account) o;

    return accountNumber.equals(account.accountNumber);
}

// Practical 3: Override hashCode()
@Override
public int hashCode() {
    return Objects.hash(accountNumber);
}


}
