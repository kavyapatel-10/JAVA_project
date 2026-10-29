package service;
import exception.BankException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.*;
import util.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import static util.StatementFormatter.buildStatement;
public class MiniBank {
    private static final Path DATA_FILE = Path.of("data", "accounts.ser");
    private static final Path LOG_DIR = Path.of("logs");
    private static final Path LOG_FILE = LOG_DIR.resolve("transactions.log");
    private static final Path REPORT_FILE = Path.of("data", "end-of-day-report.txt");
    public static void main(String[] args) {
        Repository<Account> repository = new Repository<>();
        AccountStore store = new AccountStore();
        BankService bank = new BankService(repository, store, LOG_FILE);
        try { bank.loadState(DATA_FILE); } catch (IOException e) { System.out.println("Could not load saved accounts: " + e.getMessage()); }
        if (bank.listAccounts().isEmpty()) {
            try {
                bank.openAccount("Rahul", "SAVINGS", 10000);
                bank.openAccount("Priya", "CURRENT", 5000);
                bank.openAccount("Amit", "FIXED", 20000);
            } catch (BankException | IOException e) { System.out.println("Initial setup: " + e.getMessage()); }
        }
        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                printMenu();
                System.out.print("Choose an option: ");
                String input = scanner.nextLine().trim();
                try {
                    switch (input) {
                        case "1" -> openAccount(scanner, bank);
                        case "2" -> { String id = ask(scanner, "Account number: "); long amount = readLong(scanner, "Deposit amount: "); bank.deposit(id, amount); System.out.println("Deposit successful."); }
                        case "3" -> { String id = ask(scanner, "Account number: "); long amount = readLong(scanner, "Withdrawal amount: "); bank.withdraw(id, amount); System.out.println("Withdrawal successful."); }
                        case "4" -> { String from = ask(scanner, "From account: "); String to = ask(scanner, "To account: "); long amount = readLong(scanner, "Transfer amount: "); bank.transfer(from, to, amount); System.out.println("Transfer successful."); }
                        case "5" -> { String id = ask(scanner, "Account number: "); System.out.println(bank.statement(id)); }
                        case "6" -> bank.listAccounts().forEach(System.out::println);
                        case "7" -> bank.sortedAccounts().forEach(System.out::println);
                        case "8" -> System.out.println(bank.report(LOG_DIR, REPORT_FILE));
                        case "9" -> runConcurrencyDemo();
                        case "10" -> runBatchDemo(bank);
                        case "11" -> { bank.saveState(DATA_FILE); System.out.println("Accounts saved."); }
                        case "12" -> { bank.saveState(DATA_FILE); System.out.println("Accounts saved. Goodbye!"); running = false; }
                        case "13" -> runAnnotationDemo();
                        case "14" -> { try (BankSession session = new BankSession()) { session.performOperation(); } }
                        case "15" -> AuditLogger.printAuditedMethods(BankService.class);
                        case "16" -> ProducerConsumerDemo.run();
                        case "17" -> DeadlockDemo.runUnsafeDemo();
                        default -> System.out.println("Please choose a listed option.");
                    }
                } catch (InsufficientFundsException e) {
                    System.out.println("Transaction failed: " + e.getMessage() + " Shortfall: Rs. " + e.getShortfall());
                } catch (InvalidAmountException e) {
                    System.out.println("Invalid amount: " + e.getMessage());
                } catch (BankException e) {
                    System.out.println("Banking error: " + e.getMessage());
                } catch (IOException e) {
                    System.out.println("File operation failed: " + e.getMessage());
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid whole number.");
                } finally {
                    // Per-operation cleanup/logging hook for the menu loop.
                }
            }
        } catch (Exception e) { System.out.println("Application error: " + e.getMessage()); }
    }
    private static void printMenu() {
        System.out.println("\n========== MiniBank ==========");
        System.out.println("1. Open account"); System.out.println("2. Deposit"); System.out.println("3. Withdraw");
        System.out.println("4. Transfer"); System.out.println("5. Statement"); System.out.println("6. List accounts");
        System.out.println("7. Sort accounts by balance"); System.out.println("8. End-of-day report");
        System.out.println("9. Thread-safety demo"); System.out.println("10. Batch transaction demo");
        System.out.println("11. Save accounts"); System.out.println("12. Exit");
        System.out.println("13. Annotation validation demo"); System.out.println("14. AutoCloseable demo"); System.out.println("15. Audit methods"); System.out.println("16. Producer-consumer demo"); System.out.println("17. Unsafe deadlock demo (daemon threads)");
    }
    private static void openAccount(Scanner scanner, BankService bank) throws BankException, IOException {
        String owner=ask(scanner,"Owner name: "); String type=ask(scanner,"Type (SAVINGS/CURRENT/FIXED): "); long opening=readLong(scanner,"Opening balance: ");
        Account account=bank.openAccount(owner,type,opening); System.out.println("Account opened: " + account.getAccountNumber());
    }
    private static String ask(Scanner scanner, String prompt) { System.out.print(prompt); return scanner.nextLine().trim(); }
    private static long readLong(Scanner scanner, String prompt) { return Long.parseLong(ask(scanner,prompt)); }
    private static void runConcurrencyDemo() {
        Account account = new SavingsAccount("Thread Demo", 1000, 0);
        List<Thread> threads = new ArrayList<>();
        for (int i=0; i<10; i++) { Thread t = new Thread(new AccountWorker(account, 1000, 1), "Deposit-Worker-"+(i+1)); threads.add(t); t.start(); }
        for (Thread t : threads) { try { t.join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); System.out.println("Join interrupted"); return; } }
        System.out.println("Expected balance: 11000; actual balance: " + account.getBalance());
        System.out.println("Deposit/withdraw are synchronized, so the balance should be correct.");
    }
    private static void runBatchDemo(BankService bank) {
        try (TransactionProcessor processor = new TransactionProcessor()) {
            for (Account account : bank.listAccounts()) processor.register(account);
            for (Account account : bank.listAccounts()) processor.submit(() -> {
                try { account.deposit(10); System.out.println(Thread.currentThread().getName()+" deposited Rs. 10 into "+account.getAccountNumber()); }
                catch (InvalidAmountException e) { System.out.println(e.getMessage()); }
            });
            processor.stop(); System.out.println("Batch completed.");
        } catch (Exception e) { System.out.println("Batch error: " + e.getMessage()); }
    }
    private static void runAnnotationDemo() {
        Account sample = new SavingsAccount("Annotation Test", 0, 0);
        String[] errors = AnnotationValidator.validate(sample);
        if (errors.length == 0) System.out.println("Object is valid.");
        else for (String error : errors) System.out.println("Validation error: " + error);
    }
}
