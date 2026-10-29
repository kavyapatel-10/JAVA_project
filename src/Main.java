public class Main {


public static void main(String[] args) {

    // Create three accounts
    Account[] accounts = {
        new Account("Rahul", 5000),
        new Account("Priya"),
        new Account("Amit", 10000)
    };

    // Perform deposits
    accounts[0].deposit(2000);
    accounts[1].deposit(3000);
    accounts[2].deposit(1500);

    // Perform withdrawals
    boolean result1 = accounts[0].withdraw(1000);
    boolean result2 = accounts[1].withdraw(5000);
    boolean result3 = accounts[2].withdraw(2000);

    System.out.println("\nWithdrawal Results:");
    System.out.println("Rahul: " + result1);
    System.out.println("Priya: " + result2);
    System.out.println("Amit: " + result3);

    // Display account details
    System.out.println("\nAccount Details:");

    for (Account account : accounts) {
        System.out.println("-------------------------");
        System.out.println("Account Number: "
                + account.getAccountNumber());
        System.out.println("Owner: "
                + account.getOwnerName());
        System.out.println("Balance: Rs. "
                + account.getBalance());
        System.out.println("Active: "
                + account.isActive());
    }
}


}
