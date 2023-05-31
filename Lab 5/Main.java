import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        testOneModel();
//        testModelsInParallel();
    }

    private static void testOneModel() {
        QueuingModel model = new QueuingModel();
        model.startImitation();
    }

    private static void testModelsInParallel() throws InterruptedException {
        List<Thread> threads = new ArrayList<>();
        int modelsCount = 5;
        double[] averageQueueSizes = new double[modelsCount];
        double[] rejectionProbabilities = new double[modelsCount];

        for (int i = 0; i < modelsCount; i++) {
            int threadIndex = i;
            Thread thread = new Thread(() -> {
                QueuingModel model = new QueuingModel();
                model.startImitation();

                averageQueueSizes[threadIndex] = model.getAverageQueueSize();
                rejectionProbabilities[threadIndex] = model.getRejectionProbability();

                synchronized (System.out) {
                    System.out.println("\nThread with index " + threadIndex + " has finished!");
                    System.out.println("Rejection Probability: " + model.getRejectionProbability());
                    System.out.println("Average Queue Size: " + model.getAverageQueueSize());
                }
            });

            thread.start();
            threads.add(thread);
        }

        for (Thread thread : threads) {
            thread.join();
        }

        double averageQueueSize = Arrays.stream(averageQueueSizes)
                .average()
                .orElseThrow();

        double averageRejectionProbability = Arrays.stream(rejectionProbabilities)
                .average()
                .orElseThrow();

        System.out.println("\n -------------------Final Results---------------------- \n");
        System.out.println("Average Queue Size: " + averageQueueSize);
        System.out.println("Average Rejection Probability: " + averageRejectionProbability);
    }
}