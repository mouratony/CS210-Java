import java.util.Iterator;
import java.util.NoSuchElementException;

import stdlib.StdOut;
import stdlib.StdRandom;

// A data type to represent a random queue, implemented using a resizing array as the underlying
// data structure.
public class ResizingArrayRandomQueue<Item> implements Iterable<Item> {
    // Array to store the items of queue, item[] q.
    private Item[] q;
    // Size of the queue, int n.
    private int n;

    // Constructs an empty random queue.
    public ResizingArrayRandomQueue() {
        // Initialize the instance variables.
        q = (Item[]) new Object[2];
        n = 0;
    }

    // Returns true if this queue is empty, and false otherwise.
    public boolean isEmpty() {
        // Return n == 0.
        return n == 0;
    }

    // Returns the number of items in this queue.
    public int size() {
        // Return n.
        return n;
    }

    // Adds item to the end of this queue.
    public void enqueue(Item item) {
        // Throw an error message if item is null.
        if (item == null) {
            throw new NullPointerException("item is null");
        }
        // if number of items equals the size of q,
        // double the size of q.
        if (q.length == n) {
            resize(2 * q.length);
        }
        // Add item to q at index n.
        q[n] = item;
        // Increase n by 1.
        n++;
    }

    // Returns a random item from this queue.
    public Item sample() {
        // Throw an error message if queue is empty.
        if (isEmpty()) {
            throw new NoSuchElementException("Random queue is empty");
        }
        // Having a random integer r, return the item in q at index r.
        int r = StdRandom.uniform(0, n);
        return q[r];
    }

    // Removes and returns a random item from this queue.
    public Item dequeue() {
        // Throw an error message if queue is empty.
        if (isEmpty()) {
            throw new NoSuchElementException("Random queue is empty");
        }
        // Create a random integer r of interval [o, n).
        int r = StdRandom.uniform(0, n);
        // Save q[r] in item.
        Item item = q[r];
        // Set q[r] to q[n - 1] and q[n - 1] to null.
        q[r] = q[n - 1];
        q[n - 1] = null;
        // If q is at quarter capacity,
        if (n > 0 && n == q.length/4) {
            // resize it to half its current capacity.
            resize(q.length / 2);
        }
        // Decrement n by 1.
        n--;
        // Return item.
        return item;
    }

    // Returns an independent iterator to iterate over the items in this queue in random order.
    public Iterator<Item> iterator() {
        return new RandomQueueIterator();
    }

    // Returns a string representation of this queue.
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Item item : this) {
            sb.append(item);
            sb.append(", ");
        }
        return n > 0 ? "[" + sb.substring(0, sb.length() - 2) + "]" : "[]";
    }

    // An iterator, doesn't implement remove() since it's optional.
    private class RandomQueueIterator implements Iterator<Item> {
        // Array to store the items of q.
        private Item[] items;
        // Index of the current item in items.
        private int current;

        // Constructs an iterator.
        public RandomQueueIterator() {
            // Create an array of items, that store items in q.
            items = (Item[]) new Object[n];
            for (int i = 0; i < n; i++) {
                items[i] = q[i];
            }
            // Shuffle items.
            StdRandom.shuffle(items);
            // Set current to 0.
            current = 0;
        }

        // Returns true if there are more items to iterate, and false otherwise.
        public boolean hasNext() {
            // if current less than n, there are more items.
            return current < n;
        }

        // Returns the next item.
        public Item next() {
            // Throw an error message if there is no next item to iterate.
            if (!hasNext()) {
                throw new NoSuchElementException("Iterator is empty");
            }
            // Return item from items at current index, and advance current by one.
            return items[current++];
        }
    }

    // Resizes the underlying array.
    private void resize(int max) {
        Item[] temp = (Item[]) new Object[max];
        for (int i = 0; i < n; i++) {
            if (q[i] != null) {
                temp[i] = q[i];
            }
        }
        q = temp;
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        ResizingArrayRandomQueue<Integer> q = new ResizingArrayRandomQueue<Integer>();
        int sum = 0;
        for (int i = 0; i < 1000; i++) {
            int r = StdRandom.uniform(10000);
            q.enqueue(r);
            sum += r;
        }
        int iterSumQ = 0;
        for (int x : q) {
            iterSumQ += x;
        }
        int dequeSumQ = 0;
        while (q.size() > 0) {
            dequeSumQ += q.dequeue();
        }
        StdOut.println("sum       = " + sum);
        StdOut.println("iterSumQ  = " + iterSumQ);
        StdOut.println("dequeSumQ = " + dequeSumQ);
        StdOut.println("iterSumQ + dequeSumQ == 2 * sum? " + (iterSumQ + dequeSumQ == 2 * sum));
    }
}
