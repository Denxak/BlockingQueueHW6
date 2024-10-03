package ait.mediation;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BlkQueueImpl<T> implements BlkQueue<T> {
    private final LinkedList<T> queue = new LinkedList<>();
    private final int maxSize;
    private Lock mutex = new ReentrantLock();
    private Condition notFull = mutex.newCondition();
    private Condition notEmpty = mutex.newCondition();

    public BlkQueueImpl(int maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public void push(T message) {
        mutex.lock();
        try {
            while (queue.size() == maxSize) {
                try {
                    notFull.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
                queue.addLast(message);
                notEmpty.signal();
            }
        } finally {
            mutex.unlock();
        }
    }

    @Override
    public T pop() {
        mutex.lock();
        try {
            while (queue.isEmpty()) {
                try {
                    notEmpty.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                    return null;
                }
            }
            T res = queue.removeFirst();
            notFull.signal();
            return res;
        } finally {
            mutex.unlock();
        }
    }
}
