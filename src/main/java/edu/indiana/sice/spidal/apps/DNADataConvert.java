package edu.indiana.sice.spidal.apps;

import com.google.common.io.LittleEndianDataInputStream;

import java.io.*;

public class DNADataConvert {

    private static String INPUT_MATRIX;
    private static String OUTPUT_MATRIX;
    private static String WEIGHT_MATRIX;
    private static double THRESHOLD = 0.99999;

    private static void run() throws IOException {
        InputStream inputStream = new FileInputStream(new File(INPUT_MATRIX));
        LittleEndianDataInputStream littleEndianDataInputStream = new LittleEndianDataInputStream(inputStream);
        DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new File(OUTPUT_MATRIX))));
        DataOutputStream weightOutputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new File(WEIGHT_MATRIX))));

        while (littleEndianDataInputStream.available() > 0) {
            short value = littleEndianDataInputStream.readShort();
            double out = (double) value / Short.MAX_VALUE;
            if (out <= THRESHOLD) {
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
        inputStream.close();
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 4) return;

        INPUT_MATRIX = args[0];
        OUTPUT_MATRIX = args[1];
        WEIGHT_MATRIX = args[2];
        THRESHOLD = Double.parseDouble(args[3]);

        run();
    }
}
