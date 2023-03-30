package org.example.task3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class BounceFrame extends JFrame {
    private final BallCanvas canvas;
    public static final int WIDTH = 450;
    public static final int HEIGHT = 350;
    private static final Map<String, Color> colors = new HashMap<>(); // colors add
    private static final Map<Color, Integer> colorsPriority = new HashMap<>(); // priority parameter add

    private static void setColorsAndPriority() { ; // setting
        colors.put("Blue", Color.blue);
        colors.put("Red", Color.red);
        colorsPriority.put(Color.red, Thread.MAX_PRIORITY); // red = high priority
        colorsPriority.put(Color.blue, Thread.MIN_PRIORITY); // blue = low priority
    }

    public BounceFrame() {
        setColorsAndPriority();

        this.setSize(WIDTH, HEIGHT);
        this.setTitle("Bounce program");
        this.canvas = new BallCanvas();
        System.out.println("In Frame Thread name = "
                + Thread.currentThread().getName());

        Container content = this.getContentPane();
        content.add(this.canvas, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.lightGray);

        Label redLabel = new Label(); // labels for red balls
        redLabel.setText("Red");

        Label blueLabel = new Label(); // labels for blue balls
        blueLabel.setText("Blue");

        TextField redTextField = new TextField("0");
        TextField blueTextField = new TextField("0");

        JButton buttonStart = new JButton("Start");
        JButton buttonStop = new JButton("Stop");
        buttonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // create several balls at the same time
                int blueBallsCount = Integer.parseInt(blueTextField.getText());
                int redBallsCount = Integer.parseInt(redTextField.getText());
                BallThread[] threads = new BallThread[blueBallsCount + redBallsCount];

                for (int i = 0; i < redBallsCount; i++) {
                    Ball b = new Ball(Color.red, canvas);
                    canvas.add(b);

                    int priority = colorsPriority.get(Color.red);
                    BallThread thread = new BallThread(b);
                    thread.setPriority(priority);

                    threads[i] = thread;
                }

                for (int i = redBallsCount; i < blueBallsCount + redBallsCount; i++) {
                    Ball b = new Ball(Color.blue, canvas);
                    canvas.add(b);

                    int priority = colorsPriority.get(Color.blue);
                    BallThread thread = new BallThread(b);
                    thread.setPriority(priority);

                    threads[i] = thread;
                }

                // balls threads start
                for (int i = 0; i < threads.length; i++) {
                    threads[i].start();
                }
            }
        });
        buttonStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        buttonPanel.add(redLabel); //
        buttonPanel.add(redTextField); //
        buttonPanel.add(blueLabel); //
        buttonPanel.add(blueTextField); //

        buttonPanel.add(buttonStart);
        buttonPanel.add(buttonStop);
        content.add(buttonPanel, BorderLayout.SOUTH);
    }
}