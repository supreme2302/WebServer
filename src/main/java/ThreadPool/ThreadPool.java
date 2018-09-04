package ThreadPool;

import AwesomeQueue.AwesomeQueue;

public class ThreadPool {
    private final AwesomeQueue<Runnable> taskQueue = new AwesomeQueue<Runnable>();
    private volatile boolean isRunning = true;

    public ThreadPool(int nThreads) {
        for (int i = 0; i < nThreads; i++) {
            new Thread(() -> {
                while (isRunning) {
                    Runnable nextTask = null;
                    synchronized (taskQueue) {
                        while (!taskQueue.hasItems()) {
                            try {
                                taskQueue.wait();
                            } catch (InterruptedException err) {err.printStackTrace();}

                        }
                        nextTask = taskQueue.dequeue();
                    }
                    if (nextTask != null) {
                        nextTask.run();
                    }

                }
            })
                    .start();
        }
    }

    public void execute(Runnable command) {
        synchronized (taskQueue) {
            if (isRunning) {
//                System.out.println("execute,  " + Thread.currentThread().getName());
                taskQueue.enqueue(command);
                taskQueue.notify();
            }
        }
    }
}