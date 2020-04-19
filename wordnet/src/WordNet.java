import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class WordNet {

    private RedBlackBST<String, Stack<Integer>> synsetStack;
    private Digraph dgph;
    private String[] keys;
    private final int synSize;
    private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException("No input file");

        synsetStack = new RedBlackBST<String, Stack<Integer>>();
        In synInput = new In(synsets);
        int inputSize = 0;
        while (synInput.hasNextLine()) {
            String[] synLine = synInput.readLine().split(",");
            String[] nouns = synLine[1].split(" ");
            int id = Integer.parseInt(synLine[0]);
            Stack<Integer> ids;
            for (String key : nouns) {
                if (synsetStack.contains(key)) {
                    ids = synsetStack.get(key);
                    ids.push(id);
                }
                else {
                    ids = new Stack<Integer>();
                    ids.push(id);
                }
                synsetStack.put(key, ids);
            }
            inputSize++;
        }
        synSize = inputSize;
        keys = new String[synSize];
        for (int id = 0; id < synSize; id++) {
            keys[id] = "";
        }
        for (String nym : synsetStack.keys()) {
            for (int i : synsetStack.get(nym)) {
                keys[i] += nym + " ";
            }
        }

        dgph = new Digraph(synSize);
        In hypernymsInput = new In(hypernyms);
        while (hypernymsInput.hasNextLine()) {
            String[] hypernymLink = hypernymsInput.readLine().split(",");
            int hypo = Integer.parseInt(hypernymLink[0]);
            for (int i = 1; i < hypernymLink.length; i++) {
                int hyper = Integer.parseInt(hypernymLink[i]);
                this.dgph.addEdge(hypo, hyper);
            }
        }
        sap = new SAP(dgph);
        if (isCycled() || !isRooted())
            throw new IllegalArgumentException("Input is not a rooted DAG");

    }

    private boolean isRooted() {
        int rootNum = 0;
        for (int i = 0; i < synSize; i++) {
            if (dgph.outdegree(i) == 0) rootNum++;
        }
        return rootNum == 1;
    }

    private boolean isSelfLooped() {
        // if the adjacent vertice contains itself, then it is selflooped.
        for (int i = 0; i < synSize; i++) {
            for (int j : dgph.adj(i)) {
                if (i == j) return true;
            }
        }
        return false;
    }


    private boolean isCycled() {
        if (isSelfLooped()) return true;
        boolean[] marked = new boolean[synSize];
        // boolean[] visited = new boolean[VERTICESIZE];
        int[] edgeTo = new int[synSize];
        Stack<Integer> checkpoints = new Stack<Integer>();
        for (int i = 0; i < synSize; i++) {
            if (marked[i]) continue;
            // visited[i] = true;
            checkpoints.push(i);
            edgeTo[i] = i;
            while (!checkpoints.isEmpty()) {
                int apex = checkpoints.pop();
                if (marked[apex]) {
                    Stack<Integer> cycle = new Stack<Integer>();
                    cycle.push(apex);
                    int tmp = apex;
                    while (edgeTo[tmp] != tmp) {
                        tmp = edgeTo[tmp];
                        cycle.push(tmp);
                    }
                    if (cycle.peek() == apex) return true;
                    else continue;
                }
                marked[apex] = true;
                for (int adj : dgph.adj(apex)) {
                    if (adj == i) return true;
                    else {
                        checkpoints.push(adj);
                        // visited[adj] = true;
                        edgeTo[adj] = apex;
                    }
                }
            }
        }
        return false;
    }


    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return synsetStack.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        return synsetStack.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        return sap.length(synsetStack.get(nounA), synsetStack.get(nounB));

    }

    // a synset (second field of synsets.txt) that is the common ancestor
    // of nounA and nounB in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        int ancestor = sap.ancestor(synsetStack.get(nounA), synsetStack.get(nounB));
        if (ancestor == -1) return null;
        return keys[ancestor];
    }

    // do unit testing of this class
    public static void main(String[] args) {
        if (args == null) throw new IllegalArgumentException();
        //        StdOut.print("Test");
        String synsets = args[0];
        String hypernyms = args[1];
        WordNet dict = new WordNet(synsets, hypernyms);
        for (String st : dict.nouns()) {
            StdOut.print(st + ", ");
        }
    }
}
