package edu.indiana.sice.spidal.apps;

import com.google.common.io.LittleEndianDataInputStream;

import java.io.*;

public class DNADataConvert {

    private static String INPUT_MATRIX;
    private static String OUTPUT_MATRIX;

    private static final double threshold = 0.8;

    private static void run() throws IOException {
        InputStream inputStream = new FileInputStream(new File(INPUT_MATRIX));
        LittleEndianDataInputStream littleEndianDataInputStream = new LittleEndianDataInputStream(inputStream);
        DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(new File(OUTPUT_MATRIX)));

        while (littleEndianDataInputStream.available() > 0) {
            short value = littleEndianDataInputStream.readShort();
            double out = (double) value / Short.MAX_VALUE;
            if (out <= threshold)
                dataOutputStream.writeShort(value);
            else
                dataOutputStream.writeShort(Short.MAX_VALUE);
        }

        dataOutputStream.close();
        littleEndianDataInputStream.close();
        inputStream.close();
    }

    public static void main(String[] args) {
        INPUT_MATRIX = args[0];
        OUTPUT_MATRIX = args[1];
    }
}
