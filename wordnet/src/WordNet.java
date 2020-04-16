import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.StdOut;

public class WordNet {

    private RedBlackBST<String, Synset> synsetStack;
    private Bag<Integer>[] adj;
    private int synSize;

    private class Synset {
        private String def;
        private int id;

        public Synset(int id, String gloss) {
            this.def = gloss;
            this.id = id;
        }
    }

    private
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();
        synsetStack = new RedBlackBST<String, Synset>();
        In synInput = new In(synsets);
        while (synInput.hasNextLine()) {
            String[] synLine = synInput.readLine().split(",");
            synsetStack.put(synLine[1], new Synset(Integer.parseInt(synLine[0]), synLine[2]));
            synSize++;
        }
        adj = (Bag<Integer>[]) new Bag[synSize];
        for (int i = 0; i < synSize; i++) {
            adj[i] = new Bag<Integer>();
        }
        In hypernymsInput = new In(hypernyms);
        while (hypernymsInput.hasNextLine()) {
            String[] hypernymLink = hypernymsInput.readLine().split(",");
            for (int i = 1; i < hypernymLink.length; i++) {
                adj[Integer.parseInt(hypernymLink[0])].add(Integer.parseInt(hypernymLink[i]));
            }
        }


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
        if (nounA == null || nounB == null) throw new IllegalArgumentException();
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        return 0;

    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new IllegalArgumentException();
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        return "";
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
