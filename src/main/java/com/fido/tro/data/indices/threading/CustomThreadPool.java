package com.fido.tro.data.indices.threading;

import java.util.concurrent.LinkedBlockingQueue;

@SuppressWarnings("unused")
public class CustomThreadPool {
    public final LinkedBlockingQueue<Runnable> queue;
    //Thread pool size
    private final int poolSize;
    private final WorkerThread[] workers;
    private Boolean forcedInterrupt = false;

    public CustomThreadPool(int poolSize) {
        this.poolSize = poolSize;
        queue = new LinkedBlockingQueue<>();
        workers = new WorkerThread[poolSize];

        for (int i = 0; i < poolSize; i++) {
            workers[i] = new WorkerThread();
            workers[i].start();
        }
    }

    public void execute(Runnable task) {
        synchronized (queue) {
            queue.add(task);
            queue.notify();
        }
    }

    public void shutdown() {
        System.out.println("Shutting down thread pool");
        synchronized (forcedInterrupt) {
            forcedInterrupt = true;
        }
        for (int i = 0; i < poolSize; i++) {
            do {
            } while (queue.size() > 0);
            workers[i].interrupt();
            workers[i] = null;
        }
        System.out.println("Pool is shutted down.");
    }

    private class WorkerThread extends Thread {
        public void run() {
            Runnable task = null;

            while (true) {
                synchronized (queue) {
                    while (queue.isEmpty()) {
                        try {
                            queue.wait();
                        } catch (InterruptedException e) {
                            if (!forcedInterrupt) {
                                System.out.println("An error occurred while queue is waiting: " + e.getMessage());
                            }
                        }
                    }
                    try {
                        task = queue.take();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    task.run();
                } catch (RuntimeException e) {
                    System.out.println("Thread pool is interrupted due to an issue: " + e.getMessage());
                }
            }
        }
    }
}