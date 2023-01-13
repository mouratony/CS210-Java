import dsa.BasicST;
import dsa.LinkedQueue;
import stdlib.StdIn;
import stdlib.StdOut;

public class ArrayST<Key, Value> implements BasicST<Key, Value> {
    private Key[] keys;     // keys in the symbol table
    private Value[] values; // the corresponding values
    private int n;          // number of key-value pairs

    // Constructs an empty symbol table.
    public ArrayST() {
        // Initialize Instance Variables.
        keys = (Key[]) new Object[2];
        values = (Value[]) new Object[2];
        n = 0;
    }

    // Returns true if this symbol table is empty, and false otherwise.
    public boolean isEmpty() {
        return n == 0;
    }

    // Returns the number of key-value pairs in this symbol table.
    public int size() {
        return n;
    }

    // Inserts the key and value pair into this symbol table.
    public void put(Key key, Value value) {
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }
        if (value == null) {
            throw new IllegalArgumentException("value is null");
        }
        // Search if symbol table contain the given key, if such,
        // update the value of the key in the ST to the given Value,
        // and return.
        for (int i = 0; i < n; i++) {
            if (key.equals(keys[i])) {
                values[i] = value;
                return;
            }
        }
        // If the number of keys equals keys.length,
        // double the size of the array of keys.
        if (n == keys.length) {
            resize(2 * keys.length);
        }
        // Set values[n] = value, and keys[n] = key.
        values[n] = value;
        keys[n] = key;
        // increment n.
        n++;
    }

    // Returns the value associated with key in this symbol table, or null.
    public Value get(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }
        // Search if symbol table contain the given key, if such,
        // return its value.
        for (int i = 0; i < n; i++) {
            if (keys[i].equals(key)) {
                return values[i];
            }
        }
        // return null.
        return null;
    }

    // Returns true if this symbol table contains key, and false otherwise.
    public boolean contains(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }
        // return get(key) == null;
        return get(key) != null;
    }

    // Deletes key and the associated value from this symbol table.
    public void delete(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }
        // Get the location of the key.
        int i;
        for (i = 0; i < n; i++) {
            if (key.equals(keys[i])) {
                break;
            }
        }
        // If key is not in ST, return.
        if (i == n) {
            return;
        }
        // Remove the key, by making all the keys and values pair in ST, starting at i,
        // equal the keys and values pair at i + 1.
        for (int j = i; j < n - 1; j++) {
            keys[j] = keys[j + 1];
            values[j] = values[j + 1];
        }
        // Decrement n.
        n--;

        // Set the key and value at n to null.
        keys[n] = null;
        values[n] = null;
        // if n equals quarter size of the ST, halve the size of ST.
        if (n > 0 && n == keys.length / 4) {
            resize(keys.length / 2);
        }
    }

    // Returns all the keys in this symbol table.
    public Iterable<Key> keys() {
        // Create a queue.
        LinkedQueue<Key> queue = new LinkedQueue<>();
        // Enqueue all the keys in ST.
        for (int i = 0; i < n; i++) {
            queue.enqueue(keys[i]);
        }
        // Return queue.
        return queue;
    }

    // Resizes the underlying arrays to capacity.
    private void resize(int capacity) {
        // The resize method.
        Key[] tempk = (Key[]) new Object[capacity];
        Value[] tempv = (Value[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            tempk[i] = keys[i];
            tempv[i] = values[i];
        }
        values = tempv;
        keys = tempk;
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        ArrayST<String, Integer> st = new ArrayST<>();
        for (int i = 0; !StdIn.isEmpty(); i++) {
            String key = StdIn.readString();
            st.put(key, i);
        }
        for (String s : st.keys()) {
            StdOut.println(s + " " + st.get(s));
        }
    }
}
