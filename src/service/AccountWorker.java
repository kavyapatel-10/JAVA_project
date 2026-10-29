package service;
import model.Account;
public class AccountWorker implements Runnable {
    private final Account account; private final int times; private final long amount;
    public AccountWorker(Account account, int times, long amount) { this.account=account; this.times=times; this.amount=amount; }
    @Override public void run() {
        System.out.println(Thread.currentThread().getName() + " started");
        try { for (int i=0; i<times; i++) account.deposit(amount); }
        catch (Exception e) { System.err.println(Thread.currentThread().getName() + " failed: " + e.getMessage()); }
        finally { System.out.println(Thread.currentThread().getName() + " finished"); }
    }
}
