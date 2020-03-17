import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class FastCollinearPoints {
    // finds all line segments containing 4 points

    private static final int LINE_POINTS_LIMIT = 4;
    private final LineSegment[] lineSects;

    public FastCollinearPoints(Point[] points) {
        isPointsValidate(points);

        Stack<SpotLine> lnStack = new Stack<SpotLine>();
//        Arrays.sort(points);


        for (int i = 0; i < points.length - LINE_POINTS_LIMIT + 1; i++) {
            Point[] searchPoints = Arrays.copyOfRange(points, i, points.length);
            Arrays.sort(searchPoints, points[i].slopeOrder());
            int idx = 1;
            while (idx < searchPoints.length - LINE_POINTS_LIMIT + 2) {
                int idy = idx + LINE_POINTS_LIMIT - 2;
                boolean isCollinear = false;
                double slopeTest = searchPoints[0].slopeTo(searchPoints[idx]);


                while (idy < searchPoints.length &&
                        Double.compare(slopeTest,
                                searchPoints[0].slopeTo(searchPoints[idy])) == 0) {
                    isCollinear = true;
                    idy++;
                }

                if (isCollinear) {
                    Point minPoint = searchPoints[0];
                    Point maxPoint = searchPoints[0];
                    for (int iter = idx; iter < idy; iter++) {
                        if (maxPoint.compareTo(searchPoints[iter]) < 0) {
                            maxPoint = searchPoints[iter];
                        }
                        if (minPoint.compareTo(searchPoints[iter]) > 0) {
                            minPoint = searchPoints[iter];
                        }
                    }

                    boolean isRepeated = false;
                    for (SpotLine sln : lnStack) {
                        if (sln.checkOnline(minPoint, maxPoint)) {
                            isRepeated = true;
                            break;
                        }
                    }

                    if (!isRepeated) {
                        lnStack.push(new SpotLine(minPoint, maxPoint));
                    }
                    idx = idy;

                } else {
                    idx++;
                }
            }

        }
        lineSects = new LineSegment[lnStack.size()];
        for (int i = lineSects.length - 1; i >= 0; i--) {
            SpotLine spl = lnStack.pop();
            lineSects[i] = new LineSegment(spl.bgn, spl.end);
        }
    }

    // private class for online check
    private class SpotLine {
        Point bgn;
        Point end;
        double slope;

        SpotLine(Point pb, Point pe) {
            bgn = pb;
            end = pe;
            slope = pb.slopeTo(pe);
        }

        boolean checkOnline(Point pb, Point pe) {

            if (Double.compare(pb.slopeTo(pe), slope) == 0) {
                if (bgn == pb) {
                    return Double.compare(bgn.slopeTo(pe), slope) == 0;
                } else {
                    return Double.compare(bgn.slopeTo(pb), slope) == 0;
                }
            }
            return false;
        }

        double getSlope() {
            return slope;
        }
    }

    // check the validity of constructor points
    private void isPointsValidate(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Illeagal null points");
        }
        for (Point ipt : points) {
            if (ipt == null) throw new IllegalArgumentException("Illeagal null element in points");
        }
        for (int i = 0; i < points.length - 1; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException("Illeagal repeating element in points");
                }
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return lineSects.length;
    }


    // the line segments
    public LineSegment[] segments() {
        return lineSects.clone();
    }


    // test BruteCollinearPoints
    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.setPenRadius(0.02);
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        StdDraw.setPenRadius(0.005);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}



