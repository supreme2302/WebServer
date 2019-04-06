package ThreadPool;

import AwesomeQueue.AwesomeQueue;

public class ThreadPool {
    private final AwesomeQueue<Runnable> taskQueue = new AwesomeQueue<Runnable>();
    private volatile boolean isRunning = true;

    public ThreadPool(int nThreads) {
        for (int i = 0; i < nThreads; ++i) {
            new Thread(
                    () -> {
                        while (isRunning) {
                            taskQueue.hasItems();
                            Runnable nextTask = taskQueue.dequeue();
                            if (nextTask != null) {
                                nextTask.run();
                            }
                        }
                    })
                    .start();
        }
    }

    public void execute(Runnable command) {
        if (isRunning) {
            taskQueue.enqueue(command);
        }
    }
}