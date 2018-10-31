package edu.indiana.sice.spidal.apps;

import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.io.LittleEndianDataOutputStream;

import java.io.*;

public class DNADataConvertByPercentage {

    private static final int BUF_SIZE = 819200;
    private static String INPUT_MATRIX;
    private static String OUTPUT_MATRIX;
    private static String WEIGHT_MATRIX;
    private static double PERCENTAGE_THRESHOLD = 0.99999;
    private static int[] histogram = new int[Short.MAX_VALUE + 1];

    private static void run(short cuttingPoint) throws IOException {
        LittleEndianDataInputStream littleEndianDataInputStream = new LittleEndianDataInputStream(
                new BufferedInputStream(new FileInputStream(new File(INPUT_MATRIX)), BUF_SIZE)
        );
        LittleEndianDataOutputStream dataOutputStream = new LittleEndianDataOutputStream(
                new BufferedOutputStream(new FileOutputStream(new File(OUTPUT_MATRIX)), BUF_SIZE)
        );
        LittleEndianDataOutputStream weightOutputStream = new LittleEndianDataOutputStream(
                new BufferedOutputStream(new FileOutputStream(new File(WEIGHT_MATRIX)), BUF_SIZE)
        );

        while (littleEndianDataInputStream.available() > 0) {
            short value = littleEndianDataInputStream.readShort();
            if (value <= cuttingPoint) {
                dataOutputStream.writeShort(value);
                weightOutputStream.writeShort((short) (0.998 * Short.MAX_VALUE));
            } else {
                dataOutputStream.writeShort(0);
                weightOutputStream.writeShort(0);
            }
        }

        weightOutputStream.close();
        dataOutputStream.close();
        littleEndianDataInputStream.close();
    }

    private static short lookupCuttingPoint() {
        long totalCounter = 0;
        for (int i = 0; i < histogram.length; i++) {
            totalCounter += histogram[i];
        }
        long targetCounter = (long) (totalCounter * PERCENTAGE_THRESHOLD);
        long localCounter = 0;
        for (int i = 0; i < histogram.length; i++) {
            localCounter += histogram[i];
            if (localCounter >= targetCounter) {
                return (short) i;
            }
        }
        return -1;
    }

    private static void computeHistogram() throws IOException {
        long counter = 0;
        LittleEndianDataInputStream littleEndianDataInputStream =
                new LittleEndianDataInputStream(
                        new BufferedInputStream(new FileInputStream(new File(INPUT_MATRIX)), BUF_SIZE)
                );
        while (littleEndianDataInputStream.available() > 0) {
            short value = littleEndianDataInputStream.readShort();
            histogram[value]++;
            counter++;
            if (counter % 1_000_000 == 0)
                System.out.println(counter);
        }
        littleEndianDataInputStream.close();
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 4) return;

        INPUT_MATRIX = args[0];
        OUTPUT_MATRIX = args[1];
        WEIGHT_MATRIX = args[2];
        PERCENTAGE_THRESHOLD = Double.parseDouble(args[3]);

        computeHistogram();
        short cuttingPoint = lookupCuttingPoint();
        run(cuttingPoint);
    }
}
