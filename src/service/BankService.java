package service;
import exception.*;
import model.*;
import model.annotation.Audit;
import util.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
public class BankService {
    private final Repository<Account> repository;
    private final AccountStore store;
    private final Path logFile;
    public BankService(Repository<Account> repository, AccountStore store, Path logFile) { this.repository=repository; this.store=store; this.logFile=logFile; }
    @Audit public Account openAccount(String owner, String type, long openingBalance) throws BankException, IOException {
        if (owner == null || owner.isBlank() || owner.length() > 80) throw new BankException("Owner name must contain 1–80 characters.");
        if (openingBalance < 0) throw new InvalidAmountException("Opening balance cannot be negative.");
        Account account;
        switch (type.toUpperCase()) {
            case "SAVINGS" -> account = new SavingsAccount(owner, openingBalance, 1000);
            case "CURRENT" -> account = new CurrentAccount(owner, openingBalance, 5000);
            case "FIXED", "FIXED_DEPOSIT" -> account = new FixedDepositAccount(owner, openingBalance);
            default -> throw new BankException("Unknown account type. Use SAVINGS, CURRENT, or FIXED.");
        }
        repository.save(account); store.add(account); TransactionLog.append(logFile, System.currentTimeMillis()+"|OPEN|0|"+account.getAccountNumber());
        return account;
    }
    private Account requireAccount(String id) throws AccountNotFoundException {
        Account account = repository.findById(id);
        if (account == null) throw new AccountNotFoundException("No account found with number " + id);
        return account;
    }
    @Audit public void deposit(String id, long amount) throws BankException, IOException {
        Account account=requireAccount(id); account.deposit(amount); repository.save(account); store.addHistory("DEPOSIT|"+id+"|"+amount); TransactionLog.append(logFile, System.currentTimeMillis()+"|DEPOSIT|"+amount+"|"+id);
    }
    @Audit public void withdraw(String id, long amount) throws BankException, IOException {
        Account account=requireAccount(id); account.withdraw(amount); repository.save(account); store.addHistory("WITHDRAW|"+id+"|"+amount); TransactionLog.append(logFile, System.currentTimeMillis()+"|WITHDRAW|"+amount+"|"+id);
    }
    @Audit public void transfer(String fromId, String toId, long amount) throws BankException, IOException {
        Account from=requireAccount(fromId), to=requireAccount(toId); from.transfer(to, amount); repository.save(from); repository.save(to);
        store.addHistory("TRANSFER|"+fromId+"|"+toId+"|"+amount); TransactionLog.append(logFile, System.currentTimeMillis()+"|WITHDRAW|"+amount+"|"+fromId); TransactionLog.append(logFile, System.currentTimeMillis()+"|DEPOSIT|"+amount+"|"+toId);
    }
    public String statement(String id) throws AccountNotFoundException { return StatementFormatter.buildStatement(requireAccount(id)); }
    public List<Account> listAccounts() { return repository.findAll(); }
    public String report(Path logsFolder, Path reportFile) throws IOException { return ReportGenerator.generate(logsFolder, reportFile); }
    public void saveState(Path file) throws IOException { StatePersister.save(repository.findAll().toArray(new Account[0]), file); }
    public void loadState(Path file) throws IOException {
        for (Account account : StatePersister.load(file)) { repository.save(account); store.add(account); }
    }
    public ArrayList<Account> sortedAccounts() { return store.sortedByBalance(); }
}
