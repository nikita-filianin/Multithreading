package org.example.task6;

public class CounterThread extends Thread {
    Counter counter;
    boolean increment;

    public CounterThread(Counter counter, boolean increment) {
        this.counter = counter;
        this.increment = increment;
    }

    @Override
    public void run() {
        if (increment) {
            for (int i = 0; i < 100000; i++) {
                counter.reentrantLockIncrement(); // to test
            }
        } else {
            for (int i = 0; i < 100000; i++) {
                counter.reentrantLockDecrement(); // to test
            }
        }
    }
}
