import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.awt.Color;

public class SeamCarver {
    private static final double BOUNDARYENERGY = 1000.0;
    private Picture pic;
    private int width;
    private int height;
    private double[][] energy;


    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        pic = new Picture(picture);
        width = pic.width();
        height = pic.height();
        energy = new double[width][];
        for (int w = 0; w < width; w++) {
            energy[w] = new double[height];
            for (int h = 0; h < height; h++) {
                energy[w][h] = pix_energy(w, h);
            }
        }

    }

    // current picture
    public Picture picture() {
        return pic;
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    private boolean isValidX(int x) {
        return (x < width && x >= 0);
    }

    private boolean isValidY(int y) {
        return (y < height && y >= 0);
    }

    private boolean isBoundaryX(int x) {
        return (x == 0 || x == width - 1);
    }

    private boolean isBoundaryY(int y) {
        return (y == 0 || y == height - 1);
    }

    private double gradColor(Color a, Color b) {
        double delSqare = Math.pow((double) (a.getGreen() - b.getGreen()), 2.0);
        delSqare += Math.pow((double) (a.getBlue() - b.getBlue()), 2.0);
        delSqare += Math.pow((double) (a.getRed() - b.getRed()), 2.0);
        return delSqare;
    }

    private double pix_energy(int x, int y) {
        if (isBoundaryX(x) || isBoundaryY(y)) return BOUNDARYENERGY;
        return Math.sqrt(gradColor(pic.get(x - 1, y), pic.get(x + 1, y))
                + gradColor(pic.get(x, y - 1), pic.get(x, y + 1)));
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (!(isValidX(x) && isValidY(y))) throw new IllegalArgumentException();
        return energy[x][y];
    }

    private Iterable<Integer> neighbours_h(int p) {
        Stack<Integer> neighbours = new Stack<Integer>();
        int y = (p - 1) / width;
        int x = (p - 1) % width;
        if (p == 0) {
            for (int h = 0; h < height; h++) {
                neighbours.push(h * width + 1);
            }
        } else if (p == width * height + 1) {
            return neighbours;
        } else {
            if (x == width - 1) {
                neighbours.push(width * height + 1);
            } else if (isBoundaryY(y)) {
                if (y == 0) {
                    neighbours.push(p + 1);
                    neighbours.push(p + 1 + width);
                } else {
                    neighbours.push(p + 1);
                    neighbours.push(p + 1 - width);
                }
            } else {
                neighbours.push(p + 1 - width);
                neighbours.push(p + 1);
                neighbours.push(p + 1 + width);
            }

        }
        return neighbours;
    }

    private void dfs_h(Stack<Integer> top, boolean[] marked, int p) {
        marked[p] = true;
        for (int np : neighbours_h(p)) {
            if (!marked[np]) dfs_h(top, marked, np);
        }
        top.push(p);
    }

    private Iterable<Integer> topologcial_h() {
        Stack<Integer> top = new Stack<Integer>();
        boolean[] marked = new boolean[width * height + 2];
        dfs_h(top, marked, 0);
        return top;
    }

    private Iterable<Integer> neighbours_v(int p) {
        Stack<Integer> neighbours = new Stack<Integer>();
        int y = (p - 1) / width;
        int x = (p - 1) % width;
        if (p == 0) {
            for (int h = 0; h < width; h++) {
                neighbours.push(h + 1);
            }
        } else if (p == width * height + 1) {
            return neighbours;
        } else {
            if (y == height - 1) {
                neighbours.push(width * height + 1);
            } else if (isBoundaryX(x)) {
                if (x == 0) {
                    neighbours.push(p + width);
                    neighbours.push(p + 1 + width);
                } else {
                    neighbours.push(p + width);
                    neighbours.push(p - 1 + width);
                }
            } else {
                neighbours.push(p - 1 + width);
                neighbours.push(p + width);
                neighbours.push(p + 1 + width);
            }
        }
        return neighbours;
    }

    private void dfs_v(Stack<Integer> top, boolean[] marked, int p) {
        marked[p] = true;

        for (int np : neighbours_v(p)) {
            if (!marked[np]) dfs_h(top, marked, np);
        }
        top.push(p);
    }

    private Iterable<Integer> topologcial_v() {
        Stack<Integer> top = new Stack<Integer>();
        boolean[] marked = new boolean[width * height + 2];
        dfs_v(top, marked, 0);
        return top;
    }


    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        int[] seam = new int[width];
        final int mapSize = width * height + 2;
        int[] edgeFrom = new int[mapSize];
        double[] distanceTo = new double[mapSize];
        for (int p = 0; p < mapSize; p++) {
            distanceTo[p] = Double.POSITIVE_INFINITY;
        }
        edgeFrom[0] = 0;
        distanceTo[0] = 0.0;
        for (int p : topologcial_h()) {
            for (int np : neighbours_h(p)) {
                try {
                    int x = (np - 1) % width;
                    int y = (np - 1) / width;
                    if (distanceTo[np] > distanceTo[p] + energy[x][y]) {
                        distanceTo[np] = distanceTo[p] + energy[x][y];
                        edgeFrom[np] = p;
                    }
                } catch (IndexOutOfBoundsException e) {
                    if (distanceTo[np] > distanceTo[p]) {
                        distanceTo[np] = distanceTo[p];
                        edgeFrom[np] = p;
                    }
                }
            }
        }

        int apex = edgeFrom[height * width + 1];
        for (int p = width - 1; p >= 0; p--) {
            seam[p] = (edgeFrom[apex] - 1) / width;
            apex = edgeFrom[apex];
        }

        return seam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int[] seam = new int[height];
        final int mapSize = width * height + 2;
        int[] edgeFrom = new int[mapSize];
        double[] distanceTo = new double[mapSize];
        for (int p = 0; p < mapSize; p++) {
            distanceTo[p] = Double.POSITIVE_INFINITY;
        }
        edgeFrom[0] = 0;
        distanceTo[0] = 0.0;
        for (int p : topologcial_v()) {
            for (int np : neighbours_v(p)) {
                try {
                    int x = (np - 1) % width;
                    int y = (np - 1) / width;
                    if (distanceTo[np] > distanceTo[p] + energy[x][y]) {
                        distanceTo[np] = distanceTo[p] + energy[x][y];
                        edgeFrom[np] = p;
                    }
                } catch (IndexOutOfBoundsException e) {
                    if (distanceTo[np] > distanceTo[p]) {
                        distanceTo[np] = distanceTo[p];
                        edgeFrom[np] = p;
                    }
                }
            }
        }

        int apex = edgeFrom[height * width + 1];
        for (int p = height - 1; p >= 0; p--) {
            seam[p] = (edgeFrom[apex] - 1) % width;
            apex = edgeFrom[apex];
        }

        return seam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException();
    }

    //  unit testing (optional)
    public static void main(String[] args) {
        Picture picture = new Picture(args[0]);
        StdOut.printf("image is %d pixels wide by %d pixels high.\n", picture.width(), picture.height());

        SeamCarver sc = new SeamCarver(picture);

        StdOut.printf("Printing energy calculated for each pixel.\n");

        for (int row = 0; row < sc.height(); row++) {
            for (int col = 0; col < sc.width(); col++)
                StdOut.printf("%9.0f ", sc.energy(col, row));
            StdOut.println();
        }


    }
}
