package edu.indiana.sice.spidal.apps;

import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.io.LittleEndianDataOutputStream;

import java.io.*;

public class DNADataConvert {

    private static final int BUF_SIZE = 819200;
    private static String INPUT_MATRIX;
    private static String OUTPUT_MATRIX;
    private static String WEIGHT_MATRIX;
    private static double THRESHOLD = 0.99999;

    private static void run(short max) throws IOException {
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
            double out = (double) value / max;
            if (out >= 0 && out <= THRESHOLD) {
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

    private static short countMaxDistance() throws IOException {
        short max = 0;
        long counter = 0;
        LittleEndianDataInputStream littleEndianDataInputStream =
                new LittleEndianDataInputStream(
                        new BufferedInputStream(new FileInputStream(new File(INPUT_MATRIX)), BUF_SIZE)
                );
        while (littleEndianDataInputStream.available() > 0) {
            short value = littleEndianDataInputStream.readShort();
            if (value > max) {
                max = value;
            }
            counter++;
            if (counter % 1_000_000 == 0)
                System.out.println(counter);
        }
        littleEndianDataInputStream.close();
        return max;
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 4) return;

        INPUT_MATRIX = args[0];
        OUTPUT_MATRIX = args[1];
        WEIGHT_MATRIX = args[2];
        THRESHOLD = Double.parseDouble(args[3]);

        short max = countMaxDistance();
        run(max);
    }
}
