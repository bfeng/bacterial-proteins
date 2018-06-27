package edu.indiana.sice.spidal.apps;

import mpi.MPIException;
import org.apache.commons.cli.*;

import java.io.IOException;
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
                .optionalArg(true)
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
            int points = Integer.parseInt(commandLine.getOptionValue(POINTS_OPTION));
            int dimension = Integer.parseInt(commandLine.getOptionValue(DIM_OPTION));
            String output = commandLine.getOptionValue(OUTPUT_OPTION);
            String outputCsv = null;
            if (commandLine.hasOption(OUTPUT_CSV)) {
                outputCsv = commandLine.getOptionValue(OUTPUT_CSV);
            }

            System.out.println("Running the distance calculation:" + Arrays.toString(args));
            ParallelOps.setupParallelism(args);
            DistanceCalculation.run(input, output, points, dimension, outputCsv);
            ParallelOps.tearDownParallelism();
        } catch (MPIException | IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("bp", options);
        }
    }
}
