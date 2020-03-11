import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        int optSize = Integer.parseInt(args[0]);
        RandomizedQueue<String> text = new RandomizedQueue<String>();
        for (String ikey : StdIn.readAllStrings()) {
            text.enqueue(ikey);
        }
        if (optSize > text.size()) {
            throw new IllegalArgumentException("Invalidated output size: " + String.valueOf(optSize));
        }
        for (int idx = 0; idx < optSize; idx++) {
            StdOut.print(text.sample() + " ");
        }
        StdOut.println();

    }
}
