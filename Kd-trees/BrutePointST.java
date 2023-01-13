import dsa.LinkedQueue;
import dsa.MinPQ;
import dsa.Point2D;
import dsa.RectHV;
import dsa.RedBlackBinarySearchTreeST;
import stdlib.StdIn;
import stdlib.StdOut;

public class BrutePointST<Value> implements PointST<Value> {
    // Underlying data structure to store the 2D points(keys)
    // and their corresponding values.
    private RedBlackBinarySearchTreeST<Point2D, Value> bst;

    // Constructs an empty symbol table.
    public BrutePointST() {
        bst = new RedBlackBinarySearchTreeST<Point2D, Value>();
    }

    // Returns true if this symbol table is empty, and false otherwise.
    public boolean isEmpty() {
        return bst.isEmpty();
    }

    // Returns the number of key-value pairs in this symbol table.
    public int size() {
        return bst.size();
    }

    // Inserts the given point and value into this symbol table.
    public void put(Point2D p, Value value) {
        if (p == null) {
            throw new NullPointerException("p is null");
        }
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        // Use the put method of RedBlackBinarySearchTreeST with the given values.
        bst.put(p, value);
    }

    // Returns the value associated with the given point in this symbol table, or null.
    public Value get(Point2D p) {
        if (p == null) {
            throw new NullPointerException("p is null");
        }
        // If bst contains p, use the get method from RedBlackBinarySearchTreeST to 
        // get p, and return it, otherwise return null.
        if (contains(p)) {
            return bst.get(p);
        }
        return null;
    }

    // Returns true if this symbol table contains the given point, and false otherwise.
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new NullPointerException("p is null");
        }
        // Use the get method from RedBlackBinarySearchTreeST.
        return bst.contains(p);
    }

    // Returns all the points in this symbol table.
    public Iterable<Point2D> points() {
        return bst.keys();
    }

    // Returns all the points in this symbol table that are inside the given rectangle.
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new NullPointerException("rect is null");
        }
        // Create a queue of Point2D.
        LinkedQueue<Point2D> pointsInRect = new LinkedQueue<>();
        // Iterate through all the points in bst.
        for (Point2D point: points()) {
            // if the bst's rectangle contains the point, enqueue that point.
            if (rect.contains(point)) {
                pointsInRect.enqueue(point);
            }
        }
        // return the queue.
        return pointsInRect;
    }

    // Returns the point in this symbol table that is different from and closest to the given point,
    // or null.
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new NullPointerException("p is null");
        }
        // Make a call to the iterable nearest method. Make sure that k = 1 and
        // to assign iterator() since this method is not iterable.
        return nearest(p, 1).iterator().next();
    }

    // Returns up to k points from this symbol table that are different from and closest to the
    // given point.
    public Iterable<Point2D> nearest(Point2D p, int k) {
        if (p == null) {
            throw new NullPointerException("p is null");
        }
        // Create a queue of k nearest points.
        LinkedQueue<Point2D> nearestK = new LinkedQueue<>();
        // Create a MinPQ with an appropriate comparator.
        MinPQ<Point2D> allnear = new MinPQ<Point2D>(size(), p.distanceToOrder());
        // Insert all the points different from p in MinPQ.
        for (Point2D point: bst.keys()) {
            if (p.equals(point)) {
                continue;
            }
            allnear.insert(point);
        }
        // Enqueue k points that are closest to p.
        for (int i = 1; i <= k; i++) {
            nearestK.enqueue(allnear.delMin());
        }
        // return the queue.
        return nearestK;
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        BrutePointST<Integer> st = new BrutePointST<Integer>();
        double qx = Double.parseDouble(args[0]);
        double qy = Double.parseDouble(args[1]);
        int k = Integer.parseInt(args[2]);
        Point2D query = new Point2D(qx, qy);
        RectHV rect = new RectHV(-1, -1, 1, 1);
        int i = 0;
        while (!StdIn.isEmpty()) {
            double x = StdIn.readDouble();
            double y = StdIn.readDouble();
            Point2D p = new Point2D(x, y);
            st.put(p, i++);
        }
        StdOut.println("st.empty()? " + st.isEmpty());
        StdOut.println("st.size() = " + st.size());
        StdOut.printf("st.contains(%s)? %s\n", query, st.contains(query));
        StdOut.printf("st.range(%s):\n", rect);
        for (Point2D p : st.range(rect)) {
            StdOut.println("  " + p);
        }
        StdOut.printf("st.nearest(%s) = %s\n", query, st.nearest(query));
        StdOut.printf("st.nearest(%s, %d):\n", query, k);
        for (Point2D p : st.nearest(query, k)) {
            StdOut.println("  " + p);
        }
    }
}
