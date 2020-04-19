import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Graph {
    private int verticesize;
    private int edgesize;
    private Bag<Integer>[] adjacent;

    public Graph(int verticesize) {
        // construct Graph with vs vertices
        this.verticesize = verticesize;
        this.edgesize = 0;
        this.adjacent = (Bag<Integer>[]) new Bag[this.verticesize];
        for (int i = 0; i < this.verticesize; i++) {
            this.adjacent[i] = new Bag<Integer>();
        }
    }


    public Graph(In in) {
        this(in.readInt());
        int edges = in.readInt();
        for (int i = 0; i < edges; i++) {
            int v1 = in.readInt();
            int v2 = in.readInt();
            addEdge(v1, v2);
        }
    }

    public int V() {
        return verticesize;
    }

    public int E() {
        return edgesize;
    }

    public int degree(int v) {
        return adjacent[v].size();
    }

    public void addEdge(int v1, int v2) {
        adjacent[v1].add(v2);
        adjacent[v2].add(v1);
        edgesize++;
    }

    public Iterable<Integer> adj(int v) {
        return adjacent[v];
    }

    private boolean isSelfLooped() {
        // if the adjacent vertice contains itself, then it is selflooped.
        for (int i = 0; i < verticesize; i++) {
            for (int w : adjacent[i]) {
                if (w == i) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isParalled() {
        // if there are two edges connect the neighbouring vertice, then it is
        // paralled.
        boolean[] marked = new boolean[verticesize];
        for (int i = 0; i < verticesize; i++) {
            for (int w : adjacent[i]) {
                if (marked[w]) {
                    return true;
                }
                marked[w] = true;
            }
            for (int j = 0; j < verticesize; j++) {
                // reset marked for next adjacent judge
                marked[j] = false;
            }
        }
        return false;
    }

    public boolean isCycled() {
        if (isSelfLooped()) return true;
        if (isParalled()) return true;
        boolean[] marked = new boolean[verticesize];
        int[] edgeTo = new int[verticesize];
        Stack<Integer> checkpoint;
        for (int i = 0; i < verticesize; i++) {
            if (marked[i]) continue;
            checkpoint = new Stack<Integer>();
            checkpoint.push(i);
            edgeTo[i] = i;
            while (!checkpoint.isEmpty()) {
                int apex = checkpoint.pop();
                if (marked[apex]) return true;
                marked[apex] = true;
                for (int adj : adjacent[apex]) {
                    if (!marked[adj]) {
                        checkpoint.push(adj);
                        edgeTo[adj] = apex;
                    }
                }
            }
        }
        return false;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < verticesize; i++) {
            str.append(String.format("%5d: ", i));
            for (int j : adj(i)) {
                str.append(String.format("%3d, ", j));
            }
            str.append("\n");
        }
        return str.toString();
    }

    public int[] subsets() {
        boolean[] marked = new boolean[verticesize];
        int[] subsets = new int[verticesize];
        int loop = 0;
        for (int i = 0; i < verticesize; i++) {
            marked[i] = false;
            subsets[i] = Integer.MAX_VALUE;
        }
        for (int i = 0; i < verticesize; i++) {
            if (marked[i]) continue;
            Stack<Integer> checkpoints = new Stack<Integer>();
            Stack<Integer> trace = new Stack<Integer>();
            checkpoints.push(i);
            while (!checkpoints.isEmpty()) {
                int apex = checkpoints.pop();
                if (marked[apex]) continue;
                marked[apex] = true;
                subsets[apex] = loop;
                for (int adj : adjacent[apex]) {
                    if (!marked[adj]) {
                        checkpoints.push(adj);
                    }
                }
            }
            loop++;
        }
        return subsets;
    }

    public static void main(String[] args) {
        In inp = new In(args[0]);
        Graph gph = new Graph(inp);
        if (gph.isCycled()) StdOut.println("Is circled");
        StdOut.print(gph);
        for (int item : gph.subsets()) {
            StdOut.print(item);
        }
    }
}
