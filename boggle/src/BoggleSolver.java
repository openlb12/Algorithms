import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {
    private Tries dict;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        dict = new Tries(dictionary);
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        return null;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        int length = word.length();
        int score;
        if (length < 3) score = 0;
        else if (length < 5) score = 1;
        else if (length < 6) score = 2;
        else if (length < 7) score = 3;
        else if (length < 8) score = 5;
        else score = 11;
        return score;
    }

    private boolean isContain(String key) {
        return dict.isContain(key);
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dict = in.readAllStrings();
        BoggleSolver bgsolver = new BoggleSolver(dict);
        BoggleBoard bd = new BoggleBoard();
        Graph gph = new Graph(bd);
        StdOut.println(gph);
        while (!StdIn.isEmpty()) {
            String key = StdIn.readString();
            if (bgsolver.isContain(key)) StdOut.println(String.format("%s is contained", key));
            else StdOut.println(String.format("%s is not contained", key));
        }
    }
}

class Tries {
    private Node root;
    private int size;
    private final int LOC_START = (int) 'A';

    private class Node {
        private boolean isExisted;
        private Node[] childs;
        //    private int depth;
        private final int FULL_ALPHBET_SIZE = 26;

        Node() {
//        this.depth = depth;
            isExisted = false;
            childs = new Node[FULL_ALPHBET_SIZE];
        }

        void setExisted() {
            isExisted = true;
        }


    }

    public Tries() {
        size = 0;
        root = new Node();
    }

    public Tries(String[] dic) {
        this();
        for (String wd : dic) {
            put(wd);
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private Node put(Node x, String str, int depth) {
        if (x == null) {
            x = new Node();
        }
        if (depth + 1 < str.length()) {
            int loc = str.charAt(depth + 1) - LOC_START;
            x.childs[loc] = put(x.childs[loc], str, depth + 1);
        } else {
            x.setExisted();
        }
        return x;
    }

    public void put(String str) {
        if (str == null || str.isEmpty()) throw new IllegalArgumentException();
        str = str.toUpperCase();
        int loc = str.charAt(0) - LOC_START;
        root.childs[loc] = put(root.childs[loc], str, 0);
        size++;
    }

    private boolean get(Node x, String str, int depth) {
        if (x == null) return false;
        if (depth + 1 < str.length()) {
            int loc = str.charAt(depth + 1) - LOC_START;
            return get(x.childs[loc], str, depth + 1);
        } else {
            return x.isExisted;
        }
    }

    public Node get(Node x, char c) {
        if (x == null) return null;
        return null;
    }

    public boolean isContain(String str) {
        if (str == null || str.isEmpty()) throw new IllegalArgumentException();
        str = str.toUpperCase();
        int loc = str.charAt(0) - LOC_START;
        return get(root.childs[loc], str, 0);
    }
}

class Graph {
    private class Edge {
        private final int v;
        private final int w;

        public Edge(int v, int w) {
            this.v = v;
            this.w = w;
        }

        public int either() {
            return v;
        }

        public int other(int i) {
            if (i == v) return w;
            else if (i == w) return v;
            else throw new IllegalArgumentException();
        }

    }

    private final int VERTICE_SIZE;
    private int edgeNum;
    private Bag<Edge>[] adjacent;
    private final int ROW, COL;
    private BoggleBoard bd;
    private boolean[] marked;

    public Graph(int row, int col) {
        this.ROW = row;
        this.COL = col;
        this.VERTICE_SIZE = this.ROW * this.COL;
        bd = null;
        adjacent = (Bag<Edge>[]) new Bag[this.VERTICE_SIZE];
        for (int i = 0; i < this.VERTICE_SIZE; i++) {
            adjacent[i] = new Bag<Edge>();
        }
    }

    public Graph(BoggleBoard bd) {
        this(bd.rows(), bd.cols());
        this.bd = bd;
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                int bgn = i * COL + j;
                for (int end : neighbours(i, j)) {
                    if (end > bgn) {
                        Edge ed = new Edge(bgn, end);
                        adjacent[bgn].add(ed);
                        adjacent[end].add(ed);
                        edgeNum++;
                    }
                }
            }
        }

    }

    public Iterable<String> strings() {
        for (int v = 0; v < VERTICE_SIZE; v++) {

        }
        return null;
    }

    private Iterable<Integer> paths(int v) {
        return null;
    }

    private boolean isValid(int x, int y) {
        return (x < ROW && x >= 0) && (y < COL && y >= 0);
    }

    private Iterable<Integer> neighbours(int x, int y) {
        Stack<Integer> neigh = new Stack<Integer>();
        if (isValid(x - 1, y)) neigh.push((x - 1) * COL + y);
        if (isValid(x + 1, y)) neigh.push((x + 1) * COL + y);
        if (isValid(x - 1, y + 1)) neigh.push((x - 1) * COL + y + 1);
        if (isValid(x + 1, y + 1)) neigh.push((x + 1) * COL + y + 1);
        if (isValid(x - 1, y - 1)) neigh.push((x - 1) * COL + y - 1);
        if (isValid(x + 1, y - 1)) neigh.push((x + 1) * COL + y - 1);
        if (isValid(x, y + 1)) neigh.push(x * COL + y + 1);
        if (isValid(x, y - 1)) neigh.push(x * COL + y - 1);
        return neigh;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(String.format("%d rows, %d cols\n", ROW, COL));
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                int bgn = i * COL + j;
                str.append(String.format("%d, %d :", i, j));
                for (Edge ed : adjacent[bgn]) {
                    str.append(ed.other(bgn) + " ");
                }
                str.append("\n");
            }
        }
        return str.toString();
    }
}


