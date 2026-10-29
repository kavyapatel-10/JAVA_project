public class Account {


private final String accountNumber;
private String ownerName;
private long balance;
private boolean active;

private static long accountCounter = 0;

// Generate unique account numbers
private static String generateAccountNumber() {
    accountCounter++;
    return String.format("AC%04d", accountCounter);
}

// Constructor with owner name and opening balance
public Account(String ownerName, long openingBalance) {
    this.accountNumber = generateAccountNumber();
    this.ownerName = ownerName;
    this.balance = openingBalance >= 0 ? openingBalance : 0;
    this.active = true;
}

// Constructor with owner name only
public Account(String ownerName) {
    this(ownerName, 0);
}

// Deposit money
public void deposit(long amount) {
    if (active && amount > 0) {
        balance += amount;
        System.out.println("Deposit successful: Rs. " + amount);
    } else {
        System.out.println("Invalid deposit amount or inactive account.");
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

// Getter methods
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


}

