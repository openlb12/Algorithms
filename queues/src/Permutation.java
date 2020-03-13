import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        int optSize = Integer.parseInt(args[0]);
        RandomizedQueue<String> text = new RandomizedQueue<String>();
        // In inpt = new In(args[1]);
        while (!StdIn.isEmpty()) {
            text.enqueue(StdIn.readString());
        }
        if (optSize > text.size()) {
            throw new IllegalArgumentException("Invalidated output size: " + optSize);
        }
        int idx = 0;
        for (String ikey : text) {
            if (idx >= optSize) {
                break;
            }
            StdOut.println(ikey);
            ++idx;

        }

    }
}
