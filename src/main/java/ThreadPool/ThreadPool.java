package ThreadPool;

import AwesomeQueue.AwesomeQueue;

import java.util.concurrent.Executor;

public class ThreadPool implements Executor {
    private final AwesomeQueue<Runnable> workQueue = new AwesomeQueue<Runnable>();
    private volatile boolean isRunning = true;

    public ThreadPool(int nThreads) {
        for (int i = 0; i < nThreads; i++) {
            new Thread(new TaskWorker()).start();
        }
    }

    @Override
    public void execute(Runnable command) {
        synchronized (workQueue) {
            if (isRunning) {
//                System.out.println("execute,  " + Thread.currentThread().getName());
                workQueue.enqueue(command);
//                this.notify();
            }
        }
    }

    public void shutdown() {
        isRunning = false;
    }

    private final class TaskWorker implements Runnable {

        @Override
        public void run() {
//            System.out.println("run,  " + Thread.currentThread().getName());
            while (isRunning) {
                Runnable nextTask = null;
                synchronized (workQueue) {
//                    while (!workQueue.hasItems()) {
//                        try {
//                            this.wait();
//                        } catch (InterruptedException e) {e.printStackTrace();}
//
//                    }
                    nextTask = workQueue.dequeue();
                }
                if (nextTask != null) {
                    nextTask.run();
                }

            }
        }
    }
}