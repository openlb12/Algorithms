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

        Stack<LineSegment> lnStack = new Stack<LineSegment>();

//        Arrays.sort(points);


        for (int i = 0; i < points.length - LINE_POINTS_LIMIT + 1; i++) {
            Point[] neatPoints = Arrays.copyOfRange(points, i + 1, points.length);
            Point[] searchedPoints = Arrays.copyOfRange(points, 0, i);
            Arrays.sort(neatPoints, points[i].slopeOrder());
            if (searchedPoints.length > 0) {
                Arrays.sort(searchedPoints, points[i].slopeOrder());
                neatPoints = deleteRepeatLines(searchedPoints, points[i], neatPoints);
            }
            if (neatPoints.length >= LINE_POINTS_LIMIT - 1) {


                int idx = 0;
                while (idx < neatPoints.length - LINE_POINTS_LIMIT + 2) {
                    int idy = idx + LINE_POINTS_LIMIT - 2;
                    boolean isCollinear = false;
                    double slopeTest = points[i].slopeTo(neatPoints[idx]);

                    while (idy < neatPoints.length &&
                            Double.compare(slopeTest,
                                    points[i].slopeTo(neatPoints[idy])) == 0) {
                        isCollinear = true;
                        idy++;
                    }

                    if (isCollinear) {
                        Point minPoint = points[i];
                        Point maxPoint = points[i];
                        for (int iter = idx; iter < idy; iter++) {
                            if (maxPoint.compareTo(neatPoints[iter]) < 0) {
                                maxPoint = neatPoints[iter];
                            }
                            if (minPoint.compareTo(neatPoints[iter]) > 0) {
                                minPoint = neatPoints[iter];
                            }
                        }

                        lnStack.push(new LineSegment(minPoint, maxPoint));
                        idx = idy;

                    } else {
                        idx++;
                    }
                }

            }
        }
        lineSects = new LineSegment[lnStack.size()];
        for (int i = lineSects.length - 1; i >= 0; i--) {
            lineSects[i] = lnStack.pop();
        }
    }
//
//    // private class for online check
//    private class SpotLine {
//        Point bgn;
//        Point end;
//        double slope;
//
//        SpotLine(Point pb, Point pe) {
//            bgn = pb;
//            end = pe;
//            slope = pb.slopeTo(pe);
//        }
//
//        boolean checkOnline(Point pb, Point pe) {
//
//            if (Double.compare(pb.slopeTo(pe), slope) == 0) {
//                if (bgn == pb) {
//                    return true;
//                } else {
//                    return Double.compare(bgn.slopeTo(pb), slope) == 0;
//                }
//            }
//            return false;
//        }
//
//        double getSlope() {
//            return slope;
//        }
//    }

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

    // delete repeat lines
    private Point[] deleteRepeatLines(Point[] checkPoints, Point origin, Point[] pointsList) {
        Stack<Double> slopeList = new Stack<Double>();
        Stack<Point> neatPointsStack = new Stack<Point>();
        double testSlope = origin.slopeTo(checkPoints[0]);
        slopeList.push(testSlope);
        for (Point pt : checkPoints) {
            double tmpSlope = origin.slopeTo(pt);
            if (tmpSlope > testSlope) {
                slopeList.push(tmpSlope);
                testSlope = tmpSlope;
            }
        }
        for (Point pt : pointsList) {
            testSlope = origin.slopeTo(pt);
            boolean isRepeat = false;
            for (double slp : slopeList) {
                if (Double.compare(testSlope, slp) == 0) {
                    isRepeat = true;
                    break;
                }
            }
            if (!isRepeat) {
                neatPointsStack.push(pt);
            }
        }
        Point[] neatPoints = new Point[neatPointsStack.size()];
        for (int i = 0; i < neatPoints.length; i++) {
            neatPoints[i] = neatPointsStack.pop();
        }
        return neatPoints;

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



