package AwesomeQueue;

import java.util.LinkedList;

public class AwesomeQueue<E> {
    private LinkedList<E> list = new LinkedList<E>();
    public void enqueue(E item) {
        synchronized (this) {
//            System.out.println("enq: " + Thread.currentThread().getName());
            list.addLast(item);
            this.notify();
        }
    }
    public E dequeue() {
        synchronized (this) {
//            System.out.println("deq: " + Thread.currentThread().getName());
            return list.poll();
        }
    }
    public void hasItems() {
        synchronized (this) {
//            System.out.println("has: " + Thread.currentThread().getName());
            while (list.isEmpty()) {
//                System.out.println("while: " + Thread.currentThread().getName());
                try {
//                    System.out.println("try b: " + Thread.currentThread().getName());
                    this.wait();
//                    System.out.println("try a: " + Thread.currentThread().getName());
                } catch (InterruptedException ignored) {}

            }
        }
    }
}

//todo: diff bet meth sync
//todo: wh hap if thr w't w
//todo: trim
