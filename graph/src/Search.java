import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Search {
    private final int VORTEXSIZE;
    private boolean[] marked;
    private final Graph gph;
    private final int checkPoint;
    private Stack<Integer> connected;

    public Search(Graph g, int s) {
        VORTEXSIZE = g.V();
        checkPoint = s;
        marked = new boolean[VORTEXSIZE];
        for (int i = 0; i < VORTEXSIZE; i++) {
            marked[i] = false;
        }
        gph = g;
        connected = null;

    }

    public Iterable<Integer> connectedDFS() {
//        if (connected != null) return connected;
        resetMarked();
        connected = new Stack<Integer>();
        Stack<Integer> checkApex = new Stack<Integer>();
        checkApex.push(checkPoint);
        while (!checkApex.isEmpty()) {
            int apex = checkApex.pop();
            if (marked[apex]) continue;
            connected.push(apex);
            marked[apex] = true;
            for (int adj : gph.adj(apex)) {
                if (marked[adj] != true) {
                    checkApex.push(adj);
                }
            }
        }
        return connected;
    }

    private void resetMarked() {
        for (int i = 0; i < VORTEXSIZE; i++) {
            marked[i] = false;
        }
    }


    public Iterable<Integer> connectedBFS() {
//        if (connected != null) return connected;
        resetMarked();
        connected = new Stack<Integer>();
        Queue<Integer> checkApex = new Queue<Integer>();
        checkApex.enqueue(checkPoint);
        while (!checkApex.isEmpty()) {
            int apex = checkApex.dequeue();
            if (marked[apex]) continue;
            connected.push(apex);
            marked[apex] = true;
            for (int adj : gph.adj(apex)) {
                if (marked[adj] != true) {
                    checkApex.enqueue(adj);
                }
            }
        }
        return connected;
    }


    private Iterable<Integer> DFS(int s, int v) {
        if (s == v) return null;
        resetMarked();
        Stack<Integer> searchingApex = new Stack<Integer>();
        Stack<Integer> trace = new Stack<Integer>();
        searchingApex.push(s);
        while (!searchingApex.isEmpty()) {
            int apex = searchingApex.pop();
            if (marked[apex]) continue;
            trace.push(apex);
            marked[apex] = true;
            if (apex == v) return trace;
            boolean isSearchCompleted = true;
            for (int adj : gph.adj(apex)) {
                if (marked[adj] != true) {
                    isSearchCompleted = false;
                    searchingApex.push(adj);
                }
            }
            if (isSearchCompleted) {
                trace.pop();
            }
        }
        return null;
    }


    private Iterable<Integer> BFS(int s, int v) {
        if (s == v) return null;
        resetMarked();
        Queue<Integer> searchingApex = new Queue<Integer>();
        Stack<Integer> trace = new Stack<Integer>();
        searchingApex.enqueue(s);
        while (!searchingApex.isEmpty()) {
            int apex = searchingApex.dequeue();
            if (marked[apex]) continue;
            trace.push(apex);
            marked[apex] = true;
            if (apex == v) return trace;
            boolean isSearchCompleted = true;
            for (int adj : gph.adj(apex)) {
                if (marked[adj] != true) {
                    isSearchCompleted = false;
                    searchingApex.enqueue(adj);
                }
            }
            if (isSearchCompleted) {
                trace.pop();
            }
        }
        return null;
    }

    public boolean marked(int v) {
        return marked[v];
    }

    public int count() {
        return connected.size();

    }

    public static void main(String[] args) {
        In inp = new In(args[0]);
        Graph gph = new Graph(inp);
        StdOut.print(gph);
        Search srch = new Search(gph, Integer.parseInt(args[1]));
        for (int i : srch.connectedBFS()) {
            StdOut.printf("%3d", i);
        }
        StdOut.println();
        for (int i : srch.connectedDFS()) {
            StdOut.printf("%3d", i);
        }
        StdOut.println();
        for (int i : srch.DFS(Integer.parseInt(args[1]), Integer.parseInt(args[2]))) {
            StdOut.printf("%3d", i);
        }
        StdOut.println();
        for (int i : srch.BFS(Integer.parseInt(args[1]), Integer.parseInt(args[2]))) {
            StdOut.printf("%3d", i);
        }
        StdOut.println();
    }
}
