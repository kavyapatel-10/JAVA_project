package util;
import java.util.Map;
import java.util.concurrent.*;
import model.Account;
public class TransactionProcessor implements AutoCloseable {
    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    private final ConcurrentHashMap<String, Account> sharedAccounts = new ConcurrentHashMap<>();
    public void register(Account account) { sharedAccounts.put(account.getAccountNumber(), account); }
    public Map<String, Account> getSharedAccounts() { return sharedAccounts; }
    public void submit(Runnable task) { executor.execute(task); }
    public void stop() throws InterruptedException {
        executor.shutdown();
        if (!executor.awaitTermination(30, TimeUnit.SECONDS)) { executor.shutdownNow(); if (!executor.awaitTermination(5, TimeUnit.SECONDS)) throw new IllegalStateException("Transaction tasks did not terminate"); }
    }
    @Override public void close() { try { stop(); } catch (InterruptedException e) { executor.shutdownNow(); Thread.currentThread().interrupt(); } }
}
