package org.example.task2;

public class BallThread extends Thread {
    private Ball b;

    public BallThread(Ball ball) {
        b = ball;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i < 10000; i++) {
                b.move();
                if (b.isToRemove())        // remove
                    break;
                System.out.println("Thread name = "
                        + Thread.currentThread().getName());
                Thread.sleep(15);
            }
        } catch (InterruptedException ex) {
        }
    }
}
