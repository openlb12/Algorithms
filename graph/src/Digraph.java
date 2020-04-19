import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.NoSuchElementException;

public class Digraph {
    private final int VERTICESIZE;
    private int edgeSize;
    private Bag<Integer>[] adjacent;
    private int[] inDegree;

    public Digraph(int V) {
        this.VERTICESIZE = V;
        adjacent = (Bag<Integer>[]) new Bag[VERTICESIZE];
        inDegree = new int[VERTICESIZE];
        for (int i = 0; i < VERTICESIZE; i++) {
            adjacent[i] = new Bag<Integer>();
            inDegree[i] = 0;
        }
    }

    /**
     * Initializes a digraph from the specified input stream.
     * The format is the number of vertices <em>V</em>,
     * followed by the number of edges <em>E</em>,
     * followed by <em>E</em> pairs of vertices, with each entry separated by whitespace.
     *
     * @param in the input stream
     * @throws IllegalArgumentException if {@code in} is {@code null}
     * @throws IllegalArgumentException if the endpoints of any edge are not in prescribed range
     * @throws IllegalArgumentException if the number of vertices or edges is negative
     * @throws IllegalArgumentException if the input stream is in the wrong format
     */
    public Digraph(In in) {
        if (in == null) throw new IllegalArgumentException("argument is null");
        try {
            this.VERTICESIZE = in.readInt();
            if (VERTICESIZE < 0) throw new IllegalArgumentException(
                    "number of vertices in a Digraph must be nonnegative");
            inDegree = new int[VERTICESIZE];
            adjacent = (Bag<Integer>[]) new Bag[VERTICESIZE];
            for (int v = 0; v < VERTICESIZE; v++) {
                adjacent[v] = new Bag<Integer>();
                inDegree[v] = 0;
            }
            int E = in.readInt();
            if (E < 0) throw new IllegalArgumentException(
                    "number of edges in a Digraph must be nonnegative");
            for (int i = 0; i < E; i++) {
                int v = in.readInt();
                int w = in.readInt();
                addEdge(v, w);
            }
        }
        catch (NoSuchElementException e) {
            throw new IllegalArgumentException(
                    "invalid input format in Digraph constructor", e);
        }
    }

    /**
     * Returns the number of vertices in this digraph.
     *
     * @return the number of vertices in this digraph
     */
    public int V() {
        return VERTICESIZE;
    }

    /**
     * Returns the number of edges in this digraph.
     *
     * @return the number of edges in this digraph
     */
    public int E() {
        return edgeSize;
    }


    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        if (v < 0 || v >= VERTICESIZE)
            throw new IllegalArgumentException(
                    "vertex " + v + " is not between 0 and " + (VERTICESIZE - 1));
    }

    public void addEdge(int v, int w) {
        adjacent[v].add(w);
        inDegree[w]++;
        edgeSize++;
    }


    public Iterable<Integer> adj(int v) {
        validateVertex(v);
        return adjacent[v];
    }

    public Digraph reverse() {
        Digraph reverse = new Digraph(VERTICESIZE);
        for (int i = 0; i < VERTICESIZE; i++) {
            for (int adj : adjacent[i]) {
                reverse.addEdge(adj, i);
            }
        }
        return reverse;
    }

    private boolean isSelfLoop() {
        for (int i = 0; i < VERTICESIZE; i++) {
            for (int adj : adjacent[i]) {
                if (adj == i) return true;
            }
        }
        return false;
    }


    public boolean isCycled() {
        boolean[] marked = new boolean[VERTICESIZE];
        // boolean[] visited = new boolean[VERTICESIZE];
        int[] edgeTo = new int[VERTICESIZE];
        Stack<Integer> checkpoints = new Stack<Integer>();
        for (int i = 0; i < VERTICESIZE; i++) {
            if (marked[i]) continue;
            // visited[i] = true;
            checkpoints.push(i);
            edgeTo[i] = i;
            while (!checkpoints.isEmpty()) {
                int apex = checkpoints.pop();
                if (marked[apex]) {
                    Stack<Integer> cycle = new Stack<Integer>();
                    cycle.push(apex);
                    int tmp = apex;
                    while (edgeTo[tmp] != tmp) {
                        tmp = edgeTo[tmp];
                        cycle.push(tmp);
                    }
                    if (cycle.peek() == apex) return true;
                    else continue;
                }
                marked[apex] = true;
                for (int adj : adjacent[apex]) {
                    if (adj == i) return true;
                    else {
                        checkpoints.push(adj);
                        // visited[adj] = true;
                        edgeTo[adj] = apex;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Returns a string representation of the graph.
     *
     * @return the number of vertices <em>V</em>, followed by the number of edges <em>E</em>,
     * followed by the <em>V</em> adjacency lists
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(VERTICESIZE + " vertices, " + edgeSize + " edges\n ");
        for (int v = 0; v < VERTICESIZE; v++) {
            s.append(String.format("%d: ", v));
            for (int w : adjacent[v]) {
                s.append(String.format("%d ", w));
            }
            s.append("\n");
        }
        return s.toString();
    }

    /**
     * Unit tests the {@code Digraph} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        DiCycle finder = new DiCycle(G);
        StdOut.println(G);
        if (G.isCycled()) StdOut.println("The digraph is circled!!");
        if (finder.hasCycle()) {
            StdOut.print("Directed cycle: ");
            for (int v : finder.cycle()) {
                StdOut.print(v + " ");
            }
            StdOut.println();
        }

        else {
            StdOut.println("No directed cycle");
        }
        StdOut.println();
    }
}

