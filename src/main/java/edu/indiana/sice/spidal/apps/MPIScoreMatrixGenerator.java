package edu.indiana.sice.spidal.apps;

import mpi.MPI;
import mpi.MPIException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
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

    private static long nLines = 0;
    private static long rowStart;
    private static long rowEnd;

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
    }

    public static void main(String[] args) throws MPIException, IOException {
        MPI.Init(args);
        setup(args);

        countLines();
        partitionByRank();
        allPrintln(String.format("Total # of lines:%d, rowStart=%d, rowEnd=%d", nLines, rowStart, rowEnd));
        buildIndices();

        MPI.Finalize();
    }
}
