package edu.indiana.sice.spidal.apps;

import edu.indiana.sice.dscspidal.mpicommonio.SparseMatrix;
import edu.indiana.sice.dscspidal.mpicommonio.SparseMatrixFile;
import mpi.MPI;
import mpi.MPIException;
import mpi.Status;

import java.io.*;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MPIScoreMatrixGenerator {

    private static class Link {
        int start;
        int end;
        double score;

        Link(String rowLine) {
            String[] arr = rowLine.split("\t", 3);
            start = Integer.parseInt(arr[0]);
            end = Integer.parseInt(arr[1]);
            score = Double.parseDouble(arr[2]);
        }

        @Override
        public String toString() {
            return "Link{" +
                    "start=" + start +
                    ", end=" + end +
                    ", score=" + score +
                    '}';
        }
    }

    private static int worldProcCount = 0;
    private static int worldProcRank = 0;
    private static int MASTER = 0;
    private static String source;
    private static String outputDir;
    private static boolean dumpCSV;

    private static long nLines = 0;
    private static long rowStart;
    private static long rowEnd;


    private static Map<Integer, Integer> indexMap;

    private static void allPrintln(final String msg) {
        try {
            MPI.COMM_WORLD.barrier();
            for (int i = 0; i < worldProcCount; i++) {
                MPI.COMM_WORLD.barrier();
                if (worldProcRank == i)
                    System.out.println(String.format("Rank[%02d/%02d]:%s", worldProcRank, worldProcCount, msg));
            }
            MPI.COMM_WORLD.barrier();
        } catch (MPIException e) {
            e.printStackTrace();
        }
    }

    private static void setup(String[] args) throws MPIException {
        worldProcCount = MPI.COMM_WORLD.getSize();
        worldProcRank = MPI.COMM_WORLD.getRank();
        source = args[0];
        outputDir = args[1];
        if (args.length == 3 && args[2].equals("-d")) {
            dumpCSV = true;
        }
    }

    private static void partitionByRank() {
        long rowsPerRank = (long) Math.ceil((double) nLines / worldProcCount);
        rowStart = rowsPerRank * worldProcRank + 1;
        rowEnd = rowStart + rowsPerRank - 1;
        if (rowEnd > nLines)
            rowEnd = nLines;
    }

    private static void countLines() throws MPIException {
        MPI.COMM_WORLD.barrier();
        try {
            long[] buf = new long[1];
            if (worldProcRank == MASTER) {
                buf[0] = Files.lines(Paths.get(source)).count();
            }
            MPI.COMM_WORLD.bcast(buf, 1, MPI.LONG, MASTER);
            nLines = buf[0];
        } catch (IOException e) {
            e.printStackTrace();
        }
        MPI.COMM_WORLD.barrier();
    }

    private static int allReduceMax(int value) throws MPIException {
        IntBuffer intBuffer = MPI.newIntBuffer(1);
        intBuffer.put(0, value);
        MPI.COMM_WORLD.allReduce(intBuffer, 1, MPI.INT, MPI.MAX);
        return intBuffer.get(0);
    }

    private static void buildIndices() throws MPIException, IOException {
        Set<Integer> integerSet = new HashSet<>();
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(source))) {
            for (long i = 1; i <= rowEnd; i++) {
                if (i < rowStart) {
                    br.readLine();
                } else {
                    line = br.readLine();
                    Link l = new Link(line);
                    integerSet.add(l.start);
                    integerSet.add(l.end);
                }
            }
        }
        int[] indexArray = integerSet.stream().mapToInt(Integer::intValue).toArray();
        allPrintln("Indices.length=" + indexArray.length);

        // Reduce index arrays to MASTER rank
        int BUF_SIZE = allReduceMax(indexArray.length);
        MPI.COMM_WORLD.barrier();
        if (worldProcRank == MASTER) {
            int[] bufArray = new int[BUF_SIZE];
            for (int i = 0; i < worldProcCount; i++) {
                if (i != MASTER) {
                    Status status = MPI.COMM_WORLD.recv(bufArray, BUF_SIZE, MPI.INT, i, i);
                    int realSize = status.getCount(MPI.INT);
                    System.out.println(String.format("Rank[%d]:%d size received", MASTER, realSize));
                    for (int j = 0; j < realSize; j++) {
                        integerSet.add(bufArray[j]);
                    }
                }
            }
        } else {
            MPI.COMM_WORLD.send(indexArray, indexArray.length, MPI.INT, MASTER, worldProcRank);
            System.out.println(String.format("Rank[%d]:%d size sent", worldProcRank, indexArray.length));
        }
        MPI.COMM_WORLD.barrier();

        // Broadcast indices to all slaves
        if (worldProcRank == MASTER) {
            indexArray = integerSet.stream().mapToInt(Integer::intValue).toArray();
        }
        MPI.COMM_WORLD.barrier();
        BUF_SIZE = allReduceMax(indexArray.length);
        if (worldProcRank != MASTER) {
            indexArray = new int[BUF_SIZE];
        }
        MPI.COMM_WORLD.bcast(indexArray, indexArray.length, MPI.INT, MASTER);
        allPrintln("Indices.length=" + indexArray.length);

        // Build local indices
        indexMap = new HashMap<>(indexArray.length);
        for (int i = 0; i < indexArray.length; i++) {
            indexMap.put(indexArray[i], i);
        }

        allPrintln("IndexMap.size=" + indexMap.size());
    }

    private static void dumpPartitionFiles(String input, String outputDir) throws IOException, MPIException {
        MPI.COMM_WORLD.barrier();
        assert indexMap != null;

        final String matrixPath = String.format("%s/%d-matrix-file-part-%04d.bin", outputDir, indexMap.size(), worldProcRank);
        allPrintln("Matrix partition:" + matrixPath);

        SparseMatrix sparseMatrix = new SparseMatrix(indexMap.size(), indexMap.size());
        long counter = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(source))) {
            for (long i = 1; i <= rowEnd; i++) {
                if (i < rowStart) {
                    br.readLine();
                } else {
                    String line = br.readLine();
                    Link link = new Link(line);

                    int rowIdx = indexMap.get(link.start);
                    int colIdx = indexMap.get(link.end);

                    assert rowIdx != -1 && colIdx != -1;

                    // Symmetric matrix
                    sparseMatrix.set(rowIdx, colIdx, link.score);
                    sparseMatrix.set(colIdx, rowIdx, link.score);

                    if (counter % 1_000_000 == 0) {
                        System.out.println('.');
                    }
                    counter++;
                }
            }
        }

        allPrintln("Local sparse matrix done");
        allPrintln("Start dumping:" + matrixPath);
        File matrixFile = new File(matrixPath);
        SparseMatrixFile.dumpToFile(sparseMatrix, matrixFile);
        allPrintln("Partition done:" + matrixPath);
    }

    private static void dumpToCSV() throws IOException, MPIException {
        MPI.COMM_WORLD.barrier();
        final String matrixPath = String.format("%s/%d-matrix-file-part-%04d.bin", outputDir, indexMap.size(), worldProcRank);

        File matrixFile = new File(matrixPath);

        SparseMatrix sparseMatrix = SparseMatrixFile.loadIntoMemory(matrixFile, 0, indexMap.size() - 1, indexMap.size());
        FileWriter fileWriter = new FileWriter(matrixPath + ".csv");
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        for (int i = 0; i < indexMap.size(); i++) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int j = 0; j < indexMap.size(); j++) {
                double v = sparseMatrix.get(i, j);
                stringBuilder.append(v);
                if (j != indexMap.size() - 1)
                    stringBuilder.append(',');
            }
            stringBuilder.append('\n');
            bufferedWriter.write(stringBuilder.toString());
        }

        bufferedWriter.close();
        fileWriter.close();
    }

    public static void main(String[] args) throws MPIException, IOException {
        MPI.Init(args);
        setup(args);

        countLines();
        partitionByRank();
        allPrintln(String.format("Total # of lines:%d, rowStart=%d, rowEnd=%d", nLines, rowStart, rowEnd));
        buildIndices();

        dumpPartitionFiles(source, outputDir);

        if (dumpCSV)
            dumpToCSV();

        MPI.Finalize();
    }
}
