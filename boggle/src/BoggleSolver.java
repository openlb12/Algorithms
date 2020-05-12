import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {
    private Tries dict;
    private final int LOC_START;
    private Graph gph;
    private BoggleBoard bd;
    private RedBlackBST<String, Integer> strings;


    private class Graph {

        private final int VERTICE_SIZE;
        private int edgeNum;
        private BagInt[] adjacent;
        private final int ROW, COL;
        private boolean[] marked;
        private Stack<Integer> path;

        class BagInt extends Bag<Integer> {
        }


        public Graph(int row, int col) {
            this.ROW = row;
            this.COL = col;
            this.VERTICE_SIZE = this.ROW * this.COL;
            path = null;
            adjacent = new BagInt[this.VERTICE_SIZE];

//            adjacent = (Bag<Integer>[]) new Bag[this.VERTICE_SIZE];
//            adjacent = new Bag[this.VERTICE_SIZE];
            for (int i = 0; i < this.VERTICE_SIZE; i++) {
                adjacent[i] = new BagInt();
            }
        }

        public Graph(BoggleBoard bd) {
            this(bd.rows(), bd.cols());
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COL; j++) {
                    int bgn = i * COL + j;
                    for (int end : neighbours(i, j)) {
                        if (end > bgn) {
                            adjacent[bgn].add(end);
                            adjacent[end].add(bgn);
                            edgeNum++;
                        }
                    }
                }
            }

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
    }

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        dict = new Tries(dictionary);
        LOC_START = dict.getLOC_START();
        this.gph = null;
        this.bd = null;
        this.strings = null;
    }

    private char getChar(int v) {
        if (v < gph.VERTICE_SIZE) {
            return bd.getLetter(v / gph.COL, v % gph.COL);
        } else {
            return 'U';
        }
    }

    private void path(Tries.Node nd, int vortex) {

        if (gph.path.size() >= 3 && nd.isExisted()) {
            StringBuilder str = new StringBuilder();
            for (int i : gph.path) {
                str.append(getChar(i));
            }
            String key = str.reverse().toString();
            this.strings.put(key, scoreOf(key));
        }
        for (int end : gph.adjacent[vortex]) {
            char ch = bd.getLetter(end / gph.COL, end % gph.COL);
            int loc = ch - LOC_START;
            Tries.Node next_nd = nd.getChild(loc);
            if (!gph.marked[end] && next_nd != null && ch == 'Q') {
                next_nd = next_nd.getChild('U' - LOC_START);
                if (next_nd != null) {
                    gph.path.push(end);
                    gph.path.push(gph.VERTICE_SIZE);
                    gph.marked[end] = true;
                    path(next_nd, end);
                }
            } else if (!gph.marked[end] && next_nd != null) {
                gph.path.push(end);
                gph.marked[end] = true;
                path(next_nd, end);
            }
        }

        int pt = gph.path.pop();
        if (pt == gph.VERTICE_SIZE) gph.path.pop();
        gph.marked[vortex] = false;
    }

//    private Iterable<String> paths(int v, BoggleBoard board) {
//        this.bd = board;
//        this.strings = new Stack<String>();
//        if (gph == null || gph.ROW != bd.rows() || gph.COL != bd.cols()) {
//            this.gph = new Graph(board);
//        }
//        gph.marked = new boolean[gph.VERTICE_SIZE];
//        gph.path = new Stack<Integer>();
//        gph.path.push(v);
//        gph.marked[v] = true;
//        path(v, 0);
//        return this.strings;
//    }

    private void paths(int v) {
        gph.marked = new boolean[gph.VERTICE_SIZE];
        gph.path = new Stack<Integer>();
        gph.path.push(v);
        gph.marked[v] = true;

        char letter = getChar(v);
        int loc = letter - this.LOC_START;
        Tries.Node nd = dict.getRootNode(loc);
        if (letter == 'Q') {
            nd = nd.getChild('U' - LOC_START);
            gph.path.push(gph.VERTICE_SIZE);
        }

        if (nd != null) {
            path(nd, v);
        }
    }


    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        this.bd = board;
        if (gph == null || gph.ROW != bd.rows() || gph.COL != bd.cols()) {
            this.gph = new Graph(board);
        }
        this.strings = new RedBlackBST<String, Integer>();

        for (int i = 0; i < this.bd.rows(); i++) {
            for (int j = 0; j < this.bd.cols(); j++) {
                paths(i * this.bd.cols() + j);
            }
        }

        return this.strings.keys();
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        int score = 0;
        int length = word.length();
        if (length > 2 && dict.isContain(word)) {
            if (length < 5) score = 1;
            else if (length < 6) score = 2;
            else if (length < 7) score = 3;
            else if (length < 8) score = 5;
            else score = 11;
        }
        return score;
    }

    private boolean isContain(String key) {
        return dict.isContain(key);
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dict = in.readAllStrings();
        BoggleSolver bgsolver = new BoggleSolver(dict);
        BoggleBoard bd = new BoggleBoard(args[1]);

        StdOut.println(bd);
        int score = 0;
        for (String str : bgsolver.getAllValidWords(bd)) {
            StdOut.println(str);
            score += bgsolver.scoreOf(str);

        }
        StdOut.println(score);
        while (!StdIn.isEmpty()) {
            String key = StdIn.readString();
            if (bgsolver.isContain(key)) {
                StdOut.println(String.format("%s is contained, socre is %d", key, bgsolver.scoreOf(key)));
            } else StdOut.println(String.format("%s is not contained", key));
        }
    }
}

class Tries {
    private Node root;
    private int size;
    private final int LOC_START = (int) 'A';

    class Node {
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

        boolean isExisted() {
            return isExisted;
        }

        Node getChild(int loc) {
            return childs[loc];
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

    public Node getRootNode(int loc) {
        return root.childs[loc];
    }

    public int getLOC_START() {
        return LOC_START;
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
//        if (str == null || str.isEmpty()) throw new IllegalArgumentException();
//        str = str.toUpperCase();
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
