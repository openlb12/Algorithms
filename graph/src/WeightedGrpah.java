import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class WeightedGrpah {
    private final int VERTICESIZE;
    private int edgeSize;
    private Bag<Edge>[] adjacent;

    public WeightedGrpah(int V) {
        VERTICESIZE = V;
        adjacent = (Bag<Edge>[]) new Bag[VERTICESIZE];
        for (int i = 0; i < VERTICESIZE; i++) {
            adjacent[i] = new Bag<Edge>();
        }
    }

    public WeightedGrpah(In in) {
        VERTICESIZE = in.readInt();
        int edges = in.readInt();
        for (int i = 0; i < edges; i++) {
            addEdge(new Edge(in.readInt(), in.readInt(), in.readDouble()));
        }
    }

    public int V() {
        return VERTICESIZE;
    }

    public int E() {
        return edgeSize;
    }

    public Iterable<Edge> adj(int v) {
        return adjacent[v];
    }

    public Iterable<Edge> edges() {
        return new Stack<Edge>();
    }

    public void addEdge(Edge ed) {
        int end = ed.either();
        adjacent[end].add(ed);
        adjacent[ed.other(end)].add(ed);
        edgeSize++;
    }

    public static void main(String[] args) {
        Edge e = new Edge(12, 34, 5.67);
        StdOut.println(e);
    }
}

class Edge implements Comparable<Edge> {
    private final double weight;
    private final int v;
    private final int w;

    public Edge(int v, int w, double weight) {
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    public int either() {
        return v;
    }

    public int other(int w) {
        if (w == this.w) return this.v;
        else if (w == this.v) return this.w;
        else throw new IllegalArgumentException("Illegal endpoint");
    }

    @Override
    public int compareTo(Edge ob) {
        return Double.compare(this.weight, ob.weight);
    }

    @Override
    public String toString() {
        return String.format("%d-%d %.5f", v, w, weight);
    }
}
