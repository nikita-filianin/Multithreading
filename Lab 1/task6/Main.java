package org.example.task6;

public class Main {
    public static void main(String[] args) {
        Counter counter = new Counter(1);
        CounterThread threadIncrement = new CounterThread(counter, true);
        CounterThread threadDecrement = new CounterThread(counter, false);

        threadIncrement.start();
        threadDecrement.start();

        try {
            threadIncrement.join();
            threadDecrement.join();
        } catch (InterruptedException e) {
        }

        System.out.println(counter.getCounter());
    }
}