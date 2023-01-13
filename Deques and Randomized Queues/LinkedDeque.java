import java.util.Iterator;
import java.util.NoSuchElementException;

import stdlib.StdOut;
import stdlib.StdRandom;

// A data type to represent a double-ended queue (aka deque), implemented using a doubly-linked
// list as the underlying data structure.
public class LinkedDeque<Item> implements Iterable<Item> {
    // The front of the deque.
    private Node first;
    // The back of the deque.
    private Node last;
    // Size of the deque.
    private int n;

    // Constructs an empty deque.
    public LinkedDeque() {
        // Initialize the instance variables.
        first = null;
        last = null;
        n = 0;
    }

    // Returns true if this deque is empty, and false otherwise.
    public boolean isEmpty() {
        // if n equals zero it means deque is empty
        return n == 0;
    }

    // Returns the number of items in this deque.
    public int size() {
        // return n.
        return n;
    }

    // Adds item to the front of this deque.
    public void addFirst(Item item) {
        // Throw an error message if item is null.
        if (item == null) {
            throw new NullPointerException("item is null");
        }
        // Insert at the beginning operation.
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        first.next = oldfirst;
        // If deque is empty, last equals first.
        if (isEmpty()) {
            last = first;
        } else {
            // Make sure oldfirst's previous pointer is pointing
            // at the current first.
            oldfirst.prev = first;
        }
        n++;
    }

    // Adds item to the back of this deque.
    public void addLast(Item item) {
        // Throw an error message if item is null.
        if (item == null) {
            throw new NullPointerException("item is null");
        }
        // Insert at the end operation.
        Node oldlast = last;
        last = new Node();
        last.item = item;
        last.prev = oldlast;
        // If deque is empty, first equals last.
        if (isEmpty()) {
            first = last;
        } else {
            // Make sure oldlast's next pointer is pointing
            // at the current last.
            oldlast.next = last;
        }
        n++;
    }

    // Returns the item at the front of this deque.
    public Item peekFirst() {
        // Throw an error message if deque is empty.
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }
        // return first.item.
        return first.item;
    }

    // Removes and returns the item at the front of this deque.
    public Item removeFirst() {
        // Throw an error message if deque is empty.
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }
        // Remove from the beginning operation.
        Item item = first.item;
        first = first.next;
        // decrement n by 1.
        n--;
        if (isEmpty()) {
            last = null;
        }
        // return the removed item.
        return item;
    }

    // Returns the item at the back of this deque.
    public Item peekLast() {
        // Throw an error message if deque is empty.
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }
        // return last.item.
        return last.item;
    }

    // Removes and returns the item at the back of this deque.
    public Item removeLast() {
        // Throw an error message if deque is empty.
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }

        // Apply the remove from the beginning operation,
        // but instead of removing the first.item, remove the last.item
        // and return it.
        Item item = last.item;
        last = last.prev;
        // n must be decremented.
        n--;
        if (isEmpty()) {
            first = null;
        }

        return item;
    }

    // Returns an iterator to iterate over the items in this deque from front to back.
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    // Returns a string representation of this deque.
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Item item : this) {
            sb.append(item);
            sb.append(", ");
        }
        return n > 0 ? "[" + sb.substring(0, sb.length() - 2) + "]" : "[]";
    }

    // A deque iterator.
    private class DequeIterator implements Iterator<Item> {
        // current node in the iterator.
        private Node current;

        // Constructs an iterator.
        public DequeIterator() {
            // Initialize the instance variable.
            current = first;
        }

        // Returns true if there are more items to iterate, and false otherwise.
        public boolean hasNext() {
            // If current is not null there are more items to
            // iterate.
            return current != null;
        }

        // Returns the next item.
        public Item next() {
            // Throw an error message if there are no more item to iterate.
            if (!hasNext()) {
                throw new NoSuchElementException("Iterator is empty");
            }
            // Return the current item and advance to the next node.
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    // A data type to represent a doubly-linked list. Each node in the list stores a generic item
    // and references to the next and previous nodes in the list.
    private class Node {
        private Item item;  // the item
        private Node next;  // the next node
        private Node prev;  // the previous node
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        LinkedDeque<Character> deque = new LinkedDeque<Character>();
        String quote = "There is grandeur in this view of life, with its several powers, having " +
                "been originally breathed into a few forms or into one; and that, whilst this " +
                "planet has gone cycling on according to the fixed law of gravity, from so simple" +
                " a beginning endless forms most beautiful and most wonderful have been, and are " +
                "being, evolved. ~ Charles Darwin, The Origin of Species";
        int r = StdRandom.uniform(0, quote.length());
        StdOut.println("Filling the deque...");
        for (int i = quote.substring(0, r).length() - 1; i >= 0; i--) {
            deque.addFirst(quote.charAt(i));
        }
        for (int i = 0; i < quote.substring(r).length(); i++) {
            deque.addLast(quote.charAt(r + i));
        }
        StdOut.printf("The deque (%d characters): ", deque.size());
        for (char c : deque) {
            StdOut.print(c);
        }
        StdOut.println();
        StdOut.println("Emptying the deque...");
        double s = StdRandom.uniform();
        for (int i = 0; i < quote.length(); i++) {
            if (StdRandom.bernoulli(s)) {
                deque.removeFirst();
            } else {
                deque.removeLast();
            }
        }
        StdOut.println("deque.isEmpty()? " + deque.isEmpty());
    }
}
