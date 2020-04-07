import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Stack;

public class PointSET {
    private SET<Point2D> set;

    public PointSET() {
        // construct an empty set of points
        set = new SET<Point2D>();
    }

    public boolean isEmpty() {
        // is the set empty?
        return set.isEmpty();
    }

    public int size() {
        // number of points in the set
        return set.size();
    }

    public void insert(Point2D p) {
        // add the point to the set (if it is not already in the set)
        if (p == null) throw new IllegalArgumentException(
                "point p is null in insert function");
        if (!set.contains(p)) set.add(p);
    }

    public boolean contains(Point2D p) {
        // does the set contain point p?
        if (p == null) throw new IllegalArgumentException(
                "point p is null in contains function");
        return set.contains(p);
    }

    public void draw() {
        // draw all points to standard draw
        for (Point2D p : set) {
//            StdDraw.setPenRadius(0.1);
//            StdDraw.point(p.x(), p.y());
            p.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        // all points that are inside the rectangle (or on the boundary)
        if (rect == null) throw new IllegalArgumentException(
                "rect is null in range function");
        Stack<Point2D> stack = new Stack<Point2D>();
        for (Point2D p : set) {
            if (rect.contains(p)) stack.push(p);
        }
        return stack;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException(
                "point p is null in nearest function");
        if (isEmpty()) return null;
        // a nearest neighbor in the set to point p; null if the set is empty
        Point2D nearestPoint = set.min();
        double minDist = nearestPoint.distanceSquaredTo(p);
        for (Point2D q : set) {
            double distance = q.distanceSquaredTo(p);
            if (distance < minDist) {
                minDist = distance;
                nearestPoint = q;
            }
        }
        return nearestPoint;

    }

    public static void main(String[] args) {
        // unit testing of the methods (optional)
    }
}
