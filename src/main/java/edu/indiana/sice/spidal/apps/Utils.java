package edu.indiana.sice.spidal.apps;

public class Utils {
    public static void printAndThrowRuntimeException(RuntimeException e) {
        e.printStackTrace(System.out);
        throw e;
    }

    public static void printAndThrowRuntimeException(String message) {
        System.out.println(message);
        throw new RuntimeException(message);
    }

    static void printMessage(String msg) {
        if (ParallelOps.worldProcRank != 0) {
            return;
        }
        System.out.println(msg);
    }

    static double calculateDistance(double x[], int a, int b, double min, double max) {
        if (a == b) return 0;
        double score = x[b];
        if (score == 0) {
            return 1;
        } else {
            return 1 - 0.2 * (score - min) / (max - min);
        }
    }
}
