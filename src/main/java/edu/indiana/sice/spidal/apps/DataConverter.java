package edu.indiana.sice.spidal.apps;

import edu.indiana.sice.dscspidal.mpicommonio.MatrixFile;

import java.io.*;
import java.util.*;

class Row {
    long start;
    long end;
    double score;

    Row(String rowLine) {
        String[] arr = rowLine.split("\t", 3);
        start = Long.parseLong(arr[0]);
        end = Long.parseLong(arr[1]);
        score = Double.parseDouble(arr[2]);
    }

    @Override
    public String toString() {
        return "Row{" +
                "start=" + start +
                ", end=" + end +
                ", score=" + score +
                '}';
    }
}

public class DataConverter {

    private static List<Long> indices;
    private static String matrixPath;

    private static void buildIndices(String input) throws IOException {
        Set<Long> indexSet = new HashSet<>();

        FileReader fileReader = new FileReader(input);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            Row row = new Row(line);

            indexSet.add(row.start);
            indexSet.add(row.end);
        }
        indices = new ArrayList<>(indexSet);
        bufferedReader.close();
        fileReader.close();
    }

    private static void generateMatrixFile(String input, String output) throws IOException {
        assert indices != null;

        matrixPath = output + ".matrix-file.bin";
        FileReader fileReader = new FileReader(input);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        MatrixFile matrixFile = new MatrixFile(matrixPath, indices.size(), indices.size());
        matrixFile.open();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            Row row = new Row(line);

            int rowIdx = indices.indexOf(row.start);
            int colIdx = indices.indexOf(row.end);

            assert rowIdx != -1 && colIdx != -1;

            // Symmetric matrix
            matrixFile.writeTo(rowIdx, colIdx, row.score);
            matrixFile.writeTo(colIdx, rowIdx, row.score);
        }
        matrixFile.close();
        bufferedReader.close();
        fileReader.close();
    }

    private static void dumpToCSV(String output) throws IOException {
        assert matrixPath != null;

        MatrixFile matrixFile = new MatrixFile(matrixPath, indices.size(), indices.size());
        FileWriter fileWriter = new FileWriter(output);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        matrixFile.open();

        for (long i = 0; i < indices.size(); i++) {
            StringBuilder stringBuilder = new StringBuilder();
            for (long j = 0; j < indices.size(); j++) {
                double v = 0;
                try {
                    v = matrixFile.readFrom(i, j);
                } catch (EOFException ignored) {
                    // If out of range, the value should be 0.
                }
                stringBuilder.append(v);
                if (j != indices.size() - 1)
                    stringBuilder.append(',');
            }
            stringBuilder.append('\n');
            bufferedWriter.write(stringBuilder.toString());
        }

        matrixFile.close();
        matrixFile.delete();
        bufferedWriter.close();
        fileWriter.close();
    }

    private static void printUsage() {
        System.out.println(DataConverter.class.getCanonicalName() + " <input> <output>");
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
        System.out.println("Step 3: writing into a CSV file...");
        dumpToCSV(output);
        System.out.println("Step 3: done");

        long endTime = System.currentTimeMillis();

        System.out.printf("Time spent: %ds\n", Math.abs(endTime - startTime) / 1000);
    }
}
