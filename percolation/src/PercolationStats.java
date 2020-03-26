import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;


public class PercolationStats {
    /**
     * designed for percolation simulation to get the threshold probability.
     *
     * @author HF Ye
     * @param perSize percolation size
     * @param trials simulation repeats
     * @param meanList  store the threshold probability of each simulation
     * @param size      the percolation size
     * @param repeation the simulation number
     */

    private static final double CONFDCONST = 1.96;
    private final int size; // percolation size
    private final int repeation; // simulation number
    private double[] meanList; // store the threshold probability of each simulation
    private double meanAvg; // average threshold probability
    private double expStdDev; // standard deviation of threshold probability

    //       private int count;
    //       private double runtime;

    /**
     * perform T independent experiments on an N-by-N grid.
     *
     * @author H.F. Ye
     */
    public PercolationStats(int perSize, int trials) {

        // perform T independent experiments on an N-by-N grid
        if (perSize <= 0 || trials <= 0) {
            throw new IllegalArgumentException(
                    "Invalid Size or Repeation arguments with " + perSize + ',' + trials);
        }

        size = perSize;
        repeation = trials;
        meanList = new double[trials];

        simulation();


    }

    private void simulation() {

        //           Stopwatch runTime = new Stopwatch();


        for (int it = 0; it < repeation; it++) {
            Percolation perTest = new Percolation(size);
            // StdRandom.setSeed((long) it);
            while (!perTest.percolates()) {
                int idx = StdRandom.uniform(1, size + 1);
                int idy = StdRandom.uniform(1, size + 1);
                perTest.open(idx, idy);
            }
            //               count = pTest.numberOfOpenSites();
            //               StdOut.println(count);
            meanList[it] = (double) perTest.numberOfOpenSites() / (double) (size * size);
            //               StdOut.println(mean[it]);

        }
        //           runtime = runTime.elapsedTime();


    }

    /**
     * test function.
     *
     * @author H.F. Ye
     */
    public static void main(String[] args) {

        //           StdOut.println("Site size and experiment time: "+args[0]+','+args[1]);
        //        final int size = Integer.parseInt(args[0]);
        //        final int trials = Integer.parseInt(args[1]);
        for (int idx = 0; idx < 10; idx++) {
            PercolationStats tmp = new PercolationStats(StdRandom.uniform(40, 200), 100);
            tmp.show();
        }


    }

    private void show() {
        //            StdOut.println("N = "+ (int) size +", T = "+(int) repeation);
        //            StdOut.println("count          = "+count);
        StdOut.println("mean                    = " + mean());
        StdOut.println("stddev                  = " + stddev());
        StdOut.println("95% confidence interval = ["
                               + confidenceLo() + ',' + confidenceHi() + ']');
        //            StdOut.println("confidenceHigh = "+confidenceHigh());
        //            StdOut.println("running time   = "+ runtime +"ms\n");

    }

    /**
     * sample mean of percolation threshold.
     *
     * @author H.F. Ye
     */
    public double mean() {
        meanAvg = StdStats.mean(meanList);
        return meanAvg;
    }

    /**
     * sample standard deviation of percolation threshold.
     *
     * @author H.F. Ye
     */
    public double stddev() {
        expStdDev = StdStats.stddev(meanList);
        return expStdDev;

    }

    /**
     * low endpoint of 95% confidence interval.
     *
     * @author H.F. Ye
     */
    public double confidenceLo() {
        return meanAvg - CONFDCONST * expStdDev / Math.sqrt((double) repeation);
    }

    /**
     * high endpoint of 95% confidence interval.
     *
     * @author H.F. Ye
     */
    public double confidenceHi() {
        return meanAvg + CONFDCONST * expStdDev / Math.sqrt((double) repeation);
    }

}
