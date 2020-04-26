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
    private int traceSize;


    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();
        pic = new Picture(picture);
        width = pic.width();
        height = pic.height();
        traceSize = width * height + 2;
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
        return new Picture(pic);
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

    private Iterable<Integer> neighbours_h(int x, int y) {
        Stack<Integer> neighbours = new Stack<Integer>();
        int p = x + 1 + y * width;
        if (height > 1) {
            if (x == width - 1) {
                neighbours.push(traceSize - 1);
            } else if (y == 0) {
                neighbours.push(p + 1);
                neighbours.push(p + 1 + width);
            } else if (y == height - 1) {
                neighbours.push(p + 1 - width);
                neighbours.push(p + 1);
            } else {
                neighbours.push(p + 1 - width);
                neighbours.push(p + 1);
                neighbours.push(p + 1 + width);
            }
        } else {
            neighbours.push(p + 1);
        }
        return neighbours;
    }


    private Iterable<Integer> neighbours_v(int x, int y) {
        Stack<Integer> neighbours = new Stack<Integer>();
        int p = x + 1 + y * width;

        if (width > 1) {
            if (y == height - 1) {
                neighbours.push(traceSize - 1);
            } else if (x == 0) {
                neighbours.push(p + width);
                neighbours.push(p + 1 + width);
            } else if (x == width - 1) {
                neighbours.push(p + width);
                neighbours.push(p - 1 + width);
            } else {
                neighbours.push(p + width);
                neighbours.push(p - 1 + width);
                neighbours.push(p + 1 + width);
            }
        } else {
            neighbours.push(p + width);
        }

        return neighbours;
    }


    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        int[] seam = new int[width];
        int[] edgeFrom = new int[traceSize];
        double[] distanceTo = new double[traceSize];
        for (int p = 0; p < traceSize; p++) {
            distanceTo[p] = Double.POSITIVE_INFINITY;
        }
        edgeFrom[0] = 0;
        distanceTo[0] = 0.0;
        for (int p = 0; p < height; p++) {
            edgeFrom[p * width + 1] = 0;
            distanceTo[p * width + 1] = energy(0, p);
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int p = 1 + y * width + x;
                for (int np : neighbours_h(x, y)) {
                    try {
                        int nx = (np - 1) % width;
                        int ny = (np - 1) / width;
                        if (distanceTo[np] > distanceTo[p] + energy[nx][ny]) {
                            distanceTo[np] = distanceTo[p] + energy[nx][ny];
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
        }


        int apex = edgeFrom[traceSize - 1];
        for (int p = width - 1; p >= 0; p--) {
            seam[p] = (apex - 1) / width;
            apex = edgeFrom[apex];
        }

        return seam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int[] seam = new int[height];
        int[] edgeFrom = new int[traceSize];
        double[] distanceTo = new double[traceSize];
        for (int p = 0; p < traceSize; p++) {
            distanceTo[p] = Double.POSITIVE_INFINITY;
        }
        edgeFrom[0] = 0;
        distanceTo[0] = 0.0;

        for (int x = 0; x < width; x++) {
            edgeFrom[x + 1] = 0;
            distanceTo[x + 1] = energy(x, 0);
        }
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = 1 + y * width + x;
                for (int np : neighbours_v(x, y)) {
                    try {
                        int nx = (np - 1) % width;
                        int ny = (np - 1) / width;
                        if (distanceTo[np] > distanceTo[p] + energy[nx][ny]) {
                            distanceTo[np] = distanceTo[p] + energy[nx][ny];
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
        }

        int apex = edgeFrom[traceSize - 1];
        for (int p = height - 1; p >= 0; p--) {
            seam[p] = (apex - 1) % width;
            apex = edgeFrom[apex];
        }

        return seam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        boolean isSeamable = (seam != null) && (seam.length == width) && (height > 1);
        if (!isSeamable) throw new IllegalArgumentException();
        boolean isValiedSeam = isValidY(seam[0]);
        for (int i = 1; i < width; i++) {
            if (!isValidY(seam[i]) || Math.abs(seam[i] - seam[i - 1]) > 1) {
                isValiedSeam = false;
                break;
            }
        }
        if (!isValiedSeam) throw new IllegalArgumentException();
        height -= 1;
        Picture rpic = new Picture(width, height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < seam[x]; y++) {
                rpic.setRGB(x, y, pic.getRGB(x, y));
            }
            for (int y = seam[x] + 1; y < pic.height(); y++) {
                rpic.setRGB(x, y - 1, pic.getRGB(x, y));
            }
        }
        pic = rpic;
        traceSize = width * height + 2;
        energy = new double[width][];
        for (int w = 0; w < width; w++) {
            energy[w] = new double[height];
            for (int h = 0; h < height; h++) {
                energy[w][h] = pix_energy(w, h);
            }
        }
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        boolean isSeamable = (seam != null) && (seam.length == height) && (width > 1);
        if (!isSeamable) throw new IllegalArgumentException();
        boolean isValiedSeam = isValidX(seam[0]);
        for (int i = 1; i < height; i++) {
            if (!isValidX(seam[i]) || Math.abs(seam[i] - seam[i - 1]) > 1) {
                isValiedSeam = false;
                break;
            }
        }
        if (!isValiedSeam) throw new IllegalArgumentException();
        width -= 1;
        Picture rpic = new Picture(width, height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < seam[y]; x++) {
                rpic.setRGB(x, y, pic.getRGB(x, y));
            }
            for (int x = seam[y] + 1; x < pic.width(); x++) {
                rpic.setRGB(x - 1, y, pic.getRGB(x, y));
            }
        }
        pic = rpic;
        traceSize = width * height + 2;
        energy = new double[width][];
        for (int w = 0; w < width; w++) {
            energy[w] = new double[height];
            for (int h = 0; h < height; h++) {
                energy[w][h] = pix_energy(w, h);
            }
        }
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
