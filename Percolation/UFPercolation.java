import dsa.WeightedQuickUnionUF;
import stdlib.In;
import stdlib.StdOut;

// An implementation of the Percolation API using the UF data structure.
public class UFPercolation implements Percolation {
    // Size of the percolation system.
    private int n;
    // Percolation system.
    private boolean[][] open;
    // Number of open sites.
    private int openSites;
    // UF representation of the percolation system.
    private WeightedQuickUnionUF uf;
    // A new UF representation of the percolation system used to solve the backwash problem.
    private WeightedQuickUnionUF ufForBackwash;

    // Constructs an n x n percolation system, with all sites blocked.
    public UFPercolation(int n) {
        // Throw an error message if n <= 0.
        if (n <= 0) {
            throw new IllegalArgumentException("Illegal n");
        }
        // Initialize instance variables.

        // Set this.n to n.
        this.n = n;
        // Create a percolation system of size n x n
        // the values by default is false which means all sites are blocked.
        open = new boolean[n][n];
        // Set the number of open sites to 0.
        openSites = 0;
        // Create an UF object with n * n + 2 sites.
        uf = new WeightedQuickUnionUF((n * n) + 2);
        // Create another UF object with n * n + 2 sites.
        ufForBackwash = new WeightedQuickUnionUF((n * n) + 2);
    }

    // Opens site (i, j) if it is not already open.
    public void open(int i, int j) {
        // Throw an index error message if i and j is out of bonds.
        if ((i < 0 || i > n - 1) || (j < 0 || j > n - 1)) {
            throw new IndexOutOfBoundsException("Illegal i or j");
        }
        // Check if site (i, j) is not open, if not, open the site and increment openSites by 1
        if (!open[i][j]) {
            open[i][j] = true;
            openSites++;
            // If the site is in the first row, connect it to the source.
            if (i == 0) {
                uf.union(encode(i, j), 0);
                ufForBackwash.union(encode(i, j), 0);
            }
            // If the site is in the last row, connect it to the sink.
            // But do not connect the ufForBackwash at the corresponding site
            // to the sink.
            if (i == n - 1) {
                uf.union(encode(i, j), n*n + 1);

            }
            // If any of the neighbors to the north, east, west, and south of site (i, j)
            // is open, connect site (i, j) with the uf site corresponding to that neighbor.
            if (i-1 >= 0 && open[i - 1][j]) {
                uf.union(encode(i, j), encode(i - 1, j));
                ufForBackwash.union(encode(i, j), encode(i - 1, j));
            }
            if (i+1 <= n-1 && open[i + 1][j]) {
                uf.union(encode(i, j), encode(i + 1, j));
                ufForBackwash.union(encode(i, j), encode(i + 1, j));
            }
            if (j-1 >= 0 && open[i][j - 1]) {
                uf.union(encode(i, j), encode(i, j - 1));
                ufForBackwash.union(encode(i, j), encode(i, j - 1));
            }
            if (j+1 <= n-1 && open[i][j + 1]) {
                uf.union(encode(i, j), encode(i, j + 1));
                ufForBackwash.union(encode(i, j), encode(i, j + 1));
            }
        }
    }

    // Returns true if site (i, j) is open, and false otherwise.
    public boolean isOpen(int i, int j) {
        // Throw an index error message if i and j is out of bonds.
        if ((i < 0 || i > n - 1) || (j < 0 || j > n - 1)) {
            throw new IndexOutOfBoundsException("Illegal i or j");
        }
        // It will return the boolean value which corresponds to the site (i, j).
        return open[i][j];
    }

    // Returns true if site (i, j) is full, and false otherwise.
    public boolean isFull(int i, int j) {
        // Throw an index error message if i and j is out of bonds.
        if ((i < 0 || i > n - 1) || (j < 0 || j > n - 1)) {
            throw new IndexOutOfBoundsException("Illegal i or j");
        }
        // A site is full if it is open and its corresponding uf is connected to the source.
        // However, use the ufForBackwash in order for a site to be full only if it is connected
        // to the source by its corresponding uf neighbors.
        return isOpen(i, j) && ufForBackwash.connected(encode(i, j), 0);
    }

    // Returns the number of open sites.
    public int numberOfOpenSites() {
        return openSites;
    }

    // Returns true if this system percolates, and false otherwise.
    public boolean percolates() {
        // It percolates if the sink is connected to the source.
        // Thus, check if the sink is connected to the source.
        return uf.connected(0, n * n + 1);
    }

    // Returns an integer ID (1...n) for site (i, j).
    private int encode(int i, int j) {
        // each site (i, j) should be represented as an integer,
        // Use the following formula k = ni + j, add 1 since 0 is a virtual source.
        int id = n * i + j + 1;
        return id;
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        int n = in.readInt();
        UFPercolation perc = new UFPercolation(n);
        while (!in.isEmpty()) {
            int i = in.readInt();
            int j = in.readInt();
            perc.open(i, j);
        }
        StdOut.printf("%d x %d system:\n", n, n);
        StdOut.printf("  Open sites = %d\n", perc.numberOfOpenSites());
        StdOut.printf("  Percolates = %b\n", perc.percolates());
        if (args.length == 3) {
            int i = Integer.parseInt(args[1]);
            int j = Integer.parseInt(args[2]);
            StdOut.printf("  isFull(%d, %d) = %b\n", i, j, perc.isFull(i, j));
        }
    }
}