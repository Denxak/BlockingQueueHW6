package ait.mediation;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BlkQueueImpl<T> implements BlkQueue<T> {
    private final BlockingQueue<T> queue;

    public BlkQueueImpl(int maxSize) {
        this.queue = new LinkedBlockingQueue<T>(maxSize);
    }

    @Override
    public void push(T message) {
        try {
            queue.put(message);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    @Override
    public T pop() {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
            return null;
        }
    }
}
