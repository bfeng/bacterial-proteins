package edu.indiana.sice.spidal.apps;


import edu.indiana.sice.dscspidal.mpicommonio.SparseMatrixFile;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ScoreMatrixGenerator {

    private static class Link {
        long start;
        long end;
        double score;

        Link(String rowLine) {
            String[] arr = rowLine.split("\t", 3);
            start = Long.parseLong(arr[0]);
            end = Long.parseLong(arr[1]);
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

    private static List<Long> indices;
    private static String matrixPath;

    private static void buildIndices(String input) throws IOException {
        Set<Long> indexSet = new HashSet<>();

        FileReader fileReader = new FileReader(input);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            Link link = new Link(line);

            indexSet.add(link.start);
            indexSet.add(link.end);
        }
        indices = new ArrayList<>(indexSet);
        bufferedReader.close();
        fileReader.close();
    }

    private static void generateMatrixFile(String input, String output) throws IOException {
        assert indices != null;

        long startTime = System.currentTimeMillis();
        matrixPath = output + ".matrix-file.bin";
        FileReader fileReader = new FileReader(input);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        SparseMatrixFile matrixFile = new SparseMatrixFile(matrixPath, indices.size(), indices.size());
        matrixFile.open();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            Link link = new Link(line);

            int rowIdx = indices.indexOf(link.start);
            int colIdx = indices.indexOf(link.end);

            assert rowIdx != -1 && colIdx != -1;

            // Symmetric matrix
            matrixFile.set(rowIdx, colIdx, link.score);
            matrixFile.set(colIdx, rowIdx, link.score);

        }
        matrixFile.close();
        bufferedReader.close();
        fileReader.close();
        long endTime = System.currentTimeMillis();
        System.out.printf("Time spent: %ds\n", Math.abs(endTime - startTime) / 1000);
    }

    private static void dumpToCSV(String output) throws IOException {
        assert matrixPath != null;

        SparseMatrixFile matrixFile = new SparseMatrixFile(matrixPath, indices.size(), indices.size());
        FileWriter fileWriter = new FileWriter(output);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        matrixFile.open();

        for (long i = 0; i < indices.size(); i++) {
            StringBuilder stringBuilder = new StringBuilder();
            double[] row = matrixFile.getRow(i);
            assert row.length == indices.size();
            for (long j = 0; j < indices.size(); j++) {
                double v = row[(int) j];
                stringBuilder.append(v);
                if (j != indices.size() - 1)
                    stringBuilder.append(',');
            }
            stringBuilder.append('\n');
            bufferedWriter.write(stringBuilder.toString());
        }

        matrixFile.close();
//        matrixFile.delete();
        bufferedWriter.close();
        fileWriter.close();
    }

    private static void printUsage() {
        System.out.println(ScoreMatrixGenerator.class.getCanonicalName() + " <input> <output>");
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            printUsage();
            return;
        }
        String input = args[0];
        String output = args[1];

        long startTime = System.currentTimeMillis();

        System.out.println("Step 1: building indices...");
        buildIndices(input);
        System.out.println("Step 1: done");
        System.out.printf("Length of indices: %d\n", indices.size());
        System.out.println("Step 2: converting to a matrix...");
        generateMatrixFile(input, output);
        System.out.println("Step 2: done");
//        System.out.println("Step 3: writing into a CSV file...");
//        dumpToCSV(output);
//        System.out.println("Step 3: done");

        long endTime = System.currentTimeMillis();

        System.out.printf("Time spent: %ds\n", Math.abs(endTime - startTime) / 1000);
    }
}
