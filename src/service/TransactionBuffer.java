package service;
import java.util.LinkedList;
import java.util.Queue;
public class TransactionBuffer<T> {
    private final Queue<T> queue = new LinkedList<>(); private final int capacity; private boolean closed;
    public TransactionBuffer(int capacity) { if (capacity < 1) throw new IllegalArgumentException("capacity must be positive"); this.capacity=capacity; }
    public synchronized void put(T item) throws InterruptedException {
        while (queue.size() >= capacity && !closed) wait();
        if (closed) throw new IllegalStateException("Buffer is closed");
        queue.add(item); notifyAll();
    }
    public synchronized T take() throws InterruptedException {
        while (queue.isEmpty() && !closed) wait();
        if (queue.isEmpty()) return null;
        T item=queue.remove(); notifyAll(); return item;
    }
    public synchronized void close() { closed=true; notifyAll(); }
}
