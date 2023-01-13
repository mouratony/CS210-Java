import dsa.Inversions;
import dsa.LinkedQueue;
import stdlib.In;
import stdlib.StdOut;

// A data type to represent a board in the 8-puzzle game or its generalizations.
public class Board {
    // Tiles in the board.
    private int[][] tiles;
    // Board size.
    private int n;
    // Hamming distance to the goal board.
    private int hamming;
    // Manhattan distance to the goal board.
    private int manhattan;
    // Position of the blank tile in row-major order.
    private int blankPos;

    // Constructs a board from an n x n array; tiles[i][j] is the tile at row i and column j, with 0
    // denoting the blank tile.
    public Board(int[][] tiles) {
        // Initialize the instance variables.
        // Set this.tiles to tiles.
        this.tiles = tiles;
        // Set n to tiles.length.
        n = tiles.length;

        hamming = 0;
        manhattan = 0;
        // Create a 1D array with tiles in row-major order.
        int[] rowMajor = new int[n*n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                rowMajor[i * n + j] = tileAt(i, j);

            }
        }
        // Compute the Hamming and manhattan distance.
        for (int i = 0; i < n*n; i++) {
            if (rowMajor[i] != i + 1 && rowMajor[i] != 0) {
                hamming++;
                int goal = rowMajor[i];
                int pos = i;
                int row = Math.abs((goal - 1)/n - pos/n);
                int col = Math.abs((goal - 1) % n - pos % n);
                manhattan += row + col;
            }
            // Determine the blank position.
            if (rowMajor[i] == 0) {
                blankPos = i + 1;
            }

        }
    }

    // Returns the size of this board.
    public int size() {
        return n;
    }

    // Returns the tile at row i and column j of this board.
    public int tileAt(int i, int j) {
        return tiles[i][j];
    }

    // Returns Hamming distance between this board and the goal board.
    public int hamming() {
        return hamming;
    }

    // Returns the Manhattan distance between this board and the goal board.
    public int manhattan() {
        return manhattan;
    }

    // Returns true if this board is the goal board, and false otherwise.
    public boolean isGoal() {
        // Return true if there is no tiles in wrong position.
        return hamming == 0;
    }

    // Returns true if this board is solvable, and false otherwise.
    public boolean isSolvable() {
        // Create a 1D array with tiles in row-major order without the blankPos.
        int[] rowMajorNoBlank = new int[n*n - 1];
        // Initialize a counter, and add the tileAt(i, j) to the array at index count.
        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tileAt(i, j) != 0) {
                    rowMajorNoBlank[count] = tileAt(i, j);
                    count++;
                }
            }
        }
        // Number of inversions in the array
        int numbInversions = (int) Inversions.count(rowMajorNoBlank);
        // Return true if Board is of size odd and has even number of inversions.
        int evenBoard = (numbInversions + (blankPos - 1)/n);
        if (size() % 2 != 0 && numbInversions % 2 == 0) {
            return true;
        }
        // Return true if board's size is even and the sum of number of inversions with
        // the row in which blankPos locates must be odd, false otherwise.
        return size() % 2 == 0 && evenBoard % 2 != 0;
    }

    // Returns an iterable object containing the neighboring boards of this board.
    public Iterable<Board> neighbors() {
        // Create a queue q.
        LinkedQueue<Board> q = new LinkedQueue<Board>();
        int row = (blankPos - 1) / n;
        int col = (blankPos - 1) % n;
        // Check if blankPos is away from the last row, create a clone of the
        // board, and swap the blankPos with the South tile, and enqueue
        // the neighbor in q.
        if (row != n - 1) {
            int[][] copy = cloneTiles();
            int tmp = copy[row][col];
            copy[row][col] = copy[row + 1][col];
            copy[row + 1][col] = tmp;
            Board neighbor = new Board(copy);
            q.enqueue(neighbor);
        }
        // Check if blankPos is away from the first row, create a clone of the
        // board, and swap the blankPos with the North tile, and enqueue
        // the neighbor in q.
        if (row != 0) {
            int[][] copy = cloneTiles();
            int tmp = copy[row][col];
            copy[row][col] = copy[row - 1][col];
            copy[row - 1][col] = tmp;
            Board neighbor = new Board(copy);
            q.enqueue(neighbor);
        }
        // Check if blankPos is away from the last column, create a clone of the
        // board, and swap the blankPos with the East tile, and enqueue
        // the neighbor in q.
        if (col != n - 1) {
            int[][] copy = cloneTiles();
            int tmp = copy[row][col];
            copy[row][col] = copy[row][col + 1];
            copy[row][col + 1] = tmp;
            Board neighbor = new Board(copy);
            q.enqueue(neighbor);
        }
        // Check if blankPos is away from the first column, create a clone of the
        // board, and swap the blankPos with the West tile, and enqueue
        // the neighbor in q.
        if (col != 0) {
            int[][] copy = cloneTiles();
            int tmp = copy[row][col];
            copy[row][col] = copy[row][col - 1];
            copy[row][col - 1] = tmp;
            Board neighbor = new Board(copy);
            q.enqueue(neighbor);
        }
        // Return q.
        return q;
    }

    // Returns true if this board is the same as other, and false otherwise.
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (other.getClass() != this.getClass()) {
            return false;
        }
        // Check if all the tiles of the two boards are equal.
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (this.tileAt(i, j) != ((Board) other).tileAt(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    // Returns a string representation of this board.
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2s", tiles[i][j] == 0 ? " " : tiles[i][j]));
                if (j < n - 1) {
                    s.append(" ");
                }
            }
            if (i < n - 1) {
                s.append("\n");
            }
        }
        return s.toString();
    }

    // Returns a defensive copy of tiles[][].
    private int[][] cloneTiles() {
        int[][] clone = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                clone[i][j] = tiles[i][j];
            }
        }
        return clone;
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        Board board = new Board(tiles);
        StdOut.printf("The board (%d-puzzle):\n%s\n", n, board);
        String f = "Hamming = %d, Manhattan = %d, Goal? %s, Solvable? %s\n";
        StdOut.printf(f, board.hamming(), board.manhattan(), board.isGoal(), board.isSolvable());
        StdOut.println("Neighboring boards:");
        for (Board neighbor : board.neighbors()) {
            StdOut.println(neighbor);
            StdOut.println("----------");
        }
    }
}
