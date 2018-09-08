package AwesomeQueue;

import java.util.LinkedList;

public class AwesomeQueue<E> {
    private LinkedList<E> list = new LinkedList<E>();
    public void enqueue(E item) {
        synchronized (this) {
            list.addLast(item);
            this.notify();
        }
    }
    public E dequeue() {
        synchronized (this) {
            return list.poll();
        }
    }
    public void hasItems() {
        synchronized (this) {
            while (list.isEmpty()) {
                try {
                    this.wait();
                } catch (InterruptedException ignored) {}

            }
        }
    }
}
