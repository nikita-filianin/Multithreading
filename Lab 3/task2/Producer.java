package org.example.task2;

import java.util.Random;

public class Producer implements Runnable {
    private Drop drop;

    public Producer(Drop drop) {
        this.drop = drop;
    }

    public void run() {
        int[] importantInfo = new int[1000];
        for (int i = 0; i < importantInfo.length; i++) {
            importantInfo[i] = i + 1;
        }
        Random random = new Random();

        for (int i = 0; i < importantInfo.length; i++) {
            drop.put(importantInfo[i]);
            try {
                Thread.sleep(random.nextInt(1000));
            } catch (InterruptedException e) {
            }
        }
        drop.put(0);
    }
}
