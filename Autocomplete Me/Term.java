import java.util.Arrays;
import java.util.Comparator;

import stdlib.In;
import stdlib.StdOut;

public class Term implements Comparable<Term> {
    // Query string.
    private String query;
    // Query weight.
    private long weight;

    // Constructs a term given the associated query string, having weight 0.
    public Term(String query) {
        // Throw an error message if query is null.
        if (query == null) {
            throw new NullPointerException("query is null");
        }
        // Initialize the instance variables
        this.query = query;
        weight = 0;
    }

    // Constructs a term given the associated query string and weight.
    public Term(String query, long weight) {
        // Throw an error message if query is null and if weight is negative.
        if (query == null) {
            throw new NullPointerException("query is null");
        }
        if (weight < 0) {
            throw new IllegalArgumentException("Illegal weight");
        }
        // Initialize the instance variables
        this.query = query;
        this.weight = weight;
    }

    // Returns a string representation of this term.
    public String toString() {
        return weight+ "\t" +query;
    }

    // Returns a comparison of this term and other by query.
    public int compareTo(Term other) {
        // Strings are comparable data type, use compareTo method.
        String a = this.query;
        String b = other.query;
        return a.compareTo(b);
    }

    // Returns a comparator for comparing two terms in reverse order of their weights.
    public static Comparator<Term> byReverseWeightOrder() {
        return new ReverseWeightOrder();
    }

    // Returns a comparator for comparing two terms by their prefixes of length r.
    public static Comparator<Term> byPrefixOrder(int r) {
        // Throw an error message if r is negative.
        if (r < 0) {
            throw new IllegalArgumentException("Illegal r");
        }
        return new PrefixOrder(r);
    }

    // Reverse-weight comparator.
    private static class ReverseWeightOrder implements Comparator<Term> {
        // Returns a comparison of terms v and w by their weights in reverse order.
        // Use a comparator to compare the two weights. And have w.weight be the first argument,
        // and v.weight be last.
        public int compare(Term v, Term w) {
            return Long.compare(w.weight, v.weight);
        }
    }

    // Prefix-order comparator.
    private static class PrefixOrder implements Comparator<Term> {
        // Prefix length.
        private int r;

        // Constructs a new prefix order given the prefix length.
        // Initialize instance variable.
        PrefixOrder(int r) {
            this.r = r;
        }

        // Returns a comparison of terms v and w by their prefixes of length r.
        public int compare(Term v, Term w) {
            // Assign a to be a substring of v.query, and b to be substring of w.query.
            // Thus, compare a and b.
            String a = v.query.substring(0, Math.min(r, v.query.length()));
            String b = w.query.substring(0, Math.min(r, w.query.length()));
            return a.compareTo(b);
        }
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        String filename = args[0];
        int k = Integer.parseInt(args[1]);
        In in = new In(filename);
        int N = in.readInt();
        Term[] terms = new Term[N];
        for (int i = 0; i < N; i++) {
            long weight = in.readLong();
            in.readChar();
            String query = in.readLine();
            terms[i] = new Term(query.trim(), weight);
        }
        StdOut.printf("Top %d by lexicographic order:\n", k);
        Arrays.sort(terms);
        for (int i = 0; i < k; i++) {
            StdOut.println(terms[i]);
        }
        StdOut.printf("Top %d by reverse-weight order:\n", k);
        Arrays.sort(terms, Term.byReverseWeightOrder());
        for (int i = 0; i < k; i++) {
            StdOut.println(terms[i]);
        }
    }
}
