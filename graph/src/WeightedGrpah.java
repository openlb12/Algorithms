import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
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
        adjacent = (Bag<Edge>[]) new Bag[VERTICESIZE];
        for (int i = 0; i < VERTICESIZE; i++) {
            adjacent[i] = new Bag<Edge>();
        }
        int edges = in.readInt();
        for (int i = 0; i < edges; i++) {
            int start = in.readInt();
            int end = in.readInt();
            double length = in.readDouble();
            addEdge(new Edge(start, end, length));
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
        Stack<Edge> eds = new Stack<Edge>();
        for (int i = 0; i < VERTICESIZE; i++) {
            for (Edge ed : adjacent[i]) {
                if (ed.other(i) >= i) eds.push(ed);
            }
        }
        return eds;
    }

    public void addEdge(Edge ed) {
        int end = ed.either();
        int other = ed.other(end);
        if (end == other) {
            adjacent[end].add(ed);
            edgeSize++;
        } else {
            adjacent[end].add(ed);
            adjacent[other].add(ed);
            edgeSize++;
        }
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(String.format("vertices: %d, edges: %d\n", VERTICESIZE, edgeSize));
        for (Edge ed : this.edges()) {
            str.append(ed.toString() + "\n");
        }
        return str.toString();
    }

    public String mst() {
        double edgeLength = 0;
        MinPQ<Edge> edges = new MinPQ<Edge>();
        Stack<Edge> trace = new Stack<Edge>();
        boolean[] marked = new boolean[VERTICESIZE];
        for (Edge ed : edges()) {
            edges.insert(ed);
        }
        while (!edges.isEmpty()) {
            Edge ed = edges.delMin();
            int bgn = ed.either();
            int end = ed.other(bgn);
            if (marked[bgn] && marked[end]) continue;
            else {
                marked[bgn] = true;
                marked[end] = true;
                edgeLength += ed.getWeight();
                trace.push(ed);
            }
        }
        StringBuilder str = new StringBuilder();
        str.append(String.format("MST length: %.5f\n", edgeLength));
//        for (Edge edg : trace) {
//            str.append(edg + "\n");
//        }
        return str.toString();
    }


    public static void main(String[] args) {
        In in = new In(args[0]);
        WeightedGrpah G = new WeightedGrpah(in);
        StdOut.print(G);
        StdOut.print(G.mst());
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

    /**
     * @param w
     * @return int
     */
    public int other(int w) {
        if (w == this.w) return this.v;
        else if (w == this.v) return this.w;
        else throw new IllegalArgumentException("Illegal endpoint");
    }

    /**
     * @param ob
     * @return int <0: ob is greater, =0: equal; >0: ob is smaller
     */
    @Override
    public int compareTo(Edge ob) {
        return Double.compare(this.weight, ob.weight);
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return String.format("%d-%d %.5f", v, w, weight);
    }
}
