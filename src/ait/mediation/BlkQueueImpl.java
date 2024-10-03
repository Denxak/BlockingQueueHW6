package ait.mediation;

import java.util.LinkedList;

public class BlkQueueImpl<T> implements BlkQueue<T> {
    private final LinkedList<T> queue = new LinkedList<>();
    private final int maxSize;

    public BlkQueueImpl(int maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public synchronized void push(T message) {
        while (queue.size() == maxSize) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
        queue.addLast(message);
        notifyAll();
    }

    @Override
    public synchronized T pop() {
        while (queue.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
                return null;
            }
        }
        T res = queue.removeFirst();
        notifyAll();
        return res;
    }
}
