package service;
public final class DeadlockDemo {
    private DeadlockDemo() {}
    /** Runs a deliberately unsafe demonstration with daemon threads so the app can still exit. */
    public static void runUnsafeDemo() {
        Object accountA = new Object();
        Object accountB = new Object();
        Thread ab = new Thread(() -> { synchronized (accountA) { pause(); synchronized (accountB) { System.out.println("A -> B completed"); } } }, "Unsafe-A-to-B");
        Thread ba = new Thread(() -> { synchronized (accountB) { pause(); synchronized (accountA) { System.out.println("B -> A completed"); } } }, "Unsafe-B-to-A");
        ab.setDaemon(true); ba.setDaemon(true);
        System.out.println("Starting intentionally unsafe opposite-order locks; threads may deadlock.");
        ab.start(); ba.start();
        try { Thread.sleep(150); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        System.out.println("Safe approach: Account.transfer locks accounts in account-number order.");
    }
    private static void pause() { try { Thread.sleep(50); } catch (InterruptedException e) { Thread.currentThread().interrupt(); } }
}
