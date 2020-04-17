import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Graph {
    private int verticesize;
    private int edgesize;
    private Bag<Integer>[] adjecent;

    public Graph(int verticesize) {
        // construct Graph with vs vertices
        this.verticesize = verticesize;
        this.edgesize = 0;
        this.adjecent = (Bag<Integer>[]) new Bag[this.verticesize];
        for (int i = 0; i < this.verticesize; i++) {
            this.adjecent[i] = new Bag<Integer>();
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
        return adjecent[v].size();
    }

    public void addEdge(int v1, int v2) {
        adjecent[v1].add(v2);
        adjecent[v2].add(v1);
        edgesize++;
    }

    public Iterable<Integer> adj(int v) {
        return adjecent[v];
    }

    public boolean isCycled() {

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
            marked[i] = true;
            subsets[i] = loop;
            while (!checkpoints.isEmpty()) {
                int apex = checkpoints.pop();
                marked[apex] = true;
                subsets[apex] = loop;
                for (int adj : adjecent[apex]) {
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
        StdOut.print(gph);
        for (int item : gph.subsets()) {
            StdOut.print(item);
        }
    }
}
