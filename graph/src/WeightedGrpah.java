import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.UF;

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

    public String lazy_prim_mst() {
        double edgeLength = 0;
        MinPQ<Edge> edges = new MinPQ<Edge>();
        Stack<Edge> trace = new Stack<Edge>();
        boolean[] marked = new boolean[VERTICESIZE];

        for (int i = 0; i < VERTICESIZE; i++) {
            if (!marked[i]) {
                marked[i] = true;
                for (Edge ed : adjacent[i]) {
                    edges.insert(ed);
                }
                while (!edges.isEmpty()) {
                    Edge ed = edges.delMin();
                    int bgn = ed.either();
                    int end = ed.other(bgn);
                    if (marked[bgn] && marked[end]) continue;
                    else if (!marked[bgn] && !marked[end]) continue;
                    else {
                        if (marked[end]) {
                            marked[bgn] = true;
                            for (Edge ied : adj(bgn)) {
                                if (!marked[ied.other(bgn)]) edges.insert(ied);
                            }
                        } else {
                            marked[end] = true;
                            for (Edge ied : adj(end)) {
                                if (!marked[ied.other(end)]) edges.insert(ied);
                            }
                        }
                        edgeLength += ed.getWeight();
                        trace.push(ed);
                        if (trace.size() == VERTICESIZE - 1) break;
                    }
                }
            }
        }

        StringBuilder str = new StringBuilder();
        str.append(String.format("MST length: %.5f\n", edgeLength));
        for (Edge edg : trace) {
            str.append(edg + "\n");
        }
        return str.toString();
    }

    public String prim_mst() {
        boolean[] marked = new boolean[VERTICESIZE];
        double[] distanceTo = new double[VERTICESIZE];
        double edgeLength = 0.0;
        for (int i = 0; i < VERTICESIZE; i++) {
            distanceTo[i] = Double.MAX_VALUE;
        }
        IndexMinPQ<Edge> edges = new IndexMinPQ<Edge>(VERTICESIZE);
        Stack<Edge> trace = new Stack<Edge>();
        for (int i = 0; i < VERTICESIZE; i++) {
            if (marked[i]) continue;
            distanceTo[i] = 0.0;
            marked[i] = true;
            for (Edge ed : adjacent[i]) {
                int end = ed.other(i);
                distanceTo[end] = ed.getWeight();
                edges.insert(end, ed);
            }
            while (!edges.isEmpty()) {
                int addPoint = edges.minIndex();
                Edge shortEd = edges.minKey();
                if (marked[addPoint]) continue;
                trace.push(shortEd);
                marked[addPoint] = true;
                edgeLength += shortEd.getWeight();
                edges.delMin();
                for (Edge ed : adjacent[addPoint]) {
                    int end = ed.other(addPoint);
                    if (marked[end]) continue;
                    if (ed.getWeight() < distanceTo[end]) {
                        distanceTo[end] = ed.getWeight();
                        if (edges.contains(end)) {
                            edges.decreaseKey(end, ed);
                        } else {
                            edges.insert(end, ed);
                        }
                    }
                }
            }
        }
        StringBuilder str = new StringBuilder();
        str.append(String.format("MST length: %.5f\n", edgeLength));
        for (Edge edg : trace) {
            str.append(edg + "\n");
        }
        return str.toString();
    }

    public String kurshal_mst() {
        MinPQ<Edge> edges = new MinPQ<Edge>();
        double edgeLength = 0;
        Stack<Edge> trace = new Stack<Edge>();
        UF tree = new UF(VERTICESIZE);
        for (Edge ed : edges()) {
            edges.insert(ed);
        }
        while (!edges.isEmpty()) {
            Edge ed = edges.delMin();
            int bgn = ed.either();
            int end = ed.other(bgn);
            if (tree.connected(bgn, end)) continue;
            tree.union(bgn, end);
            trace.push(ed);
            edgeLength += ed.getWeight();
            if (trace.size() == VERTICESIZE - 1) break;
        }
        StringBuilder str = new StringBuilder();
        str.append(String.format("MST length: %.5f\n", edgeLength));
        for (Edge edg : trace) {
            str.append(edg + "\n");
        }
        return str.toString();

    }


    public static void main(String[] args) {
        In in = new In(args[0]);
        WeightedGrpah G = new WeightedGrpah(in);
        StdOut.print(G);
        StdOut.print(G.lazy_prim_mst());
        StdOut.print(G.prim_mst());
        StdOut.print(G.kurshal_mst());
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
