package util;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import model.Account;
public class AccountStore {
    private final Map<String, Account> accounts = new HashMap<>();
    private final ArrayList<String> transactionHistory = new ArrayList<>();
    public synchronized void add(Account account) { accounts.put(account.getAccountNumber(), account); }
    public synchronized Account get(String accountNumber) { return accounts.get(accountNumber); }
    public synchronized ArrayList<Account> getAll() { return new ArrayList<>(accounts.values()); }
    public synchronized ArrayList<Account> sortedByBalance() { ArrayList<Account> result = getAll(); result.sort(Comparator.comparingLong(Account::getBalance)); return result; }
    public synchronized void addHistory(String entry) { transactionHistory.add(entry); }
    public synchronized ArrayList<String> getTransactionHistory() { return new ArrayList<>(transactionHistory); }
}
