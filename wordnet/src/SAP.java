import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private final Digraph dgph;
    private final int synSize;
    private int length;


    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        dgph = new Digraph(G);
        synSize = G.V();
        length = -1;
    }

    private boolean isOutRange(int v) {
        if (v < 0 || v >= synSize) return true;
        return false;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (isOutRange(v) || isOutRange(w))
            throw new IllegalArgumentException();
        ancestor(v, w);
        return length;

    }

//    private Iterable<Integer> path() {
//        return path;
//    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (isOutRange(v) || isOutRange(w))
            throw new IllegalArgumentException();
        Stack<Integer> checkv = new Stack<Integer>();
        Stack<Integer> checkw = new Stack<Integer>();
        checkv.push(v);
        checkw.push(w);
        return ancestor(checkv, checkw);
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        ancestor(v, w);
        return length;
    }

    private boolean isInList(Iterable<Integer> v, int w) {
        for (int i : v) {
            if (i == w) return true;
        }
        return false;
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        Queue<Integer> checkpoints = new Queue<Integer>();
        boolean[] markedV = new boolean[synSize];
        int[] stepsV = new int[synSize];
        boolean[] markedW = new boolean[synSize];
        int[] stepsW = new int[synSize];
        length = Integer.MAX_VALUE;
        int ancestorId = -1;
        for (Integer i : v) {
            if (i == null || isOutRange(i)) throw new IllegalArgumentException();
            // todo: dealing repeating item in checklist
            checkpoints.enqueue(i);
            markedV[i] = true;
            stepsV[i] = 0;
        }
        while (!checkpoints.isEmpty()) {
            int apex = checkpoints.dequeue();
            for (int adj : dgph.adj(apex)) {
                if (markedV[adj]) continue;
                checkpoints.enqueue(adj);
                stepsV[adj] = stepsV[apex] + 1;
                markedV[adj] = true;
            }
        }

        for (Integer i : w) {
            if (i == null || isOutRange(i)) throw new IllegalArgumentException();
            // todo: dealing repeating item in checklist
            checkpoints.enqueue(i);
            markedW[i] = true;
            stepsW[i] = 0;
        }
        while (!checkpoints.isEmpty()) {
            int apex = checkpoints.dequeue();
            for (int adj : dgph.adj(apex)) {
                if (markedW[adj]) continue;
                checkpoints.enqueue(adj);
                stepsW[adj] = stepsW[apex] + 1;
                markedW[adj] = true;
            }
        }

        for (int i = 0; i < synSize; i++) {
            if (markedV[i] && markedW[i]) {
                if (stepsV[i] + stepsW[i] < length) {
                    length = stepsV[i] + stepsW[i];
                    ancestorId = i;
                }
            }
        }

        if (ancestorId == -1) {
            length = -1;
        }
        return ancestorId;
    }


    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            String[] v = StdIn.readLine().split(",\\s*|\\s+");
            String[] w = StdIn.readLine().split(",\\s*|\\s+");
            Stack<Integer> checkV = new Stack<Integer>();
            Stack<Integer> checkW = new Stack<Integer>();
            for (String tm : v) {
                checkV.push(Integer.parseInt(tm));
            }
            for (String tm : w) {
                checkW.push(Integer.parseInt(tm));
            }
            // int checkV = StdIn.readInt();
            // int checkW = StdIn.readInt();
            int length = sap.length(checkV, checkW);
            int ancestor = sap.ancestor(checkV, checkW);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
