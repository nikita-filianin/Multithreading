import mpi.MPI;
import mpi.Request;

public class AsyncStringMatrixMultiplicationMpi {
    public static void main(String[] args) {

        MPI.Init(args);

        final int NRA = 1500, NCA = 1500, NCB = 1500;

        final int MASTER_RANK = 0;

        final int numberProcessors = MPI.COMM_WORLD.Size();
        final int rank = MPI.COMM_WORLD.Rank();

        final int numberWorkers = numberProcessors - 1;

        if (rank == MASTER_RANK) {
            final int[][] matrixA = Matrix.generateMatrix(NRA, NCA, 0, 10);
            final int[][] matrixB = Matrix.generateMatrix(NCA, NCB, 0, 10);
            final int[][] matrixC = new int[NRA][NCB];
            final int[] rows = new int[numberWorkers];
            final int[] offsets = new int[numberWorkers];

            System.out.println(" ");
            System.out.println("Matrix A:");
            Matrix.showMatrix(matrixA);
            System.out.println(" ");
            System.out.println("Matrix B:");
            Matrix.showMatrix(matrixB);

            long start = System.currentTimeMillis();

            int averageRow = NRA / numberWorkers;
            int extra = NRA % numberWorkers;
            int offset = 0;

            for (int i = 1; i < numberWorkers + 1; i++) {
                int rowsPerProcess = averageRow + (extra > 0 ? 1 : 0);

                rows[i - 1] = rowsPerProcess;
                offsets[i - 1] = offset;

                if (rowsPerProcess == 0)
                    break;

                MPI.COMM_WORLD.Isend(new int[]{rowsPerProcess}, 0, 1, MPI.INT, i, Tags.FIRST_ROWS_NUM.getValue());
                MPI.COMM_WORLD.Isend(matrixA, offset, rowsPerProcess, MPI.OBJECT, i, Tags.FIRST_MATRIX.getValue());
                MPI.COMM_WORLD.Isend(matrixB, 0, NCA, MPI.OBJECT, i, Tags.SECOND_MATRIX.getValue());

                extra--;
                offset += rowsPerProcess;
            }

            Request[] resRequests = new Request[numberWorkers];

            for (int i = 1; i < numberWorkers + 1; i++) {
                Request resRequest = MPI.COMM_WORLD.Irecv(matrixC, offsets[i - 1], rows[i - 1],
                        MPI.OBJECT, i, Tags.RESULT.getValue());
                resRequests[i - 1] = resRequest;
            }

            Request.Waitall(resRequests);

            long finish = System.currentTimeMillis();
            long elapsed = finish - start;

            System.out.println(" ");
            System.out.println("Matrix C (Multiplied):");
            Matrix.showMatrix(matrixC);
            System.out.println(" ");

            System.out.println("Time: " + (1.0 * elapsed) + " sec.");
        } else {
            int[] rowsPerProcess = new int[1];

            Request rowsRequest = MPI.COMM_WORLD.Irecv(rowsPerProcess, 0, 1,
                    MPI.INT, MASTER_RANK, Tags.FIRST_ROWS_NUM.getValue());

            rowsRequest.Wait();

            int[][] firstPart = new int[rowsPerProcess[0]][NCA];
            int[][] second = new int[NCA][NCB];
            int[][] res = new int[rowsPerProcess[0]][NCB];

            Request firstRequest = MPI.COMM_WORLD.Irecv(firstPart, 0, rowsPerProcess[0],
                    MPI.OBJECT, MASTER_RANK, Tags.FIRST_MATRIX.getValue());
            Request secondRequest = MPI.COMM_WORLD.Irecv(second, 0, NCA,
                    MPI.OBJECT, MASTER_RANK, Tags.SECOND_MATRIX.getValue());

            firstRequest.Wait();
            secondRequest.Wait();

            for (int i = 0; i < firstPart.length; i++) {
                for (int j = 0; j < second[0].length; j++) {
                    int sum = 0;
                    for (int k = 0; k < second.length; k++) {
                        sum += firstPart[i][k] * second[k][j];
                    }
                    res[i][j] = sum;
                }
            }

            MPI.COMM_WORLD.Isend(res, 0, res.length, MPI.OBJECT, MASTER_RANK, Tags.RESULT.getValue());
        }

        MPI.Finalize();
    }
}
