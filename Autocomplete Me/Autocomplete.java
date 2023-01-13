import java.util.Arrays;
import java.util.Comparator;

import stdlib.In;
import stdlib.StdIn;
import stdlib.StdOut;

public class Autocomplete {
    // Array of terms.
    private Term[] terms;

    // Constructs an autocomplete data structure from an array of terms.
    public Autocomplete(Term[] terms) {
        // Throw an error message if terms is null.
        if (terms == null) {
            throw new NullPointerException("terms is null");
        }
        // Initialize this.terms as a copy of terms.
        this.terms = new Term[terms.length];
        for (int i = 0; i < terms.length; i++) {
            this.terms[i] = terms[i];
        }
        // Sort this.terms.
        Arrays.sort(this.terms);
    }

    // Returns all terms that start with prefix, in descending order of their weights.
    public Term[] allMatches(String prefix) {
        // Throw an error message if prefix is null.
        if (prefix == null) {
            throw new NullPointerException("prefix is null");
        }
        // Use BinarySearchDeluxe.firstIndexOf() to find the index of the first
        // match.
        int i = BinarySearchDeluxe.firstIndexOf(this.terms, new Term(prefix),
                Term.byPrefixOrder(prefix.length()));
        // Make a call to the method numberOfMatches() with prefix as argument
        int n =  numberOfMatches(prefix);
        // Create an array of terms that matches, sort it, and return it.
        Term[] matches = new Term[n];
        for (int k = 0; k < n; k++) {
            matches[k] = terms[i + k];
        }
        Arrays.sort(matches, Term.byReverseWeightOrder());
        return matches;
    }

    // Returns the number of terms that start with prefix.
    public int numberOfMatches(String prefix) {
        // Throw an error message if prefix is null.
        if (prefix == null) {
            throw new NullPointerException("prefix is null");
        }
        // Terms with prefix, prefix.
        Term term = new Term(prefix);
        // Assign the comparator Term.byPrefixOrder() into a variable.
        Comparator<Term> prefixOrder = Term.byPrefixOrder(prefix.length());
        // Get the indexes of the first and last term that has the same prefix.
        int i = BinarySearchDeluxe.firstIndexOf(terms, term, prefixOrder);
        int j = BinarySearchDeluxe.lastIndexOf(terms, term, prefixOrder);
        // Compute n as j - i + 1.
        int n =  i == -1 && j == -1 ? 0 : j - i + 1;
        // Return n.
        return n;
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
        Autocomplete autocomplete = new Autocomplete(terms);
        StdOut.print("Enter a prefix (or ctrl-d to quit): ");
        while (StdIn.hasNextLine()) {
            String prefix = StdIn.readLine();
            Term[] results = autocomplete.allMatches(prefix);
            String msg = " matches for \"" + prefix + "\", in descending order by weight:";
            if (results.length == 0) {
                msg = "No matches";
            } else if (results.length > k) {
                msg = "First " + k + msg;
            } else {
                msg = "All" + msg;
            }
            StdOut.printf("%s\n", msg);
            for (int i = 0; i < Math.min(k, results.length); i++) {
                StdOut.println("  " + results[i]);
            }
            StdOut.print("Enter a prefix (or ctrl-d to quit): ");
        }
    }
}
