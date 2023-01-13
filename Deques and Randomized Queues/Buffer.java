import dsa.LinkedStack;
import stdlib.StdOut;

// A data type to represent a text editor buffer.
public class Buffer {
    protected LinkedStack<Character> left;  // chars left of cursor
    protected LinkedStack<Character> right; // chars right of cursor

    // Creates an empty buffer.
    public Buffer() {
        left = new LinkedStack<Character>();
        right = new LinkedStack<Character>();
    }

    // Inserts c at the cursor position.
    public void insert(char c) {
        left.push(c);
    }

    // Deletes and returns the character immediately ahead of the cursor.
    public char delete() {
        if (!right.isEmpty()) {
            return right.pop();
        }
        return 0;
    }

    // Moves the cursor k positions to the left.
    public void left(int k) {
        int pos = k;
        while (!left.isEmpty() && --pos >= 0) {
            char character = left.pop();
            right.push(character);

        }
    }

    // Moves the cursor k positions to the right.
    public void right(int k) {
        int pos = k;
        while (!right.isEmpty() && --pos >= 0) {
            char character = right.pop();
            left.push(character);

        }
    }

    // Returns the number of characters in this buffer.
    public int size() {
        return left.size() + right.size();
    }

    // Returns a string representation of the buffer with the "|" character (not part of the buffer)
    // at the cursor position.
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Push chars from left into a temporary stack.
        LinkedStack<Character> temp = new LinkedStack<Character>();
        for (char character: left) {
            temp.push(character);
        }

        // Append chars from temporary stack to sb.
        for (char chars: temp) {
            sb.append(chars);
        }

        // Append "|" to sb.
        sb.append("|");

        // Append chars from right to sb.
        for (char chars: right) {
            sb.append(chars);
        }

        // Return the string from sb.
        return sb.toString();
    }

    // Unit tests the data type (DO NOT EDIT).
    public static void main(String[] args) {
        Buffer buf = new Buffer();
        String s = "There is grandeur in this view of life, with its several powers, having been " +
                "originally breathed into a few forms or into one; and that, whilst this planet " +
                "has gone cycling on according to the fixed law of gravity, from so simple a " +
                "beginning endless forms most beautiful and most wonderful have been, and are " +
                "being, evolved. ~ Charles Darwin, The Origin of Species";
        for (int i = 0; i < s.length(); i++) {
            buf.insert(s.charAt(i));
        }
        buf.left(buf.size());
        buf.right(97);
        s = "by the Creator ";
        for (int i = 0; i < s.length(); i++) {
            buf.insert(s.charAt(i));
        }
        buf.right(228);
        buf.delete();
        buf.insert('-');
        buf.insert('-');
        buf.left(342);
        StdOut.println(buf);
    }
}
