package edu.indiana.sice.spidal.apps;

import mpi.MPIException;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import static edu.indiana.sice.spidal.apps.Utils.calculateDistance;
import static edu.indiana.sice.spidal.apps.Utils.roundToShort;

class DistanceCalculation {

    private static double[] calculateMinMaxNoneZero(double[][] points, final int dimension) throws MPIException {
        double min = Double.MAX_VALUE, max = Double.MIN_VALUE;
        for (int i = 0; i < ParallelOps.procRowCount; i++) {
            for (int j = 0; j < dimension; j++) {
                double score = points[ParallelOps.procRowStartOffset + i][j];
                if (score != 0) {
                    min = Math.min(min, score);
                    max = Math.max(max, score);
                }
            }
        }
        min = ParallelOps.allReduceMin(min);
        max = ParallelOps.allReduceMax(max);
        return new double[]{min, max};
    }

    static void run(final String inputFile, final String outputFile, final int numPoints, final int dimension, final String outputFileCsv) throws MPIException, IOException {
        Utils.printMessage("Starting with " + ParallelOps.worldProcsCount + "Processes");
        double newMean = 0;
        double newSd = 0.1;
        BufferedReader br = Files.newBufferedReader(Paths.get(inputFile));
        FileOutputStream fos = new FileOutputStream(outputFile);
        FileChannel fc = fos.getChannel();
        BufferedWriter csvWriter = null;
        if (outputFileCsv != null) {
            csvWriter = new BufferedWriter(new FileWriter(outputFileCsv, false));
        }
        ParallelOps.setParallelDecomposition(numPoints, dimension);
        double[][] points = new double[numPoints][dimension];

        String line = null;
        int count = 0;
        while ((line = br.readLine()) != null) {
            String splits[] = line.split(",");
            for (int j = 0; j < splits.length; j++) {
                points[count][j] = Double.valueOf(splits[j]);
            }
            count++;
        }

        // calculate min and max over all scores
        final double[] minMax = calculateMinMaxNoneZero(points, dimension);
        final double minScore = minMax[0];
        final double maxScore = minMax[1];
        Utils.printMessage(String.format("Min:%f, Max:%f", minScore, maxScore));

        //Calculate means and sd of features //TODO need to parallise
        Utils.printMessage("Start calculating mean and sd");

        double[] means = new double[dimension];
        double[] sd = new double[dimension];
        double max = Double.MIN_VALUE;
        double disMean = 0;
        double disSd = 0;
        for (int i = 0; i < numPoints; i++) {
            for (int j = 0; j < dimension; j++) {
                means[j] += points[i][j];
            }
        }
        for (int i = 0; i < dimension; i++) {
            means[i] = means[i] / numPoints;
        }
        for (int i = 0; i < numPoints; i++) {
            for (int j = 0; j < dimension; j++) {
                sd[j] += (points[i][j] - means[j]) * (points[i][j] - means[j]);
            }
        }
        for (int i = 0; i < dimension; i++) {
            sd[i] = Math.sqrt(sd[i] / numPoints);
        }
        Utils.printMessage("End calculating mean and sd");

        //Update value with new normalized values
//        Utils.printMessage("Start calculating normalized data");
//
//        for (int i = 0; i < numPoints; i++) {
//            for (int j = 0; j < dimension; j++) {
//                if (sd[j] == 0) continue;
//                points[i][j] = newMean + ((points[i][j] - means[j]) / sd[j]) * newSd;
//            }
//        }
//
//        Utils.printMessage("End calculating normalized data");

        double[][] localDistances = new double[ParallelOps.procRowCount][numPoints];
        for (int i = 0; i < ParallelOps.procRowCount; i++) {
            for (int j = 0; j < numPoints; j++) {
                double distance = calculateDistance(points[i + ParallelOps.procRowStartOffset], i + ParallelOps.procRowStartOffset, j, minScore, maxScore);
                localDistances[i][j] = distance;
                if (distance < Double.MAX_VALUE) disMean += distance;
            }
            if (i % 1000 == 0) Utils.printMessage("Distance calculation ......");
        }


        disMean = ParallelOps.allReduce(disMean) / (((double) numPoints) * numPoints);
        Utils.printMessage("Distance mean : " + disMean);

        for (int i = 0; i < ParallelOps.procRowCount; i++) {
            for (int j = 0; j < numPoints; j++) {
                disSd += (localDistances[i][j] - disMean) * (localDistances[i][j] - disMean);
            }
        }

        disSd = Math.sqrt(ParallelOps.allReduce(disSd) / (((double) numPoints) * numPoints));
        Utils.printMessage("Distance SD : " + disSd);


        Utils.printMessage("Replacing distance larger than 3*SD with 3*SD");
        for (int i = 0; i < ParallelOps.procRowCount; i++) {
            for (int j = 0; j < numPoints; j++) {
                if (localDistances[i][j] > (disMean + 3 * disSd)) localDistances[i][j] = (disMean + 3 * disSd);
                if (localDistances[i][j] > max) {
                    max = localDistances[i][j];
                }
            }
        }
        max = ParallelOps.allReduceMax(max);
        Utils.printMessage("Done Replacing distance larger than 3*SD with 3*SD, Max is : " + max);

//        short[] row = new short[numPoints];
//        long filePosition = ((long) ParallelOps.procRowStartOffset) * numPoints * 2;
//        for (int i = 0; i < ParallelOps.procRowCount; i++) {
//            ByteBuffer byteBuffer = ByteBuffer.allocate(numPoints * 2);
//            byteBuffer.order(ByteOrder.BIG_ENDIAN);
//            for (int j = 0; j < numPoints; j++) {
//                row[j] = roundToShort(localDistances[i][j], max);
//            }
//            byteBuffer.clear();
//            byteBuffer.asShortBuffer().put(row);
//            if (i % 500 == 0) Utils.printMessage(".");
//            fc.write(byteBuffer, (filePosition + ((long) i) * numPoints * 2));
//            if (csvWriter != null) {
//                String rowdata = (Arrays.toString(row)).replace("[", "").replace("]", "");
//                csvWriter.write(rowdata + "\n");
//            }
//        }

        double[] row = new double[numPoints];
        long filePosition = ((long) ParallelOps.procRowStartOffset) * numPoints * 8;
        for (int i = 0; i < ParallelOps.procRowCount; i++) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(numPoints * 8);
            byteBuffer.order(ByteOrder.BIG_ENDIAN);
            System.arraycopy(localDistances[i], 0, row, 0, numPoints);
            byteBuffer.clear();
            byteBuffer.asDoubleBuffer().put(row);
            if (i % 500 == 0) Utils.printMessage(".");
            fc.write(byteBuffer, (filePosition + ((long) i) * numPoints * 8));
            if (csvWriter != null) {
                String rowdata = (Arrays.toString(row)).replace("[", "").replace("]", "");
                csvWriter.write(rowdata + "\n");
            }
        }

        fc.close();
        if (csvWriter != null) csvWriter.close();
        System.out.println("Process " + ParallelOps.worldProcRank + " Done");
    }
}

