import stdlib.StdOut;

public class Sample {
    // Entry point.
    public static void main(String[] args) {
        // Accept lo(int), hi(int), k(int), and mode(String)
        // as command-line arguments.
        int lo = Integer.parseInt(args[0]);
        int hi = Integer.parseInt(args[1]);
        int k = Integer.parseInt(args[2]);
        String mode = args[3];
        // Throw an error message if mode is not equal to "+" or "-"
        if (!mode.equals("+") && !mode.equals("-")) {
            throw new IllegalArgumentException("Illegal mode");
        }
        // Create a random queue q
        ResizingArrayRandomQueue<Integer> q = new ResizingArrayRandomQueue<Integer>();
        // q.enqueue() all integers from interval [lo, hi].
        for (int i = lo; i <= hi; i++) {
            q.enqueue(i);
        }
        // if mode is "+",
        if (mode.equals("+")) {
            // Sample and write k integers from q to standard output.
            for (int i = 1; i <= k; i++) {
                StdOut.println(q.sample());
            }
        } else if (mode.equals("-")) {
            // if mode is "-", dequeue and write k integers from q
            // to standard output.
            for (int i = 1; i <= k; i++) {
                StdOut.println(q.dequeue());
            }
        }
    }
}
