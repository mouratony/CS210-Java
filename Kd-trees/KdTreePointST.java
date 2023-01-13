import dsa.LinkedQueue;
import dsa.MaxPQ;
import dsa.Point2D;
import dsa.RectHV;
import stdlib.StdIn;
import stdlib.StdOut;

public class KdTreePointST<Value> implements PointST<Value> {
    // The root of a 2dTree.
    private Node root;
    // Number of nodes in the tree.
    private int n;

    // Constructs an empty symbol table.
    public KdTreePointST() {
        // Initialize the instance Variables
        // Set root to null, and n to 0.
        root = null;
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

    // Inserts the given point and value into this symbol table.
    public void put(Point2D p, Value value) {
        if (p == null) {
            throw new NullPointerException("p is null");
        }
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        // Create a rectangle for the root, where the xMin, and yMin values are
        // negative infinity, xMax and yMax values are positive infinity.
        RectHV r = new RectHV(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY,
                Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        // Set root to put(root, p, value, r, true).
        root = put(root, p, value, r, true);
    }

    // Returns the value associated with the given point in this symbol table, or null.
    public Value get(Point2D p) {
        if (p == null) {
            throw new NullPointerException("p is null");
        }
        // Make a call to private get method with
        // arguments root, p, and true, respectively.
        return get(root, p, true);
    }

    // Returns true if this symbol table contains the given point, and false otherwise.
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new NullPointerException("p is null");
        }
        return get(p) != null;
    }

    // Returns all the points in this symbol table.
    public Iterable<Point2D> points() {
        // Create two queues one of node, q1, the other of Point2D, q2.
        LinkedQueue<Node> q1 = new LinkedQueue<Node>();
        LinkedQueue<Point2D> q2 = new LinkedQueue<Point2D>();
        // Start by inserting root into q1.
        q1.enqueue(root);
        // While there is a node in q1.
        while (!q1.isEmpty()) {
            // set x to q1.dequeue().
            Node x = q1.dequeue();
            // See if there is a node in the left subtree and right subtree.
            // Thus, enqueue the node.
            if (x.lb != null) {
                q1.enqueue(x.lb);
            }
            if (x.rt != null) {
                q1.enqueue(x.rt);
            }
            // Enqueue point in x to q2.
            q2.enqueue(x.p);
        }
        // return q2.
        return q2;
    }

