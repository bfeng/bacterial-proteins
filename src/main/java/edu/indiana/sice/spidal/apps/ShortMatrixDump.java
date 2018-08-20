package edu.indiana.sice.spidal.apps;

import com.google.common.io.LittleEndianDataInputStream;

import java.io.*;

public class ShortMatrixDump {

    private static String INPUT_MATRIX;
    private static String OUTPUT_CSV;
    private static int COLUMNS;

    private static void dumpLittleEndian() throws IOException {
        InputStream inputStream = new FileInputStream(new File(INPUT_MATRIX));
        LittleEndianDataInputStream littleEndianDataInputStream = new LittleEndianDataInputStream(inputStream);
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(OUTPUT_CSV));

        StringBuilder line = new StringBuilder();
        int counter = 1;
        while (littleEndianDataInputStream.available() > 0) {
            short value = littleEndianDataInputStream.readShort();
            double out = (double) value / Short.MAX_VALUE;
            line.append(out);
            if (counter < COLUMNS) {
                line.append(",");
                counter++;
            } else {
                bufferedWriter.write(line.toString());
                bufferedWriter.newLine();
                line.delete(0, line.length());
                counter = 1;
            }
        }

        bufferedWriter.close();
        littleEndianDataInputStream.close();
        inputStream.close();
    }

    private static void dumpBigEndian() throws IOException {
        InputStream inputStream = new FileInputStream(new File(INPUT_MATRIX));
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(OUTPUT_CSV));

        StringBuilder line = new StringBuilder();
        int counter = 1;
        int lineCounter = 1;
        while (dataInputStream.available() > 0) {
            short value = dataInputStream.readShort();
            double out = (double) value / Short.MAX_VALUE;
            line.append(out);
            if (counter < COLUMNS) {
                line.append(",");
                counter++;
            } else {
                bufferedWriter.write(line.toString());
                bufferedWriter.newLine();
                line.delete(0, line.length());
                counter = 1;
                if (lineCounter++ % 500 == 0)
                    System.out.println(lineCounter);
            }
        }

        bufferedWriter.close();
        dataInputStream.close();
        inputStream.close();
    }

    public static void main(String[] args) {
        try {
            if (args.length == 4) {
                INPUT_MATRIX = args[0].trim();
                OUTPUT_CSV = args[1].trim();
                COLUMNS = Integer.parseInt(args[2]);
                boolean isBigEndian = Boolean.parseBoolean(args[3]);
                System.out.println("Start dumping...");
                if (isBigEndian) {
                    dumpBigEndian();
                } else {
                    dumpLittleEndian();
                }
                System.out.println("End dumping...");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
