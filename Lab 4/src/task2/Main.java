package task2;

import task2.basic.BasicAlgorithm;
import task2.fox.FoxAlgorithm;
import task2.utils.Matrix;
import java.util.concurrent.ForkJoinPool;

public class Main {
  public static void main(String[] args) {
    simpleRun(false);
  }

  public static void simpleRun(boolean printMatrices) {
    int sizeAxis0 = 2000;
    int sizeAxis1 = 2000;

    Matrix A = new Matrix(sizeAxis0, sizeAxis1);
    Matrix B = new Matrix(sizeAxis0, sizeAxis1);

    A.generateRandomMatrix();
    B.generateRandomMatrix();

    int nThread = Runtime.getRuntime().availableProcessors();

    BasicAlgorithm ba = new BasicAlgorithm(A, B);

    long currTimeBasic = System.nanoTime();
    Matrix C = ba.multiply();
    currTimeBasic = System.nanoTime() - currTimeBasic;

    if (printMatrices) C.print();

    System.out.println("Time for Basic Algorithm: " + currTimeBasic / 1000000);

    long currTimeFox = System.nanoTime();
    ForkJoinPool forkJoinPool = new ForkJoinPool(nThread);
    C = forkJoinPool.invoke(new FoxAlgorithm(A, B, 6));
    currTimeFox = System.nanoTime() - currTimeFox;

    if (printMatrices) C.print();

    System.out.println("Time for Fox Algorithm: " + currTimeFox / 1000000);
    System.out.println();
    System.out.println("SpeedUp (timeBasic / timeFoxForkJoin): " + (double) currTimeBasic / currTimeFox);

    System.out.println("SpeedUp (timeFox / timeFoxForkJoin): " + (double) 22474 / (currTimeFox / 1000000));
  }
}
