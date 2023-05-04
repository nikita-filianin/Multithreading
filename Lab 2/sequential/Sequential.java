package org.example.sequential;

import org.example.util.MultiplierAlgo;
import org.example.results.Result;

public class Sequential implements MultiplierAlgo {
    private int[][] first;
    private int[][] second;
    private Result result;

    public Sequential(int[][] first, int[][] second) {
        this.first = first;
        this.second = second;
        this.result = new Result(first.length, second[0].length);
    }

    public void multiply() {
        for (int i = 0; i < first.length; i++) {
            for (int j = 0; j < second[i].length; j++) {
                int sum = 0;
                for (int k = 0; k < second.length; k++) {
                    sum += first[i][k] * second[k][j];
                }
                result.setElement(i, j, sum);
            }
        }
    }

    @Override
    public int[][] getResult() {
        return result.getResult();
    }
}
