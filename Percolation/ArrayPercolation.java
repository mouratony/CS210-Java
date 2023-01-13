import stdlib.In;
import stdlib.StdOut;

// An implementation of the Percolation API using a 2D array.
public class ArrayPercolation implements Percolation {
    // Instance Variables.
    // Percolation system size (int n).
    private int n;
    // Percolation system (a 2d array of booleans).
    private boolean[][] open;
    // Number of open sites in the system.
    private int openSites;

    // Constructs an n x n percolation system, with all sites blocked.
    public ArrayPercolation(int n) {
        // Throw an error message if n <= 0.
        if (n <= 0) {
            throw new IllegalArgumentException("Illegal n");
        }
        // Initialize the instance variables.
        // set this.n to n.
        this.n = n;
        // By default the 2d array will have all entries false (all sites are blocked).
        open = new boolean[n][n];
        // Set number of open sites to 0.
        openSites = 0;
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
        }
    }

    // Returns true if site (i, j) is open, and false otherwise.
    public boolean isOpen(int i, int j) {
        // Throw an index error message if i and j is out of bonds.
        if ((i < 0 || i > n - 1) || (j < 0 || j > n - 1)) {
            throw new IndexOutOfBoundsException("Illegal i or j");
        }
        // It is a boolean array, thus the value that will be returned is true or false.
        return open[i][j];
    }

    // Returns true if site (i, j) is full, and false otherwise.
    public boolean isFull(int i, int j) {
        // Throw an index error message if i and j is out of bonds.
        if ((i < 0 || i > n - 1) || (j < 0 || j > n - 1)) {
            throw new IndexOutOfBoundsException("Illegal i or j");
        }
        // Create a 2D boolean array of size n x n, and name it full.
        boolean[][] full = new boolean[n][n];
        // Call the floodfill() method on every site in the first row
        // of the percolation system, and use "full" as the first argument.
        for (int t = 0; t <= n - 1; t++) {
            floodFill(full, 0, t);
        }
        // Return full[i][j] (the site (i, j) is full or not).
        return full[i][j];
    }

    // Returns the number of open sites.
    public int numberOfOpenSites() {
        return openSites;

    }

    // Returns true if this system percolates, and false otherwise.
    public boolean percolates() {
        // A system percolates if the last row as a full site.
        // By using a for loop we search each site at the last row.
        for (int t = 0; t <= n - 1; t++) {
            // Call the method isFull to see if the system percolates or not.
            if (isFull(n - 1, t)) {
                return true;
            }
        } return false;
    }

    // Recursively flood fills full[][] using depth-first exploration, starting at (i, j).
    private void floodFill(boolean[][] full, int i, int j) {
        // Return if the i and j out bonds or if the site (i, j) blocked or full.
        if ((i < 0 || i > n - 1) || (j < 0 || j > n - 1) || (full[i][j]) || (!isOpen(i, j))) {
            return;
        }
        // Fill the site (i, j).
        full[i][j] = true;

        // Recursively call floodfill() on the sites to
        // the north, east, west, and south of site (i, j).
        floodFill(full, i - 1, j);
        floodFill(full, i + 1, j);
        floodFill(full, i, j - 1);
        floodFill(full, i, j + 1);

    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        int n = in.readInt();
        ArrayPercolation perc = new ArrayPercolation(n);
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