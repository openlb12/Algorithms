import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class MaxFlow {
    private final FlowGraph FGPH;
    private final int SOURCE;
    private final int TARGET;
    private boolean[] marked;
    private FlowEdge[] edgeTo;

    public MaxFlow(FlowGraph gph, int source, int target) {
        this.FGPH = gph;
        this.SOURCE = source;
        this.TARGET = target;
    }

    public double maxflow() {
        double flow = 0;
        while (hasAugumentPath()) {
            // get bottle neck of the augument path
            FlowEdge fedg = edgeTo[TARGET];
            double bottleNeck = fedg.getResidual();
            while (fedg != null) {
                if (bottleNeck > fedg.getResidual()) {
                    bottleNeck = fedg.getResidual();
                }
                fedg = edgeTo[fedg.getFROM()];
            }
            // reset residual capacity of flowedge
            fedg = edgeTo[TARGET];
            while (fedg != null) {
                fedg.setResidual(fedg.getResidual() - bottleNeck);
                fedg = edgeTo[fedg.getFROM()];
            }
            // add to the maxflow
            flow += bottleNeck;
        }
        return flow;
    }


    public boolean hasAugumentPath() {
        // achieved by Bread first search
        Queue<Integer> checkpoints = new Queue<Integer>();
        marked = new boolean[FGPH.V()];
        edgeTo = new FlowEdge[FGPH.V()];
        checkpoints.enqueue(SOURCE);
        marked[SOURCE] = true;
        edgeTo[SOURCE] = null;
        while (!checkpoints.isEmpty()) {
            int apex = checkpoints.dequeue();
            for (FlowEdge eg : FGPH.adj(apex)) {
                if (eg.getResidual() > 0.0 && !marked[eg.getTO()]) {
                    edgeTo[eg.getTO()] = eg;
                    marked[eg.getTO()] = true;
                    checkpoints.enqueue(eg.getTO());
                }
            }
        }
        return marked[TARGET];
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        FlowGraph fgph = new FlowGraph(in);
        MaxFlow mflow = new MaxFlow(fgph, 0, fgph.V() - 1);
        StdOut.print(fgph);
        StdOut.print(mflow.maxflow());

    }

}

class FlowGraph {
    private final int VERTICESIZE;
    private int edgeNum;
    private Bag<FlowEdge>[] adjacent;

    public FlowGraph(int VERTICESIZE) {
        this.VERTICESIZE = VERTICESIZE;
        this.edgeNum = 0;
        adjacent = (Bag<FlowEdge>[]) new Bag[this.VERTICESIZE];
        for (int i = 0; i < VERTICESIZE; i++) {
            adjacent[i] = new Bag<FlowEdge>();
        }

    }

    public FlowGraph(int VERTICESIZE, int edgeNum) {
        this.VERTICESIZE = VERTICESIZE;
        this.edgeNum = 0;
        adjacent = (Bag<FlowEdge>[]) new Bag[this.VERTICESIZE];
        for (int i = 0; i < VERTICESIZE; i++) {
            adjacent[i] = new Bag<FlowEdge>();
        }
        for (int i = 0; i < edgeNum; i++) {
            int from = StdRandom.uniform(this.VERTICESIZE);
            int to = StdRandom.uniform(this.VERTICESIZE);
            double cap = StdRandom.uniform(100);
            addEdge(from, to, cap);
        }

    }

    /**
     * Initializes a flow network from an input stream.
     * The format is the number of vertices <em>V</em>,
     * followed by the number of edges <em>E</em>,
     * followed by <em>E</em> pairs of vertices and edge capacities,
     * with each entry separated by whitespace.
     *
     * @param in the input stream
     * @throws IllegalArgumentException if the endpoints of any edge are not in prescribed range
     * @throws IllegalArgumentException if the number of vertices or edges is negative
     */
    public FlowGraph(In in) {
        this(in.readInt());
        int E = in.readInt();
        if (E < 0) throw new IllegalArgumentException("number of edges must be nonnegative");
        for (int i = 0; i < E; i++) {
            int v = in.readInt();
            int w = in.readInt();
            validateVertex(v);
            validateVertex(w);
            double capacity = in.readDouble();
            addEdge(v, w, capacity);
        }
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= VERTICESIZE)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (VERTICESIZE - 1));
    }


    /**
     * Returns the number of vertices in the edge-weighted graph.
     *
     * @return the number of vertices in the edge-weighted graph
     */
    public int V() {
        return VERTICESIZE;
    }

    /**
     * Returns the number of edges in the edge-weighted graph.
     *
     * @return the number of edges in the edge-weighted graph
     */
    public int E() {
        return edgeNum;
    }

    public void addEdge(int from, int to, double capacity) {
        validateVertex(from);
        validateVertex(to);
        adjacent[from].add(new FlowEdge(from, to, capacity));
        this.edgeNum++;
    }

    /**
     * Returns the edges incident on vertex {@code v} (includes both edges pointing to
     * and from {@code v}).
     *
     * @param v the vertex
     * @return the edges incident on vertex {@code v} as an Iterable
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public Iterable<FlowEdge> adj(int v) {
        validateVertex(v);
        return adjacent[v];
    }

    // return list of all edges - excludes self loops
    public Iterable<FlowEdge> edges() {
        Bag<FlowEdge> list = new Bag<FlowEdge>();
        for (int v = 0; v < VERTICESIZE; v++)
            for (FlowEdge e : adjacent[v]) {
                if (e.getTO() != v)
                    list.add(e);
            }
        return list;
    }

    /**
     * Returns a string representation of the flow network.
     * This method takes time proportional to <em>E</em> + <em>V</em>.
     *
     * @return the number of vertices <em>V</em>, followed by the number of edges <em>E</em>,
     * followed by the <em>V</em> adjacency lists
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(String.format("%d %d\n", VERTICESIZE, edgeNum));
        for (int v = 0; v < VERTICESIZE; v++) {
            s.append(v + ":  ");
            for (FlowEdge e : adjacent[v]) {
                if (e.getTO() != v) s.append(e + "  ");
            }
            s.append("\n");
        }
        return s.toString();
    }

}

class FlowEdge {
    private final int FROM;
    private final int TO;
    private final double CAPACITY;
    private double residual;

    public FlowEdge(int FROM, int TO, double CAPACITY) {
        this.FROM = FROM;
        this.TO = TO;
        this.CAPACITY = CAPACITY;
        this.residual = this.CAPACITY;
    }

    public int getFROM() {
        return FROM;
    }

    public int getTO() {
        return TO;
    }

    public double getCAPACITY() {
        return CAPACITY;
    }

    public double getResidual() {
        return residual;
    }

    public void setResidual(double residual) {
        this.residual = residual;
    }

    @Override
    public String toString() {
        return String.format("%d -> %d  %.4f/%.4f", FROM, TO, residual, CAPACITY);
    }
}
