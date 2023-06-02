import mpi.MPI;

public class StringMatrixMultiplicationMpi {
    public static void main(String[] args) {
        MPI.Init(args);

        final int NRA = 2000, NCA = 2000, NCB = 2000;

        final int MASTER_RANK = 0;

        final int numberProcessors = MPI.COMM_WORLD.Size();
        final int rank = MPI.COMM_WORLD.Rank();

        final int numberWorkers = numberProcessors - 1;

        if (rank == MASTER_RANK) {
            final int[][] matrixA = Matrix.generateMatrix(NRA, NCA, 1, 10);
            final int[][] matrixB = Matrix.generateMatrix(NCA, NCB, 1, 10);
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

                MPI.COMM_WORLD.Send(new int[]{rowsPerProcess}, 0, 1, MPI.INT,
                        i, Tags.FIRST_ROWS_NUM.getValue());
                MPI.COMM_WORLD.Send(matrixA, offset, rowsPerProcess, MPI.OBJECT, i, Tags.FIRST_MATRIX.getValue());
                MPI.COMM_WORLD.Send(matrixB, 0, NCA, MPI.OBJECT, i, Tags.SECOND_MATRIX.getValue());

                extra--;
                offset += rowsPerProcess;
            }

            for (int i = 1; i < numberWorkers + 1; i++) {
                MPI.COMM_WORLD.Recv(matrixC, offsets[i - 1], rows[i - 1], MPI.OBJECT, i, Tags.RESULT.getValue());
            }

            long finish = System.currentTimeMillis();
            long elapsed = finish - start;

            System.out.println(" ");
            System.out.println("Matrix C (Multiplied):");
            Matrix.showMatrix(matrixC);
            System.out.println(" ");

            System.out.println("Time: " + (1.0 * elapsed / 1000) + " sec.");
        } else {
            int[] rowsPerProcess = new int[1];

            MPI.COMM_WORLD.Recv(rowsPerProcess, 0, 1, MPI.INT, MASTER_RANK, Tags.FIRST_ROWS_NUM.getValue());

            int[][] firstPart = new int[rowsPerProcess[0]][NCA];
            int[][] second = new int[NCA][NCB];
            int[][] res = new int[rowsPerProcess[0]][NCB];

            MPI.COMM_WORLD.Recv(firstPart, 0, rowsPerProcess[0], MPI.OBJECT, MASTER_RANK, Tags.FIRST_MATRIX.getValue());
            MPI.COMM_WORLD.Recv(second, 0, NCA, MPI.OBJECT, MASTER_RANK, Tags.SECOND_MATRIX.getValue());

            for (int i = 0; i < firstPart.length; i++) {
                for (int j = 0; j < second[0].length; j++) {
                    int sum = 0;
                    for (int k = 0; k < second.length; k++) {
                        sum += firstPart[i][k] * second[k][j];
                    }
                    res[i][j] = sum;
                }
            }

            MPI.COMM_WORLD.Send(res, 0, res.length, MPI.OBJECT, MASTER_RANK, Tags.RESULT.getValue());
        }

        MPI.Finalize();
    }
}
