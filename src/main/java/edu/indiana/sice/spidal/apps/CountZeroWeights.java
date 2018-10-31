package edu.indiana.sice.spidal.apps;

import com.google.common.io.LittleEndianDataInputStream;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class CountZeroWeights {

    private static final int BUF_SIZE = 819200;
    private long totalCounts;
    private long zeroCounts;

    private String weightMatrix;

    private CountZeroWeights(String weightMatrix) {
        this.weightMatrix = weightMatrix;
    }

    private void run() {
        try {
            LittleEndianDataInputStream dataInputStream = new LittleEndianDataInputStream(
                    new BufferedInputStream(
                            new FileInputStream(
                                    new File(weightMatrix)
                            ),
                            BUF_SIZE
                    )
            );

            while (dataInputStream.available() > 0) {
                short val = dataInputStream.readShort();
                if (val <= 0) {
                    zeroCounts++;
                }
                totalCounts++;
            }

            dataInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void report() {
        System.out.printf("Matrix: %s\n%d/%d\n", weightMatrix, zeroCounts, totalCounts);
    }

    public static void main(String[] args) {
        CountZeroWeights countZeroWeights = new CountZeroWeights(args[0]);

        countZeroWeights.run();

        countZeroWeights.report();
    }
}
