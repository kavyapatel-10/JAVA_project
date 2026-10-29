import java.util.Scanner;

public class MiniBank {

```
// Record to store bank information
record BankInfo(String name, String branch) {}

// Enum containing available menu options
enum MenuOption {
    OPEN_ACCOUNT,
    DEPOSIT,
    WITHDRAW,
    TRANSFER,
    EXIT
}

public static void main(String[] args) {

    Scanner sc = new Scanner(System.in);

    // Display application header
    BankInfo bank = new BankInfo("MiniBank", "Vadodara");

    System.out.println("================================");
    System.out.println("       " + bank.name());
    System.out.println("       Branch: " + bank.branch());
    System.out.println("================================");

    int choice;

    // Display menu repeatedly until EXIT is selected
    do {
        System.out.println("\n========== MAIN MENU ==========");
        System.out.println("1. Open Account");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Transfer");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");

        // Read user's choice
        if (!sc.hasNextInt()) {
            System.out.println("Invalid input! Please enter a number.");
            sc.next();
            choice = 0;
            continue;
        }

        choice = sc.nextInt();

        // Switch expression to handle menu choices
        if (choice >= 1 && choice <= 5) {
            MenuOption option = switch (choice) {
                case 1 -> MenuOption.OPEN_ACCOUNT;
                case 2 -> MenuOption.DEPOSIT;
                case 3 -> MenuOption.WITHDRAW;
                case 4 -> MenuOption.TRANSFER;
                case 5 -> MenuOption.EXIT;
                default -> throw new IllegalStateException();
            };

            String message = switch (option) {
                case OPEN_ACCOUNT ->
                    "Open Account — to be implemented in a later lab.";
                case DEPOSIT ->
                    "Deposit — to be implemented in a later lab.";
                case WITHDRAW ->
                    "Withdraw — to be implemented in a later lab.";
                case TRANSFER ->
                    "Transfer — to be implemented in a later lab.";
                case EXIT ->
                    "Thank you for using MiniBank!";
            };

            System.out.println(message);

        } else {
            System.out.println("Invalid choice! Please select a number from 1 to 5.");
        }

    } while (choice != 5);

    sc.close();
}
```

}
