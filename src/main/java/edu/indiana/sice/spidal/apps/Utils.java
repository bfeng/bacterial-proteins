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

    static double calculateDistance(double x[], double y[], int a, int b, int dimension) {
        if (a == b) return 0;

        double min = 0.501;
        double max = 13.284;
        double score = x[b];
        //        printMessage(String.format("a:%d, b:%d, score_a:%f, score_b:%f, distance:%f", a, b, x[b], y[a], distance));
//        return score > min ? (max - min) / (score - min) : 1;
        return score > min ? min*0.8 / score : 1;
//        double sum = 0;
//        for (int i = 0; i < dimension; i++) {
//            sum += (x[i] - y[i]) * (x[i] - y[i]);
//        }
//
//        return Math.sqrt(sum);
    }
}
