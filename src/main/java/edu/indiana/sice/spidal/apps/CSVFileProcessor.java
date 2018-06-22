package edu.indiana.sice.spidal.apps;

import org.apache.commons.cli.*;

import java.util.Arrays;

public class CSVFileProcessor {

    private static final String INPUT_OPTION = "input";
    private final static String OUTPUT_OPTION = "output";
    private static final String DIM_OPTION = "dim";
    private static final String POINTS_OPTION = "points";
    private static final String OUTPUT_CSV = "output_csv";

    private static Options setupOptions() {
        final Option input = Option.builder(INPUT_OPTION)
                .required()
                .hasArg()
                .desc("Input file path")
                .build();
        final Option points = Option.builder(POINTS_OPTION)
                .required()
                .hasArg()
                .desc("Number of data points")
                .build();
        final Option dimension = Option.builder(DIM_OPTION)
                .required()
                .hasArg()
                .desc("Number of features")
                .build();
        final Option output = Option.builder(OUTPUT_OPTION)
                .required()
                .hasArg()
                .desc("Output file path")
                .build();
        final Option outputCsv = Option.builder(OUTPUT_CSV)
                .required()
                .hasArg()
                .desc("Output csv file path")
                .build();
        final Options options = new Options();
        options
                .addOption(input)
                .addOption(points)
                .addOption(dimension)
                .addOption(output)
                .addOption(outputCsv);
        return options;
    }

    public static void main(String[] args) {
        CommandLineParser parser = new DefaultParser();
        Options options = setupOptions();
        try {
            CommandLine commandLine = parser.parse(options, args);
            String input = commandLine.getOptionValue(INPUT_OPTION);
            String points = commandLine.getOptionValue(POINTS_OPTION);
            String dimension = commandLine.getOptionValue(DIM_OPTION);
            String output = commandLine.getOptionValue(OUTPUT_OPTION);
            String outputCsv = commandLine.getOptionValue(OUTPUT_CSV);
            String[] newArgs = {input, points, dimension, output, outputCsv};
            System.out.println("Running the distance calculation:" + Arrays.toString(newArgs));
            DistanceCalculation.run(newArgs);
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("bp", options);
        }
    }
}
