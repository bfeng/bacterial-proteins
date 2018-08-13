package edu.indiana.sice.spidal.apps;

import edu.indiana.sice.dscspidal.mpicommonio.SparseMatrix;
import edu.indiana.sice.dscspidal.mpicommonio.SparseMatrixFile;
import mpi.Intracomm;
import mpi.MPI;
import mpi.MPIException;
import org.apache.commons.io.FileUtils;

import java.io.IOException;

public class SparseDistanceMatrixGenerator {

    private static SparseMatrix distanceMatrix;

    private static Intracomm worldProcComm;
    private static int worldProcRank;
    private static int worldProcCount;

    private static void init() throws MPIException {
        worldProcComm = MPI.COMM_WORLD; //initializing MPI world communicator
        worldProcRank = worldProcComm.getRank();
        worldProcCount = worldProcComm.getSize();
    }

    private static void allPrintln(final String msg) throws MPIException {
        MPI.COMM_WORLD.barrier();
        for (int i = 0; i < worldProcCount; i++) {
            MPI.COMM_WORLD.barrier();
            if (worldProcRank == i)
                System.out.println(String.format("%02d/%02d:%s", worldProcRank, worldProcCount, msg));
        }
        MPI.COMM_WORLD.barrier();
    }

    private static void reportHeapSize() throws MPIException {
        long heapSize = Runtime.getRuntime().totalMemory();
        allPrintln("Heap size:" + FileUtils.byteCountToDisplaySize(heapSize));
    }

    private static void printDistanceMatrix(int dimension) {
        assert distanceMatrix != null;
        assert distanceMatrix.numCols() == dimension;
        assert distanceMatrix.numRows() == dimension;
    }

    public static void main(String[] args) throws MPIException, IOException {
        MPI.Init(args);

        init();

        allPrintln("Step 0: Report heap size");
        reportHeapSize();
        allPrintln("Step 0: Done");

        allPrintln("Step 1: Read the sparse score matrix");
        final String path = "../output/fusion2.15k_sampled-score-matrix-new.csv.matrix-file.bin";
        final int N = 9556;
        SparseMatrixFile matrixFile = new SparseMatrixFile(path, N, N);
        matrixFile.open();

        distanceMatrix = matrixFile.toSparseMatrix();

        matrixFile.close();
        MPI.COMM_WORLD.barrier();
        allPrintln("Step 1: Done");

        allPrintln("Step X: Report heap size");
        reportHeapSize();
        allPrintln("Step X: Done");

        MPI.Finalize();
    }
}
