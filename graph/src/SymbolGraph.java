import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class SymbolGraph {
    private ST<String, Integer> st;
    private String[] keys;
    private Graph gph;


    public SymbolGraph(String filename, String delim) {
        In input = new In(filename);
        st = new ST<String, Integer>();


        while (input.hasNextLine()) {
            String[] line = input.readLine().split(delim);
            for (String tm : line) {
                if (!st.contains(tm)) {
                    st.put(tm, st.size());
                }
            }

        }

        keys = new String[st.size()];
        for (String name : st.keys()) {
            keys[st.get(name)] = name;
        }

        gph = new Graph(st.size());
        input = new In(filename);
        while (input.hasNextLine()) {
            String[] line = input.readLine().split(delim);
            gph.add_edge(st.get(line[0]), st.get(line[1]));
        }

    }

    public int degreesOfSeparation(String p1, String p2) {
        int idp1 = st.get(p1);
        int idp2 = st.get(p2);
        Stack<Integer> trace = gph.dfs(idp1, idp2);
        if (trace != null) return trace.size();
        else return Integer.MAX_VALUE;
    }

    public String chains(String p1, String p2) {
        StringBuilder chn = new StringBuilder();
        int idp1 = st.get(p1);
        int idp2 = st.get(p2);
        Stack<Integer> trace = gph.bfs(idp1, idp2);
        if (trace != null) {
            for (int tr : trace) {
                chn.append(keys[tr] + " ");
            }
            return chn.toString();
        }
        else {
            return String.format("%s and %s are unrelated", p1, p2);
        }
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < st.size(); i++) {
            str.append(keys[i] + ": ");
            for (int item : gph.adj(i)) {
                str.append(keys[item] + " ");
            }
            str.append("\n");
        }
        return str.toString();
    }

    public static void main(String[] args) {
        String delim = " ";
        if (args.length > 1) {
            delim = args[1];
        }
        SymbolGraph sgph = new SymbolGraph(args[0], delim);
        StdOut.print(sgph);
        StdOut.println(sgph.chains("LAX", "JFK"));
    }

    private class Graph {
        private Bag<Integer>[] adjacent;
        private final int VORTICESIZE;
        private int edgeSize;
        private int[] subsets;

        public Graph(int size) {
            VORTICESIZE = size;
            adjacent = (Bag<Integer>[]) new Bag[VORTICESIZE];
            subsets = new int[VORTICESIZE];
            for (int i = 0; i < VORTICESIZE; i++) {
                adjacent[i] = new Bag<Integer>();
                subsets[i] = Integer.MAX_VALUE;
            }
        }

        public void add_edge(int v, int w) {
            adjacent[v].add(w);
            adjacent[w].add(v);
            edgeSize++;
        }

        public Iterable<Integer> adj(int v) {
            return adjacent[v];
        }

        public int[] subsets() {
            boolean[] marked = new boolean[VORTICESIZE];
            int loop = 0;
            for (int i = 0; i < VORTICESIZE; i++) {
                marked[i] = false;
            }
            for (int i = 0; i < VORTICESIZE; i++) {
                if (marked[i]) continue;
                Queue<Integer> checkpoints = new Queue<Integer>();
                Stack<Integer> trace = new Stack<Integer>();
                checkpoints.enqueue(i);
                marked[i] = true;
                subsets[i] = loop;
                while (!checkpoints.isEmpty()) {
                    int apex = checkpoints.dequeue();
                    for (int adj : adjacent[apex]) {
                        if (!marked[adj]) {
                            subsets[adj] = loop;
                            marked[adj] = true;
                            checkpoints.enqueue(adj);
                        }
                    }
                }
                loop++;
            }
            return subsets;
        }

        public Stack<Integer> bfs(int p1, int p2) {
            boolean[] marked = new boolean[VORTICESIZE];
            int[] edgeTo = new int[VORTICESIZE];
            for (int i = 0; i < VORTICESIZE; i++) {
                marked[i] = false;
                edgeTo[i] = Integer.MAX_VALUE;
            }
            Queue<Integer> checkpoints = new Queue<Integer>();
            Stack<Integer> trace = new Stack<Integer>();
            checkpoints.enqueue(p1);
            edgeTo[p1] = p1;
            marked[p1] = true;
            while (!checkpoints.isEmpty()) {
                int apex = checkpoints.dequeue();
                if (apex == p2) {
                    int tmp = p2;
                    while (edgeTo[tmp] != tmp) {
                        trace.push(tmp);
                        tmp = edgeTo[tmp];
                    }
                    trace.push(tmp);
                    return trace;
                }

                for (int adj : adjacent[apex]) {
                    if (!marked[adj]) {
                        edgeTo[adj] = apex;
                        marked[adj] = true;
                        checkpoints.enqueue(adj);
                    }
                }
            }
            return null;
        }

        public Stack<Integer> dfs(int p1, int p2) {
            boolean[] marked = new boolean[VORTICESIZE];
            int[] edgeTo = new int[VORTICESIZE];
            for (int i = 0; i < VORTICESIZE; i++) {
                marked[i] = false;
                edgeTo[i] = Integer.MAX_VALUE;
            }
            Stack<Integer> checkpoints = new Stack<Integer>();
            Stack<Integer> trace = new Stack<Integer>();
            checkpoints.push(p1);
            edgeTo[p1] = p1;
            while (!checkpoints.isEmpty()) {
                int apex = checkpoints.pop();
                if (marked[apex]) continue;
                if (apex == p2) {
                    int tmp = p2;
                    while (edgeTo[tmp] != tmp) {
                        trace.push(tmp);
                        tmp = edgeTo[tmp];
                    }
                    trace.push(tmp);
                    return trace;
                }

                marked[apex] = true;
                for (int adj : adjacent[apex]) {
                    if (!marked[adj]) {
                        edgeTo[adj] = apex;
                        checkpoints.push(adj);
                    }
                }
            }
            return null;
        }
    }

}
