import dsa.MinPQ;
import stdlib.StdOut;

public class Ramanujan2 {
    // Entry point.
    public static void main(String[] args) {
        // take n from the command-line argument.
        int n = Integer.parseInt(args[0]);
        // Create a MinPQ of Pair(i, i + 1).
        MinPQ<Pair> pq = new MinPQ<Pair>();
        for (int i = 1; i*i*i < n; i++) {
            pq.insert(new Pair(i, i + 1));
        }
        // Initialize prev as Pair(0, 0).
        Pair prev = new Pair(0, 0);
        // While pq is not empty.
        while (!pq.isEmpty()) {
            // Remove the smallest value of pq and assign it to current.
            Pair current = pq.delMin();
            // Print if prev's sum of cubes equals current's sum of cubes.
            if (prev.compareTo(current) == 0 && prev.sumOfCubes <= n) {
                String x =  prev.i +"^3 + " + prev.j + "^3";
                String y =  current.i +"^3 + " + current.j + "^3";
                StdOut.println(prev.sumOfCubes + " = " + x + " = " + y);
            }
            // Set prev to current.
            prev = current;
            // if j^3 < n, insert Pair(i, j + 1) in pq.
            if (current.j * current.j* current.j < n) {
                pq.insert(new Pair(current.i, current.j + 1));
            }
        }
    }

    // A data type that encapsulates a pair of numbers (i, j) and the sum of their cubes.
    private static class Pair implements Comparable<Pair> {
        private int i;          // first number in the pair
        private int j;          // second number in the pair
        private int sumOfCubes; // i^3 + j^3

        // Constructs a pair (i, j).
        public Pair(int i, int j) {
            this.i = i;
            this.j = j;
            sumOfCubes = i * i * i + j * j * j;
        }

        // Returns a comparison of pairs this and other based on their sum-of-cubes values.
        public int compareTo(Pair other) {
            return sumOfCubes - other.sumOfCubes;
        }
    }
}
