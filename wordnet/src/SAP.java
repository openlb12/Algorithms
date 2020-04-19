import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private final Digraph dgph;
    private final int synSize;
    private Stack<Integer> path;


    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        dgph = new Digraph(G);
        DirectedCycle cgph = new DirectedCycle(G);
        synSize = G.V();
        path = null;
    }

    private boolean isOutRange(int v) {
        if (v < 0 || v >= synSize) return true;
        return false;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (isOutRange(v) || isOutRange(w))
            throw new IllegalArgumentException();
        if (ancestor(v, w) == -1) return -1;
        else return path.size() - 1;

    }

    private Iterable<Integer> path() {
        return path;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (isOutRange(v) || isOutRange(w))
            throw new IllegalArgumentException();
        // if (w == v) {
        //     path = new Stack<Integer>();
        //     path.push(w);
        //     return v;
        // }
        // boolean[] marked = new boolean[dgph.V()];
        // int[] edgeTo = new int[dgph.V()];
        // Queue<Integer> checkpoints = new Queue<Integer>();
        // checkpoints.enqueue(w);
        // checkpoints.enqueue(v);
        // marked[w] = true;
        // edgeTo[w] = w;
        // marked[v] = true;
        // edgeTo[v] = v;
        // while (!checkpoints.isEmpty()) {
        //     int apex = checkpoints.dequeue();
        //     for (int adj : dgph.adj(apex)) {
        //         if (marked[adj]) {
        //             int tmp1 = adj;
        //             path = new Stack<Integer>();
        //             Stack<Integer> reverse = new Stack<Integer>();
        //             reverse.push(tmp1);
        //             while (edgeTo[tmp1] != tmp1) {
        //                 tmp1 = edgeTo[tmp1];
        //                 reverse.push(tmp1);
        //             }
        //             while (!reverse.isEmpty()) {
        //                 path.push(reverse.pop());
        //             }
        //             int tmp2 = apex;
        //             path.push(tmp2);
        //             while (edgeTo[tmp2] != tmp2) {
        //                 tmp2 = edgeTo[tmp2];
        //                 path.push(tmp2);
        //             }
        //             if ((tmp1 == v && tmp2 == w) || (tmp1 == w && tmp2 == v))
        //                 return adj;
        //         }
        //         else {
        //             checkpoints.enqueue(adj);
        //             edgeTo[adj] = apex;
        //             marked[adj] = true;
        //         }
        //     }
        // }
        // path = null;
        // return -1;
        Stack<Integer> checkv = new Stack<Integer>();
        Stack<Integer> checkw = new Stack<Integer>();
        checkv.push(v);
        checkw.push(w);
        return ancestor(checkv, checkw);
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        if (ancestor(v, w) == -1) return -1;
        return path.size() - 1;
    }

    private boolean isInList(Iterable<Integer> v, int w) {
        for (int i : v) {
            if (i == w) return true;
        }
        return false;
    }

    private Stack<Integer> ancestorSearch(Iterable<Integer> v, Iterable<Integer> w) {
        Queue<Integer> checkpoints = new Queue<Integer>();
        Stack<Integer> searchedPath;
        boolean[] marked = new boolean[synSize];
        int[] edgeTo = new int[synSize];
        for (Integer i : v) {
            if (i == null || isOutRange(i)) throw new IllegalArgumentException();
            // todo: dealing repeating item in checklist
            checkpoints.enqueue(i);
            marked[i] = true;
            edgeTo[i] = i;
        }
        for (Integer i : w) {
            if (i == null || isOutRange(i)) throw new IllegalArgumentException();
            if (marked[i]) {
                // todo: dealing repeating item in checklist
                searchedPath = new Stack<Integer>();
                searchedPath.push(i);
                searchedPath.push(i);
                return searchedPath;
            }
            checkpoints.enqueue(i);
            marked[i] = true;
            edgeTo[i] = i;
        }

        while (!checkpoints.isEmpty()) {
            int apex = checkpoints.dequeue();
            for (int adj : dgph.adj(apex)) {
                if (marked[adj]) {
                    int tmp1 = adj;
                    searchedPath = new Stack<Integer>();
                    Stack<Integer> reverse = new Stack<Integer>();
                    reverse.push(tmp1);
                    while (edgeTo[tmp1] != tmp1) {
                        tmp1 = edgeTo[tmp1];
                        reverse.push(tmp1);
                    }
                    while (!reverse.isEmpty()) {
                        searchedPath.push(reverse.pop());
                    }
                    int tmp2 = apex;
                    searchedPath.push(tmp2);
                    while (edgeTo[tmp2] != tmp2) {
                        tmp2 = edgeTo[tmp2];
                        searchedPath.push(tmp2);
                    }
                    if (isInList(v, tmp1) && isInList(w, tmp2) ||
                            isInList(v, tmp2) && isInList(w, tmp1)) {
                        searchedPath.push(adj);
                        return searchedPath;
                    }
                }
                else {
                    checkpoints.enqueue(adj);
                    edgeTo[adj] = apex;
                    marked[adj] = true;
                }
            }
        }
        return null;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        Stack<Integer> findPath;
        int ancestor;
        findPath = ancestorSearch(v, w);
        if (findPath == null) {
            this.path = null;
            return -1;
        }
        else {
            ancestor = findPath.pop();
            this.path = findPath;
            return ancestor;
        }
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
            if (sap.path() != null) {
                for (int i : sap.path()) {
                    StdOut.print(i + ", ");
                }
                StdOut.print("\n");
            }

            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
