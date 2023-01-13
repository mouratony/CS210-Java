import stdlib.StdOut;
import stdlib.StdRandom;
import stdlib.StdStats;

public class PercolationStats {
    // number of trials.
    private int m;
    // percolation thresholds for the m trials.
    private double[] x;

    // Performs m independent experiments on an n x n percolation system.
    public PercolationStats(int n, int m) {
        // Throw an error message if n <= 0 and m <= 0.
        if (n <= 0 || m <= 0) {
            throw new IllegalArgumentException("Illegal n or m");
        }
        // Initialize instance variables.

        // Set this.m = m
        this.m = m;
        // Create a 1D array of doubles of size m.
        this.x = new double[m];
        // Initialize i to 0 for while loop.
        int i = 0;
        // Create a double variable of value n as a double.
        double d = n;
        // Create a loop which runs for m trials.
        while (i < m) {
            // at each trial,
            // create an UF percolation system of size n x n.
            UFPercolation perc = new UFPercolation(n);
            // Until the system percolates,
            while (!perc.percolates()) {
                // Randomly open a site (r, s) in the system if it is not open.
                int r = StdRandom.uniform(0, n);
                int s = StdRandom.uniform(0, n);
                if (!perc.isOpen(r, s)) {
                    perc.open(r, s);
                }
            }
            // After the system percolates at the current trial,
            // compute the percolation threshold as the fraction of sites opened.
            double p = perc.numberOfOpenSites() / (d * d);
            // and add it to the array x.
            this.x[i] = p;
            // go to the next trial.
            i++;
        }
    }

    // Returns sample mean of percolation threshold.
    public double mean() {
        // Use StdStats.mean() over our 1D array x to compute the mean.
        return StdStats.mean(x);
    }

    // Returns sample standard deviation of percolation threshold.
    public double stddev() {
        // Use StdStats.stddev() over our 1D array x to compute the standard deviation.
        return StdStats.stddev(x);
    }


    // Returns low endpoint of the 95% confidence interval.
    public double confidenceLow() {
        // compute the low endpoint using the given formula.
        return mean() - ((1.96 * stddev()) / Math.sqrt(m));
    }

    // Returns high endpoint of the 95% confidence interval.
    public double confidenceHigh() {
        // compute the high endpoint using the given formula.
        return mean() + ((1.96 * stddev()) / Math.sqrt(m));
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int m = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(n, m);
        StdOut.printf("Percolation threshold for a %d x %d system:\n", n, n);
        StdOut.printf("  Mean                = %.3f\n", stats.mean());
        StdOut.printf("  Standard deviation  = %.3f\n", stats.stddev());
        StdOut.printf("  Confidence interval = [%.3f, %.3f]\n", stats.confidenceLow(),
                stats.confidenceHigh());
    }
}