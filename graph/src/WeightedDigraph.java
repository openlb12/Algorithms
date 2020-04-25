import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.IndexMaxPQ;
import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class WeightedDigraph {
    private final int VERTICESIZE;
    private int edgeSize;
    private Bag<Diedge>[] adjacent;
    private int inDegree[];
    private int dataSize;
    Stack<Integer> trace;
    boolean[] marked;

    public WeightedDigraph(int V) {
        VERTICESIZE = V;
        adjacent = (Bag<Diedge>[]) new Bag[VERTICESIZE];
        inDegree = new int[VERTICESIZE];
        for (int i = 0; i < VERTICESIZE; i++) {
            adjacent[i] = new Bag<Diedge>();
        }
    }

    public WeightedDigraph(In in) {
        VERTICESIZE = in.readInt();
        adjacent = (Bag<Diedge>[]) new Bag[VERTICESIZE];
        inDegree = new int[VERTICESIZE];
        for (int i = 0; i < VERTICESIZE; i++) {
            adjacent[i] = new Bag<Diedge>();
        }
        int edges = in.readInt();
        for (int i = 0; i < edges; i++) {
            int start = in.readInt();
            int end = in.readInt();
            double length = in.readDouble();
            addEdge(new Diedge(start, end, length));
        }
    }

    public int V() {
        return VERTICESIZE;
    }

    public int E() {
        return edgeSize;
    }

    public Iterable<Diedge> adj(int v) {
        return adjacent[v];
    }

    public Iterable<Diedge> edges() {
        Stack<Diedge> eds = new Stack<Diedge>();
        for (int i = 0; i < VERTICESIZE; i++) {
            for (Diedge ed : adjacent[i]) {
                eds.push(ed);
            }
        }
        return eds;
    }

    public void addEdge(Diedge ed) {
        int from = ed.getFrom();
        int to = ed.getTo();
        adjacent[from].add(ed);
        inDegree[to]++;
        edgeSize++;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(String.format("vertices: %d, edges: %d\n", VERTICESIZE, edgeSize));
        for (Diedge ed : this.edges()) {
            str.append(ed.toString() + "\n");
        }
        return str.toString();
    }

    public String queue_spt(int v) {
        double[] distanceTo = new double[VERTICESIZE];
        int[] edgeFrom = new int[VERTICESIZE];
        boolean[] marked = new boolean[VERTICESIZE];
        for (int i = 0; i < VERTICESIZE; i++) {
            distanceTo[i] = Double.POSITIVE_INFINITY;
        }
        edgeFrom[v] = v;
        distanceTo[v] = 0.0;
        marked[v] = true;
        Queue<Diedge> edges = new Queue<Diedge>();
        for (Diedge ded : adjacent[v]) {
            distanceTo[ded.getTo()] = ded.getWeight();
            edgeFrom[ded.getTo()] = v;
            edges.enqueue(ded);
        }
        dataSize = edges.size();
        while (!edges.isEmpty()) {
            Diedge ed = edges.dequeue();
            int end = ed.getTo();
            marked[end] = true;
            for (Diedge ded : adjacent[end]) {
                int dedEnd = ded.getTo();
                if (distanceTo[end] + ded.getWeight() < distanceTo[dedEnd]) {
                    distanceTo[dedEnd] = distanceTo[end] + ded.getWeight();
                    edgeFrom[dedEnd] = end;
                    edges.enqueue(ded);
                }
            }
            if (edges.size() > dataSize) dataSize = edges.size();
        }
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < VERTICESIZE; i++) {
            if (distanceTo[i] < Double.MAX_VALUE) {
                str.append(String.format("distance %d -> %d: %.5f\n", v, i, distanceTo[i]));
                Stack<Integer> trace = new Stack<Integer>();
                int vs = i;

                while (edgeFrom[vs] != vs) {
                    vs = edgeFrom[vs];
                    trace.push(vs);
                }

                while (!trace.isEmpty()) {
                    str.append(String.format("%d -> ", trace.pop()));
                }
                str.append(String.format("%d\n", i));

            }
        }
        return str.toString();
    }

    public String indexMinPQ_spt(int v) {
        double[] distanceTo = new double[VERTICESIZE];
        int[] edgeFrom = new int[VERTICESIZE];
        boolean[] marked = new boolean[VERTICESIZE];
        for (int i = 0; i < VERTICESIZE; i++) {
            distanceTo[i] = Double.MAX_VALUE;
        }
        edgeFrom[v] = v;
        distanceTo[v] = 0.0;
        marked[v] = true;
        IndexMinPQ<Double> edges = new IndexMinPQ<Double>(VERTICESIZE);
        for (Diedge ded : adjacent[v]) {
            int end = ded.getTo();
            distanceTo[end] = ded.getWeight();
            edgeFrom[end] = v;
            edges.insert(end, distanceTo[end]);
        }
        dataSize = edges.size();
        while (!edges.isEmpty()) {
            double distance = edges.minKey();
            int ver = edges.delMin();
            for (Diedge ded : adjacent[ver]) {
                int dedEnd = ded.getTo();
                if (distance + ded.getWeight() < distanceTo[dedEnd]) {
                    distanceTo[dedEnd] = distance + ded.getWeight();
                    edgeFrom[dedEnd] = ver;
                    if (edges.contains(dedEnd)) edges.decreaseKey(dedEnd, distanceTo[dedEnd]);
                    else edges.insert(dedEnd, distanceTo[dedEnd]);
                }
            }
            if (dataSize < edges.size()) dataSize = edges.size();
        }
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < VERTICESIZE; i++) {
            if (distanceTo[i] < Double.MAX_VALUE) {
                str.append(String.format("distance %d -> %d: %.5f\n", v, i, distanceTo[i]));
                Stack<Integer> trace = new Stack<Integer>();
                int vs = i;

                while (edgeFrom[vs] != vs) {
                    vs = edgeFrom[vs];
                    trace.push(vs);
                }

                while (!trace.isEmpty()) {
                    str.append(String.format("%d -> ", trace.pop()));
                }
                str.append(String.format("%d\n", i));

            }
        }
        return str.toString();
    }

    private boolean isDAG() {
        int[] edgeTo = new int[VERTICESIZE];
        boolean[] marked = new boolean[VERTICESIZE];
        for (int i = 0; i < VERTICESIZE; i++) {
            if (marked[i]) continue;
            Stack<Integer> checkPoints = new Stack<Integer>();
            edgeTo[i] = i;
            checkPoints.push(i);
            while (!checkPoints.isEmpty()) {
                int apex = checkPoints.pop();
                marked[apex] = true;
                for (Diedge dg : adjacent[apex]) {
                    int end = dg.getTo();
                    if (marked[end]) {
                        if (end == apex) return false;
                        // self-loop check
                        int track = apex;
                        while (edgeTo[track] != track) {
                            if (track == end) return false;
                            // cycle check
                            track = edgeTo[track];
                        }
                    } else {
                        checkPoints.push(end);
                        edgeTo[end] = apex;
                    }
                }
            }
        }
        return true;
    }

    private void dfs(int i) {
        marked[i] = true;
        for (Diedge dg : adjacent[i]) {
            if (!marked[dg.getTo()]) dfs(dg.getTo());
        }
        trace.push(i);
    }

    private Stack<Integer> topolocical() {
        if (!isDAG()) return null;
        marked = new boolean[VERTICESIZE];
        trace = new Stack<Integer>();
        for (int i = 0; i < VERTICESIZE; i++) {
            if (marked[i]) continue;
            dfs(i);
        }
        return trace;

    }


    public String acyclic_spt() {
        if (!isDAG()) throw new IllegalArgumentException("Digraph is not acyclic.");
        boolean[] marked = new boolean[VERTICESIZE];
        double[] distanceTo = new double[VERTICESIZE];
        int[] edgeFrom = new int[VERTICESIZE];
        IndexMinPQ<Double> track = new IndexMinPQ<Double>(VERTICESIZE);
        for (int i = 0; i < VERTICESIZE; i++) {
            distanceTo[i] = Double.POSITIVE_INFINITY;
        }


        for (int id : topolocical()) {
            if (distanceTo[id] == Double.POSITIVE_INFINITY) {
                distanceTo[id] = 0.0;
                edgeFrom[id] = id;
            }
            for (Diedge eg : adjacent[id]) {
                int end = eg.getTo();
                if (distanceTo[end] > eg.getWeight() + distanceTo[id]) {
                    distanceTo[end] = eg.getWeight() + distanceTo[id];
                    edgeFrom[end] = id;
                }

            }
        }
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < VERTICESIZE; i++) {
            if (distanceTo[i] < Double.MAX_VALUE) {
                int vs = i;
                Stack<Integer> trace = new Stack<Integer>();
                while (edgeFrom[vs] != vs) {
                    vs = edgeFrom[vs];
                    trace.push(vs);
                }
                str.append(String.format("distance %d -> %d: %.5f\n", vs, i, distanceTo[i]));

                while (!trace.isEmpty()) {
                    str.append(String.format("%d -> ", trace.pop()));
                }
                str.append(String.format("%d\n", i));

            }
        }
        return str.toString();

    }

    public String acyclic_lpt() {
        if (!isDAG()) throw new IllegalArgumentException("Digraph is not acyclic.");
        boolean[] marked = new boolean[VERTICESIZE];
        double[] distanceTo = new double[VERTICESIZE];
        int[] edgeFrom = new int[VERTICESIZE];
        IndexMaxPQ<Double> track = new IndexMaxPQ<Double>(VERTICESIZE);
        for (int i = 0; i < VERTICESIZE; i++) {
            distanceTo[i] = Double.NEGATIVE_INFINITY;
        }
        for (int id : topolocical()) {
            if (marked[id]) continue;
            edgeFrom[id] = id;
            distanceTo[id] = 0.0;
            track.insert(id, distanceTo[id]);
            while (!track.isEmpty()) {
                int apex = track.maxIndex();
                double distance = track.maxKey();
                marked[apex] = true;
                track.delMax();
                for (Diedge eg : adjacent[apex]) {
                    int end = eg.getTo();
                    if (distanceTo[end] < eg.getWeight() + distance) {
                        distanceTo[end] = eg.getWeight() + distance;
                        edgeFrom[end] = apex;
                        if (track.contains(end)) {
                            track.increaseKey(end, distanceTo[end]);
                        } else {
                            track.insert(end, distanceTo[end]);
                        }

                    }
                }
            }

        }
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < VERTICESIZE; i++) {
            if (distanceTo[i] > Double.NEGATIVE_INFINITY) {
                int vs = i;
                Stack<Integer> trace = new Stack<Integer>();
                while (edgeFrom[vs] != vs) {
                    vs = edgeFrom[vs];
                    trace.push(vs);
                }
                str.append(String.format("distance %d -> %d: %.5f\n", vs, i, distanceTo[i]));

                while (!trace.isEmpty()) {
                    str.append(String.format("%d -> ", trace.pop()));
                }
                str.append(String.format("%d\n", i));

            }
        }
        return str.toString();

    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        WeightedDigraph G = new WeightedDigraph(in);
        StdOut.print(G);
        for (int i : G.topolocical()) {
            StdOut.print(String.format("%d -> ", i));
        }
        StdOut.println();
        StdOut.print(G.acyclic_lpt());
        while (!StdIn.isEmpty()) {
            int vertice = StdIn.readInt();
            StdOut.print(G.queue_spt(vertice));
            int qSize = G.dataSize;
            StdOut.print(G.indexMinPQ_spt(vertice));
            int iSize = G.dataSize;
            StdOut.print(G.acyclic_spt(vertice));
            StdOut.println(String.format("Queue size %d vs IndexMinPQ size %d", qSize, iSize));
        }
    }
}

class Diedge implements Comparable<Diedge> {
    private final double weight;
    private final int from;
    private final int to;

    public Diedge(int v, int w, double weight) {
        this.from = v;
        this.to = w;
        this.weight = weight;
    }

    public int getFrom() {
        return from;
    }

    /**
     * @return int
     */
    public int getTo() {
        return to;
    }

    /**
     * @param ob
     * @return int <0: ob is greater, =0: equal; >0: ob is smaller
     */
    @Override
    public int compareTo(Diedge ob) {
        return Double.compare(this.weight, ob.weight);
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return String.format("%d -> %d %.5f", from, to, weight);
    }
}
