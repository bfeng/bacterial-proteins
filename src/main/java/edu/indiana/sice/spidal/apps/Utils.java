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

    /**
     * @param value should be between 0 and max
     * @return
     */
    static short roundToShort(double value, double max) {
        return (short) Math.round((value / max) * Short.MAX_VALUE);
    }

    static short selectWeight(double value, short weight1, short weight2) {
        short weight;
        if (value >= 0 && value < 1.0) {
            weight = weight1;
        } else
            weight = weight2;
        return weight;
    }

    static double calculateDistance(double x[], int a, int b, double min, double max) {
        if (a == b) return 0;
        double score = x[b];
        if (score == 0) {
            return 1;
        } else {
//            return 1.0 - 0.8 * (score - min) / (max - min);
//            return 1.0 - (score - min) / (max - min);
//            return 0.8 * (1.0 - (score - min) / (max - min));
            return (1.0 / score - 1.0 / max) * min * max / (max - min);
        }
    }
}
