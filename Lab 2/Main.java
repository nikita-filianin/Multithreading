package org.example;

import org.example.fox.FoxAlgorithm;
import org.example.sequential.Sequential;
import org.example.stripe.StripeAlgorithm;
import org.example.util.MatrixUtil;
import org.example.util.MultiplierAlgo;

public class Main {
    public static void main(String[] args) {
//        testResult();
//        testMatrixSize();
        testThreadsCount();
    }

    public static void testResult() {
        int[][] first = MatrixUtil.generateMatrix(4, 4, 10, 99);
        int[][] second = MatrixUtil.generateMatrix(4, 4, 10, 99);

        System.out.println("First matrix: ");
        MatrixUtil.showMatrix(first);
        System.out.println("\nSecond matrix: ");
        MatrixUtil.showMatrix(second);

        // For Stripe Algo
        StripeAlgorithm stripeAlgorithm = new StripeAlgorithm(first, second, first.length);
        stripeAlgorithm.multiply();
        System.out.println("\nStripe Algorithm result: ");
        MatrixUtil.showMatrix(stripeAlgorithm.getResult());

        // For Fox Algo
        FoxAlgorithm foxAlgorithm = new FoxAlgorithm(first, second, 4);
        foxAlgorithm.multiply();
        System.out.println("\nFox Algorithm result: ");
        MatrixUtil.showMatrix(foxAlgorithm.getResult());

        // For Sequential Multiply
        Sequential sequential = new Sequential(first, second);
        sequential.multiply();
        System.out.println("\nSequential Multiply result: ");
        MatrixUtil.showMatrix(sequential.getResult());
    }

    public static void testThreadsCount() {
        int size = 1500;
        int min = 10;
        int max = 99;

        int[] threadsCounts = {4, 10, 18, 36, 64, 100};
        int[][] first = MatrixUtil.generateMatrix(size, size, min, max);
        int[][] second = MatrixUtil.generateMatrix(size, size, min, max);
        int experimentsCount = 4;

        for (int threadsCount : threadsCounts) {
            StripeAlgorithm stripeAlgorithm = new StripeAlgorithm(first, second, threadsCount);

            long experimentsTime = 0;
            for (int i = 0; i < experimentsCount; i++) {
                experimentsTime += getExpTime(stripeAlgorithm);
            }
            long average = experimentsTime / experimentsCount;

            System.out.println("Stripe (" + size + "x" + size + "): " +
                    "threads count - " + threadsCount + "; time - " + average + " ms;");
        }

        System.out.println();

        for (int threadsCount : threadsCounts) {
            FoxAlgorithm fox = new FoxAlgorithm(first, second, threadsCount);

            long experimentsTime = 0;
            for (int i = 0; i < experimentsCount; i++) {
                experimentsTime += getExpTime(fox);
                fox.resetResult();
            }
            long average = experimentsTime / experimentsCount;

            System.out.println("Fox (" + size + "x" + size + "): " +
                    "threads count - " + threadsCount + "; time - " + average + " ms;");
        }
        System.out.println();

        Sequential sequential = new Sequential(first, second);

        long experimentsTime = 0;
        for (int i = 0; i < experimentsCount; i++) {
            experimentsTime += getExpTime(sequential);
        }
        long average = experimentsTime / experimentsCount;

        System.out.println("Sequential (" + size + "x" + size + "): " +
                "threads count - 1; time - " + average + " ms;");
    }

    public static void testMatrixSize() {
        int min = 10;
        int max = 99;

        int[] sizes = {500, 1000, 1500, 2000};
        int experimentsCount = 4;
        int threadsCount = 16;

        for (int matrixSize : sizes) {
            int[][] first = MatrixUtil.generateMatrix(matrixSize, matrixSize, min, max);
            int[][] second = MatrixUtil.generateMatrix(matrixSize, matrixSize, min, max);

            StripeAlgorithm stripeAlgorithm = new StripeAlgorithm(first, second, threadsCount);

            long experimentsTime = 0;
            for (int i = 0; i < experimentsCount; i++) {
                experimentsTime += getExpTime(stripeAlgorithm);
            }
            long average = experimentsTime / experimentsCount;

            System.out.println("Stripe: matrix size - " + matrixSize + "x"
                    + matrixSize + "; time - " + average + " ms;");

            FoxAlgorithm foxAlgorithm = new FoxAlgorithm(first, second, threadsCount);

            experimentsTime = 0;
            for (int i = 0; i < experimentsCount; i++) {
                experimentsTime += getExpTime(foxAlgorithm);
                foxAlgorithm.resetResult();
            }
            average = experimentsTime / experimentsCount;

            System.out.println("Fox: matrix size - " + matrixSize + "x"
                    + matrixSize + "; time - " + average + " ms;");

            Sequential sequential = new Sequential(first, second);

            experimentsTime = 0;
            for (int i = 0; i < experimentsCount; i++) {
                experimentsTime += getExpTime(sequential);
            }
            average = experimentsTime / experimentsCount;

            System.out.println("Sequential: matrix size - " + matrixSize + "x"
                    + matrixSize + "; time - " + average + " ms;");
            System.out.println();
        }
    }

    public static long getExpTime(MultiplierAlgo algorithm) {
        long start = System.currentTimeMillis();
        algorithm.multiply();
        long finish = System.currentTimeMillis();
        return finish - start;
    }
}