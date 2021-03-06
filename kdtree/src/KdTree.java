import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private static final double PENRAD = 0.01;
    //    private static final boolean RED = true;
    //    private static final boolean BLACK = false;

    private class Node {

        private Point2D pt;
        private Node left, right;
        private int hierarchy;
        private int count;
        //        private boolean color;

        private Node(Point2D p, int h) {
            pt = p;
            hierarchy = h;
            count = 1;
        }

        int compareTo(Point2D cpt) {
            if (hierarchy % 2 == 0)
                return Point2D.X_ORDER.compare(this.pt, cpt);
            else
                return Point2D.Y_ORDER.compare(this.pt, cpt);
        }

        double distance(Point2D cpt) {
            if (hierarchy % 2 == 0)
                return Math.pow(pt.x() - cpt.x(), 2);
            else
                return Math.pow(pt.y() - cpt.y(), 2);
        }

        void draw(RectHV area) {
            if (pt == null || area == null) return;
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.filledCircle(pt.x(), pt.y(), PENRAD);
            //            StdDraw.text(pt.x(), pt.y(), pt.toString() + hierarchy);
            if (hierarchy % 2 == 0) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(pt.x(), area.ymin(), pt.x(), area.ymax());
            }
            else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(area.xmin(), pt.y(), area.xmax(), pt.y());

            }
        }

        RectHV[] split(RectHV rect) {
            //            if (!rect.contains(pt)) throw new IllegalArgumentException("Wrong rectangle in split");
            RectHV[] rectList = new RectHV[2];
            double x0, y0, x1, y1;
            double ndx, ndy;
            x0 = rect.xmin();
            y0 = rect.ymin();
            x1 = rect.xmax();
            y1 = rect.ymax();
            ndx = pt.x();
            ndy = pt.y();
            if (hierarchy % 2 == 0) {
                rectList[0] = new RectHV(x0, y0, ndx, y1);
                rectList[1] = new RectHV(ndx, y0, x1, y1);
            }
            else {
                rectList[0] = new RectHV(x0, y0, x1, ndy);
                rectList[1] = new RectHV(x0, ndy, x1, y1);
            }
            return rectList;
        }


        void draw() {
            if (hierarchy % 2 == 0) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.filledCircle(pt.x(), pt.y(), PENRAD);
                StdDraw.text(pt.x(), pt.y(), pt.toString() + hierarchy);
            }
            else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.filledCircle(pt.x(), pt.y(), PENRAD);
                StdDraw.text(pt.x(), pt.y(), pt.toString() + hierarchy);
            }
        }

        @Override
        public String toString() {
            return String.format("%5.3f %5.3f", pt.x(), pt.y());
        }
    }

    private Node root;

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
        if (x.pt.equals(p)) return x;
        int cmp = x.compareTo(p);
        if (cmp <= 0) x.right = insert(x.right, p, hi + 1);
        else x.left = insert(x.left, p, hi + 1);
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
        if (x.pt.equals(p)) return true;
        int cmp = x.compareTo(p);
        if (cmp <= 0) return contains(x.right, p);
        else return contains(x.left, p);
    }


    public boolean contains(Point2D p) {
        // does the set contain point p?
        if (p == null) throw new IllegalArgumentException(
                "point p is null in contains function");
        return contains(root, p);

    }


    private Queue<Node> nodes() {
        Stack<Node> st = new Stack<Node>();
        Queue<Node> key = new Queue<Node>();
        if (root == null) return key;
        st.push(root);
        while (!st.isEmpty()) {
            Node tmp = st.pop();
            key.enqueue(tmp);
            if (tmp.right != null) st.push(tmp.right);
            if (tmp.left != null) st.push(tmp.left);
        }
        return key;
    }


    public void draw() {
        // draw all points to standard draw
        if (root == null) return;
        Stack<Node> st = new Stack<Node>();
        Stack<RectHV> rect = new Stack<RectHV>();
        RectHV rootRect = new RectHV(0.0, 0.0, 1.0, 1.0);
        st.push(root);
        rect.push(rootRect);
        while (!st.isEmpty()) {
            Node tmp = st.pop();
            RectHV tmpRect = rect.pop();
            tmp.draw(tmpRect);
            RectHV[] tmpRectList = tmp.split(tmpRect);

            if (tmp.right != null) {
                st.push(tmp.right);
                rect.push(tmpRectList[1]);
            }
            if (tmp.left != null) {
                st.push(tmp.left);
                rect.push(tmpRectList[0]);
            }
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        // all points that are inside the rectangle (or on the boundary)
        if (rect == null) throw new IllegalArgumentException(
                "rect is null in range function");
        RectHV rootRect = new RectHV(0.0, 0.0, 1.0, 1.0);
        if (root == null || !rootRect.intersects(rect)) return null;
        Stack<Point2D> pts = new Stack<Point2D>();
        Stack<Node> st = new Stack<Node>();
        Stack<RectHV> rectHVS = new Stack<RectHV>();
        st.push(root);
        rectHVS.push(rootRect);

        //        root.draw(rootRect);
        while (!st.isEmpty()) {
            Node tmp = st.pop();
            RectHV tmpRect = rectHVS.pop();
            RectHV[] tmpRectList = tmp.split(tmpRect);
            if (rect.contains(tmp.pt)) pts.push(tmp.pt);
            if (tmp.right != null && rect.intersects(tmpRectList[1])) {
                st.push(tmp.right);
                rectHVS.push(tmpRectList[1]);
            }
            if (tmp.left != null && rect.intersects(tmpRectList[0])) {
                st.push(tmp.left);
                rectHVS.push(tmpRectList[0]);
            }
        }

        //        if (root == null) return;
        //        Stack<Node> st = new Stack<Node>();
        //        Stack<RectHV> rect = new Stack<RectHV>();
        //        while ()
        //            st.push(root);
        //        rect.push(rootRect);
        //        root.draw(rootRect);

        return pts;
    }


    private Point2D nearest(Node x, Point2D p, Point2D npt, RectHV rect) {
        //        StdOut.println(x);
        if (x == null || p == null || npt == null || rect == null) return null;
        double nptDistance = npt.distanceSquaredTo(p);
        if (nptDistance < rect.distanceSquaredTo(p)) return npt;
        if (nptDistance > x.pt.distanceSquaredTo(p)) {
            npt = x.pt;
            nptDistance = x.pt.distanceSquaredTo(p);
        }
        RectHV[] rectList = x.split(rect);
        int cmp = x.compareTo(p);
        Point2D tmpnpt = npt;
        double leftRectDis = rectList[0].distanceSquaredTo(p);
        double rigtRectDis = rectList[1].distanceSquaredTo(p);
        if (cmp <= 0 && rigtRectDis < nptDistance) {
            tmpnpt = nearest(x.right, p, npt, rectList[1]);
            if (tmpnpt == null) {
                if (leftRectDis < nptDistance) {
                    tmpnpt = nearest(x.left, p, npt, rectList[0]);
                }
            }
            else {
                if (tmpnpt.distanceSquaredTo(p) < nptDistance) {
                    nptDistance = tmpnpt.distanceSquaredTo(p);
                }
                else {
                    tmpnpt = npt;
                }
                if (leftRectDis < nptDistance) {
                    Point2D lp = nearest(x.left, p, tmpnpt, rectList[0]);
                    if (lp != null && lp.distanceSquaredTo(p) < nptDistance) {
                        tmpnpt = lp;
                    }
                }
            }
        }
        else if (cmp > 0 && leftRectDis < nptDistance) {
            tmpnpt = nearest(x.left, p, npt, rectList[0]);
            if (tmpnpt == null) {
                if (rigtRectDis < nptDistance) {
                    tmpnpt = nearest(x.right, p, npt, rectList[1]);
                }
            }
            else {
                if (tmpnpt.distanceSquaredTo(p) < nptDistance) {
                    nptDistance = tmpnpt.distanceSquaredTo(p);
                }
                else {
                    tmpnpt = npt;
                }
                if (rigtRectDis < nptDistance) {
                    Point2D rp = nearest(x.right, p, tmpnpt, rectList[1]);
                    if (rp != null && rp.distanceSquaredTo(p) < nptDistance) {
                        tmpnpt = rp;
                    }
                }
            }
        }
        if (tmpnpt == null || tmpnpt.distanceSquaredTo(p) > nptDistance) tmpnpt = npt;
        return tmpnpt;

    }

    private Point2D nearestUnrecurse(Point2D p) {
        if (p == null) throw new IllegalArgumentException(
                "point p is null in nearest function");
        // a nearest neighbor in the set to point p; null if the set is empty
        if (root == null) return null;
        RectHV rect = new RectHV(0.0, 0.0, 1.0, 1.0);
        Stack<Node> nds = new Stack<Node>();
        Stack<RectHV> rects = new Stack<RectHV>();
        Point2D npt = root.pt;
        double nptDistance = npt.distanceSquaredTo(p);
        nds.push(root);
        rects.push(rect);
        while (!nds.isEmpty()) {
            Node nd = nds.pop();
            //            StdOut.println(nd);
            rect = rects.pop();
            if (rect.distanceSquaredTo(p) >= nptDistance) continue;
            if (nd.pt.distanceSquaredTo(p) < nptDistance) {
                nptDistance = nd.pt.distanceSquaredTo(p);
                npt = nd.pt;
            }
            RectHV[] tmpRectList = nd.split(rect);
            int cmp = nd.compareTo(p);
            if (cmp <= 0) {
                if (nd.left != null && tmpRectList[0].distanceSquaredTo(p) < nptDistance) {
                    nds.push(nd.left);
                    rects.push(tmpRectList[0]);
                }
                if (nd.right != null && tmpRectList[1].distanceSquaredTo(p) < nptDistance) {
                    nds.push(nd.right);
                    rects.push(tmpRectList[1]);
                }
            }
            else {
                if (nd.right != null && tmpRectList[1].distanceSquaredTo(p) < nptDistance) {
                    nds.push(nd.right);
                    rects.push(tmpRectList[1]);
                }
                if (nd.left != null && tmpRectList[0].distanceSquaredTo(p) < nptDistance) {
                    nds.push(nd.left);
                    rects.push(tmpRectList[0]);
                }

            }
        }
        return npt;

    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException(
                "point p is null in nearest function");
        if (root == null) return null;
        return nearest(root, p, root.pt, new RectHV(0.0, 0.0, 1.0, 1.0));
    }


    public static void main(String[] args) {
        // unit testing of the methods (optional)
        In in = new In(args[0]);
        double queryx = Double.parseDouble(args[1]);
        double queryy = Double.parseDouble(args[2]);
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }
        RectHV rect = new RectHV(0.1, 0.2, 0.5, 0.7);
        StdDraw.clear();
        rect.draw();
        kdtree.draw();
        StdDraw.show();


        Point2D checkpoint = new Point2D(queryx, queryy);
        StdDraw.setPenColor(StdDraw.GRAY);
        StdDraw.filledCircle(checkpoint.x(), checkpoint.y(), PENRAD);

        Point2D npt = kdtree.nearest(checkpoint);
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.filledCircle(npt.x(), npt.y(), PENRAD);

        for (Point2D rpt : kdtree.range(rect)) {
            StdDraw.filledCircle(rpt.x(), rpt.y(), PENRAD);
        }

        StdDraw.show();
    }
}
