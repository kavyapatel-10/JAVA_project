package service;
import java.util.concurrent.atomic.AtomicInteger;
public final class ProducerConsumerDemo {
    private ProducerConsumerDemo() {}
    public static void run() {
        TransactionBuffer<String> buffer = new TransactionBuffer<>(3);
        AtomicInteger processed = new AtomicInteger();
        Thread consumer = new Thread(() -> {
            try {
                String transaction;
                while ((transaction = buffer.take()) != null) {
                    System.out.println("Consumer processed: " + transaction);
                    processed.incrementAndGet();
                }
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }, "Transaction-Consumer");
        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) buffer.put("TX-" + i);
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            finally { buffer.close(); }
        }, "Transaction-Producer");
        consumer.start(); producer.start();
        try { producer.join(); consumer.join(); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        System.out.println("Producer-consumer completed; processed=" + processed.get());
    }
}
