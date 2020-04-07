import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;

public class KdTree {

    private class Node {
        private Point2D pt;
        private Node left, right;
        private int hierarchy;
        private int count;

        public Node(Point2D p, int h) {
            pt = p;
            hierarchy = h;
            count = 1;
        }

        public int compareTo(Point2D o) {
            if (hierarchy / 2 == 0)
                return Point2D.Y_ORDER.compare(this.pt, o);
            else
                return Point2D.Y_ORDER.compare(this.pt, o);
        }

        public double distance(Point2D o) {
            if (hierarchy / 2 == 0)
                return Math.abs(pt.y() - o.y());
            else
                return Math.abs(pt.x() - o.x());
        }
    }

    Node root;

    public KdTree() {
        // construct an empty set of points
        root = null;
    }

    public boolean isEmpty() {
        // is the set empty?
        return root == null;
    }

    public int size() {
        // number of points in the set
        return size(root);
    }

    private int size(Node x) {
        if (x == null) return 0;
        return x.count;
    }

    private Node insert(Node x, Point2D p, int hi) {
        if (x == null) return new Node(p, hi);
        int cmp = x.compareTo(p);
        if (cmp < 0) x.right = insert(x.right, p, hi + 1);
        else if (cmp > 0) x.left = insert(x.left, p, hi + 1);
        else throw new IllegalArgumentException("Point is on the line");
        x.count = 1 + size(x.left) + size(x.right);
        return x;
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException(
                "point p is null in insert function");
        // add the point to the set (if it is not already in the set)
        root = insert(root, p, 0);
    }

    private boolean contains(Node x, Point2D p) {
        if (x == null) return false;
        int cmp = x.compareTo(p);
        if (cmp < 0) return contains(x.right, p);
        else if (cmp > 0) return contains(x.left, p);
        else return x.pt.equals(p);
    }


    public boolean contains(Point2D p) {
        // does the set contain point p?
        if (p == null) throw new IllegalArgumentException(
                "point p is null in contains function");
        return contains(root, p);

    }

    public void draw() {
        // draw all points to standard draw

    }

}

    public Iterable<Point2D> range(RectHV rect) {
        // all points that are inside the rectangle (or on the boundary)
        if (rect == null) throw new IllegalArgumentException(
                "rect is null in range function");
        Stack<Point2D> stack = new Stack<Point2D>();

        return stack;
    }

    private Point2D nearest(Node x, Point2D p) {
        if (x == null) return null;
        int cmp = x.compareTo(p);
        double distance = x.distance(p);
        if (cmp < 0) {
            Point2D rp = nearest(x.right, p);
            if (rp.distanceTo(p) < distance) {
                return rp;
            } else {
                Point2D lp = nearest(x.left, p);
                if (p.distanceTo(rp) < p.distanceTo(lp)) {
                    return rp;
                } else {
                    return lp;
                }
            }
        } else if (cmp > 0) {
            Point2D lp = nearest(x.left, p);
            if (lp.distanceTo(p) < distance) {
                return lp;
            } else {
                Point2D rp = nearest(x.right, p);
                if (p.distanceTo(rp) < p.distanceTo(lp)) {
                    return rp;
                } else {
                    return lp;
                }
            }

        } else {
            Point2D lp = nearest(x.left, p);
            Point2D rp = nearest(x.right, p);
            double dis = x.pt.distanceTo(p);
            if (lp.distanceTo(p) < rp.distanceTo(p)) {
                if (lp.distanceTo(p) < dis) return lp;
                else return x.pt;
            } else {
                if (rp.distanceTo(p) < dis) return rp;
                else return x.pt;
            }
        }


    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException(
                "point p is null in nearest function");
        // a nearest neighbor in the set to point p; null if the set is empty
        return nearest(root, p);

    }

    public static void main(String[] args) {
        // unit testing of the methods (optional)
    }
}
