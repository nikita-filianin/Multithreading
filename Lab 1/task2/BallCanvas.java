package org.example.task2;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class BallCanvas extends JPanel {
    private ArrayList<Ball> balls = new ArrayList<>();

    private TextField removedBallsCounter;

    private static final int endX = 400;

//    private static final int endY = 300;

    public void add(Ball b) {
        this.balls.add(b);
    }

    public void addCounter() {   // setting up counter
        String text = removedBallsCounter.getText();
        int newNumber = Integer.parseInt(text) + 1;
        String newText = Integer.toString(newNumber);
        removedBallsCounter.setText(newText);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        for (Iterator<Ball> iterator = balls.iterator(); iterator.hasNext(); ) {
            Ball b = iterator.next();
            if (b.getX() >= endX) {    // removing rules
                b.setToRemove(true);
                iterator.remove();
                balls.remove(b);
                addCounter();
            } else {
                b.draw(g2);
            }
        }
    }

    public void setRemovedBallsCounter(TextField removedBallsCounter) {
        this.removedBallsCounter = removedBallsCounter;
    }
}
