
import dsa.LinkedStack;

import stdlib.StdIn;
import stdlib.StdOut;

public class Sort {
    // Entry point.
    public static void main(String[] args) {
        // Create a deque d.
        LinkedDeque<String> d = new LinkedDeque<String>();
        // For each word w,
        while (!StdIn.isEmpty()) {
            String w = StdIn.readString();
            // if deque is empty, add w.
            if (d.isEmpty()) {
                d.addFirst(w);
            } else if (less(w, d.peekFirst())) {
                // if w is less than the first word in d
                // add w to front of d.
                d.addFirst(w);
            } else if (less(d.peekLast(), w)) {
                // if w is greater than the last word in d
                // add w to the back of d.
                d.addLast(w);
            } else {
                // Otherwise, create a temporary stack,
                LinkedStack<String> s = new LinkedStack<String>();
                // Remove all the words on d that are less than w
                // and add them to s.
                for (String l: d) {
                    if (less(l, w)) {
                        String word = d.removeFirst();
                        s.push(word);
                    }
                }
                // Then, add w to the front of d.
                d.addFirst(w);
                // Therefore, add the words less than w in s
                // back to d.
                for (String word: s) {
                    d.addFirst(s.pop());
                }
            }

        }
        // Print each word in d to the standard output.
        for (String word: d) {
            StdOut.println(word);
        }

    }

    // Returns true if v is less than w according to their lexicographic order, and false otherwise.
    private static boolean less(String v, String w) {
        return v.compareTo(w) < 0;
    }
}
