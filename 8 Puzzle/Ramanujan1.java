import stdlib.StdOut;

public class Ramanujan1 {
    // Entry point.
    public static void main(String[] args) {
        // Accept n (int) as command-line argument.
        int n = Integer.parseInt(args[0]);
        // Have four nested for loops.
        // 0 < a^3 <= n
        for (int a = 1; a*a*a <= n; a++) {
            // a < b^3 <= n - a^3
            for (int b = a + 1; b*b*b <= n - a*a*a; b++) {
                // a < c^3 <= n
                for (int c = a + 1; c*c*c <= n; c++) {
                    // c < d^3 <= n - c^3
                    for (int d = c + 1; d*d*d <= n - c*c*c; d++) {
                        // Print total = a^3 + b^3 = c^3 + d^3 if
                        // a^3 + b^3 = c^3 + d^3 <= n.
                        int first = a*a*a + b*b*b;
                        int second = c*c*c + d*d*d;
                        if (first == second && first <= n) {
                            String x =  a +"^3 + " + b + "^3";
                            String y =  c +"^3 + " + d + "^3";
                            StdOut.println(first + " = " + x + " = " + y);
                        }
                    }
                }
            }
        }
    }
}
