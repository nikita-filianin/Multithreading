package org.example.fox;

import org.example.util.MultiplierAlgo;
import org.example.results.Result;

public class FoxAlgorithm implements MultiplierAlgo {
    private int[][] first;
    private int[][] second;
    private int threadsCount;
    private Result result;

    public FoxAlgorithm(int[][] first, int[][] second, int threadsCount) {

        this.first = first;
        this.second = second;
        this.result = new Result(first.length, second[0].length);

        if (threadsCount > first.length * second[0].length / 4) {
            this.threadsCount = first.length * second[0].length / 4;
        } else if (threadsCount < 1) {
            this.threadsCount = 1;
        } else {
            this.threadsCount = threadsCount;
        }
    }

    @Override
    public void multiply() {
        int step = (int) Math.ceil(1.0 * first.length / (int) Math.sqrt(threadsCount));

        FoxAlgoThread[] threads = new FoxAlgoThread[threadsCount];
        int index = 0;

        for (int i = 0; i < first.length; i += step) {
            for (int j = 0; j < second[0].length; j += step) {
                threads[index++] = new FoxAlgoThread(first, second, i, j, step, result);
            }
        }

        for (int i = 0; i < index; i++) {
            threads[i].start();
        }

        for (int i = 0; i < index; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {

            }
        }
    }

    public void resetResult() {
        result.reset();
    }

    @Override
    public int[][] getResult() {
        return this.result.getResult();
    }
}
