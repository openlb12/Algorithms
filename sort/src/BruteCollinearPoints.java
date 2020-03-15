import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BruteCollinearPoints {
    // finds all line segments containing 4 points
    private final LineSegment[] lineSects;

    public BruteCollinearPoints(Point[] points) {
        Stack<LineSegment> lines = new Stack<LineSegment>();
        isPointsValidate(points);

        for (int i = 0; i < points.length - 3; i++) {
            for (int j = i + 1; j < points.length - 2; j++) {
                for (int k = j + 1; k < points.length - 1; k++) {
                    for (int m = k + 1; m < points.length; m++) {
                        if (Double.compare(points[i].slopeTo(points[j]), points[i].slopeTo(points[k])) == 0
                                && Double.compare(points[i].slopeTo(points[j]), points[i].slopeTo(points[m])) == 0) {
                            Point[] linePoints = {points[i], points[j], points[k], points[m]};
                            Arrays.sort(linePoints);
                            lines.push(new LineSegment(linePoints[linePoints.length - 1], linePoints[0]));
                        }
                    }
                }
            }
        }
        lineSects = new LineSegment[lines.size()];
        for (int i = lineSects.length - 1; i >= 0; i--) {
            lineSects[i] = lines.pop();
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        StdDraw.setPenRadius(0.005);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

}