    // Returns all the points in this symbol table that are inside the given rectangle.
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new NullPointerException("rect is null");
        }
        // Create a queue.
        LinkedQueue<Point2D> queue = new LinkedQueue<>();
        // make a call to the private range method, with arguments root,
        // rect, queue, respectively.
        range(root, rect, queue);
        // return queue.
        return queue;
    }

    // Returns the point in this symbol table that is different from and closest to the given point,
    // or null.
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new NullPointerException("p is null");
        }
        // make a call to the private nearest method, with arguments root,
        // null, true, respectively, and return it.
        return nearest(root, p, null, true);
    }

    // Returns up to k points from this symbol table that are different from and closest to the
    // given point.
    public Iterable<Point2D> nearest(Point2D p, int k) {
        if (p == null) {
            throw new NullPointerException("p is null");
        }
        // Create a MaxPQ pq.
        // make a call to the private iterable nearest method, with arguments root,
        // p, k, pq, and true, respectively.
        // return pq.
        MaxPQ<Point2D> pq = new MaxPQ<Point2D>(p.distanceToOrder());
        nearest(root, p, k, pq, true);
        return pq;
    }

    // Note: In the helper methods that have lr as a parameter, its value specifies how to
    // compare the point p with the point x.p. If true, the points are compared by their
    // x-coordinates; otherwise, the points are compared by their y-coordinates. If the
    // comparison of the coordinates (x or y) is true, the recursive call is made on x.lb;
    // otherwise, the call is made on x.rt.

    // Inserts the given point and value into the KdTree x having rect as its axis-aligned
    // rectangle, and returns a reference to the modified tree.
    private Node put(Node x, Point2D p, Value value, RectHV rect, boolean lr) {
        // if x is null, increment n, and return a new node with p, value, and rect
        // as arguments.
        if (x == null) {
            n++;
            return new Node(p, value, rect);
        }
        // if point in x equals the given point, set the value of x to the
        // given value.
        if (x.p.equals(p)) {
            x.value = value;
        } else {
            // Otherwise, check what coordinate is being considered,
            // Then compare the value of the points depending on the
            // compared coordinates, thus determine the location, where
            // the new node will be inserted.
            // Also, the values of the rect must always be appropriately updated when
            // making recursive call.
            if (lr) {
                if (x.p.x() <= p.x()) {
                    RectHV r = new RectHV(x.p.x(), rect.yMin(), rect.xMax(), rect.yMax());
                    x.rt = put(x.rt, p, value, r, !lr);
                } else {
                    RectHV r = new RectHV(rect.xMin(), rect.yMin(), x.p.x(), rect.yMax());
                    x.lb = put(x.lb, p, value, r, !lr);
                }
            } else {
                if (x.p.y() <= p.y()) {
                    RectHV r = new RectHV(rect.xMin(),  x.p.y(), rect.xMax(), rect.yMax());
                    x.rt = put(x.rt, p, value, r, !lr);
                } else {
                    RectHV r = new RectHV(rect.xMin(), rect.yMin(), rect.xMax(), x.p.y());
                    x.lb = put(x.lb, p, value, r, !lr);
                }
            }
        }
        // Return x.
        return x;
    }

    // Returns the value associated with the given point in the KdTree x, or null.
    private Value get(Node x, Point2D p, boolean lr) {
        // if x is null, return null.
        if (x == null) {
            return null;
        }
        // if point in x equals p, return the value of x.
        if (x.p.equals(p)) {
            return x.value;
        }
        // Check what coordinate is being considered,
        // Then compare the value of the points depending on the
        // compared coordinates, thus return the value at the specific subtree.
        if (lr) {
            if (x.p.x() <= p.x()) {
                return get(x.rt, p, !lr);
            } else {
                return get(x.lb, p, !lr);
            }
        } else {
            if (x.p.y() <= p.y()) {
                return get(x.rt, p, !lr);
            } else {
                return get(x.lb, p, !lr);
            }
        }
    }

    // Collects in the given queue all the points in the KdTree x that are inside rect.
    private void range(Node x, RectHV rect, LinkedQueue<Point2D> q) {
        // if x is null, return.
        if (x == null) {
            return;
        }
        // if the given rectangle contains the point in x, enqueue that point into
        // q.
        if (rect.contains(x.p)) {
            q.enqueue(x.p);
        }
        // Thus, make a recursive call to the left subtree and right subtree.
        range(x.lb, rect, q);
        range(x.rt, rect, q);

    }

    // Returns the point in the KdTree x that is closest to p, or null; nearest is the closest
    // point discovered so far.
    private Point2D nearest(Node x, Point2D p, Point2D nearest, boolean lr) {
        // Initialize a local variable for the nearest argument.
        Point2D near = nearest;
        // if x is null, return the near.
        if (x == null) {
            return near;
        }
        // Thus, have the two different distancesquare, the first one, is between point in x and p
        // the other is between point p and near. We must make sure near is not null.
        double d1 = x.p.distanceSquaredTo(p);
        double d2;
        if (near == null) {
            near = x.p;
        }
        d2 = p.distanceSquaredTo(near);
        // Then compare the two distances, if d1 is less than d2, set near to the point in x.
        if (!x.p.equals(p) && d1 < d2) {
            near = x.p;
        }
        // Pruning Rule,
        // Determine the first node to be left subtree, the second to be right subtree.
        Node first = x.lb;
        Node second = x.rt;
        // Make the comparison based on the coordinates and value of the point, and determine
        // which subtree is nearest.
        if (lr && x.p.x() <= p.x() || !lr && x.p.y() <= p.y()) {
            first = x.rt;
            second = x.lb;
        }
        // Then we need to make sure none of the subtrees is null, and so compare the distances of
        // the subtrees related to the given point compared to the node x's distance to p.
        // Make a recursive call to the nearest method with the appropriate values as arguments
        // and store it in near.
        if (first != null) {
            if (first.rect.distanceSquaredTo(p) < d1) {
                near = nearest(first, p, near, !lr);
                d1 = near.distanceSquaredTo(p);
            }
        }
        if (second != null) {
            if (second.rect.distanceSquaredTo(p) < d1) {
                near = nearest(second, p, near, !lr);
            }
        }
        // return near.
        return near;
    }

    // Collects in the given max-PQ up to k points from the KdTree x that are different from and
    // closest to p.
    private void nearest(Node x, Point2D p, int k, MaxPQ<Point2D> pq, boolean lr) {
        // if x is null and the size of pq is greater then k, return.
        if (x == null || pq.size() > k) {
            return;
        }
        // if point in x is different from p, insert that point in pq.
        if (!x.p.equals(p)) {
            pq.insert(x.p);
        }
        // if size of pq is greater than k, call pq.delMax().
        if (pq.size() > k) {
            pq.delMax();
        }
        // Make a recursive call in the left and right subtree of x, with the appropriate value.
        nearest(x.lb, p, k, pq, !lr);
        nearest(x.rt, p, k, pq, !lr);

    }

    // A representation of node in a KdTree in two dimensions (ie, a 2dTree). Each node stores a
    // 2d point (the key), a value, an axis-aligned rectangle, and references to the left/bottom
    // and right/top subtrees.
    private class Node {
        private Point2D p;   // the point (key)
        private Value value; // the value
        private RectHV rect; // the axis-aligned rectangle
        private Node lb;     // the left/bottom subtree
        private Node rt;     // the right/top subtree

        // Constructs a node given the point (key), the associated value, and the
        // corresponding axis-aligned rectangle.
        Node(Point2D p, Value value, RectHV rect) {
            this.p = p;
            this.value = value;
            this.rect = rect;
        }
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        KdTreePointST<Integer> st = new KdTreePointST<>();
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
