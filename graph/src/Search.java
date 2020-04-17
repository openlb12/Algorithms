import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Search {
    private final int VORTEXSIZE;
    private boolean[] marked;
    private int[] edgeTo;
    private final Graph gph;
    private final int checkPoint;
    private Stack<Integer> connected;

    public Search(Graph g, int s) {
        VORTEXSIZE = g.V();
        checkPoint = s;
        marked = new boolean[VORTEXSIZE];
        edgeTo = new int[VORTEXSIZE];
        for (int i = 0; i < VORTEXSIZE; i++) {
            marked[i] = false;
        }
        gph = g;
        connected = null;
    }


    public void circlesDFS() {
        int[] circle = new int[VORTEXSIZE];
        resetMarked();

        int loopid = 0;
        for (int i = 0; i < VORTEXSIZE; i++) {
            if (marked[i]) continue;
            int chkpoint = i;
            Stack<Integer> checkApex = new Stack<Integer>();
            Stack<Integer> trace = new Stack<Integer>();
            checkApex.push(chkpoint);
            while (!checkApex.isEmpty()) {
                int apex = checkApex.pop();
                if (marked[apex]) continue;
                marked[apex] = true;
                circle[apex] = loopid;
                trace.push(apex);
                boolean isCompleteSeached = true;
                for (int adj : gph.adj(apex)) {
                    if (!marked[adj]) {
                        isCompleteSeached = false;
                        checkApex.push(adj);
                    }
                }
                if (isCompleteSeached) {
                    trace.pop();
                }
            }
            loopid++;
        }
    }


    public Iterable<Integer> connectedDFS() {
//        if (connected != null) return connected;
        resetMarked();
        connected = new Stack<Integer>();
        Stack<Integer> checkApex = new Stack<Integer>();
        Stack<Integer> trace = new Stack<Integer>();
        checkApex.push(checkPoint);
        edgeTo[checkPoint] = checkPoint;
        while (!checkApex.isEmpty()) {
            int apex = checkApex.pop();
            if (marked[apex]) continue;
            marked[apex] = true;
            if (!trace.isEmpty()) {
                edgeTo[apex] = trace.peek();
            }
            trace.push(apex);
            boolean isCompleteSeached = true;
            for (int adj : gph.adj(apex)) {
                if (!marked[adj]) {
                    isCompleteSeached = false;
                    checkApex.push(adj);
                }
            }
            if (isCompleteSeached) {
                trace.pop();
            }
        }
        for (int i = 0; i < VORTEXSIZE; i++) {
            if (marked[i]) {
                connected.push(i);
            }
        }
        return connected;
    }

    private void resetMarked() {
        for (int i = 0; i < VORTEXSIZE; i++) {
            marked[i] = false;
            edgeTo[i] = 0;
        }
    }


    public Iterable<Integer> connectedBFS() {
//        if (connected != null) return connected;
        resetMarked();
        Queue<Integer> checkApex = new Queue<Integer>();
        connected = new Stack<Integer>();
        checkApex.enqueue(checkPoint);
        edgeTo[checkPoint] = checkPoint;
        marked[checkPoint] = true;
        while (!checkApex.isEmpty()) {
            int apex = checkApex.dequeue();
            for (int adj : gph.adj(apex)) {
                if (!marked[adj]) {
                    checkApex.enqueue(adj);
                    marked[adj] = true;
                    edgeTo[adj] = apex;
                }
            }
        }
        for (int i = 0; i < VORTEXSIZE; i++) {
            if (marked[i]) {
                connected.push(i);
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
        edgeTo[s] = s;
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
                    edgeTo[adj] = apex;
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
        marked[s] = true;
        edgeTo[s] = s;
        while (!searchingApex.isEmpty()) {
            int apex = searchingApex.dequeue();
            if (apex == v) {
                int tmp = v;
                trace.push(tmp);
                while (edgeTo[tmp] != tmp) {
                    trace.push(edgeTo[tmp]);
                    tmp = edgeTo[tmp];
                }
                return trace;
            }
            for (int adj : gph.adj(apex)) {
                if (!marked[adj]) {
                    searchingApex.enqueue(adj);
                    edgeTo[adj] = apex;
                    marked[adj] = true;
                }
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

        srch.circlesDFS();

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
