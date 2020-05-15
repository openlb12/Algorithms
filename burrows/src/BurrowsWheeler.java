import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.Stack;

public class BurrowsWheeler {

    private static class Elem implements Comparable<Elem> {
        char key;
        int next;

        public Elem(char key, int next) {
            this.key = key;
            this.next = next;
        }

        @Override
        public int compareTo(Elem that) {
            return this.key - that.key;
        }
    }

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        char[] transStr;
        while (!BinaryStdIn.isEmpty()) {
            String oriStr = BinaryStdIn.readString();
            CircularSuffixArray csArray = new CircularSuffixArray(oriStr);
            transStr = new char[csArray.length()];
            int index = 0;
            for (int i = 0; i < csArray.length(); i++) {
                int loc = csArray.index(i);
                if (loc == 0) index = i;
                transStr[i] = oriStr.charAt((csArray.length() - 1 + loc) % csArray.length());
            }
            BinaryStdOut.write(index);
            for (char ch : transStr) BinaryStdOut.write(ch);
        }
        BinaryStdOut.flush();
    }


    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        while (!BinaryStdIn.isEmpty()) {
            int first = BinaryStdIn.readInt();
            String str = BinaryStdIn.readString();

            int[] count = new int[256 + 1];
            int[] sortedIndex = new int[str.length()];


            for (int i = 0; i < str.length(); i++) {
                count[str.charAt(i) + 1]++;
            }

            for (int i = 1; i < count.length; i++) {
                count[i] += count[i - 1];
            }

            for (int i = 0; i < str.length(); i++) {
                sortedIndex[i] = count[str.charAt(i)]++;
            }

            int index = first;
            int len = 0;
            Stack<Character> st = new Stack<Character>();
            while (len < str.length()) {
                st.push(str.charAt(index));
                index = sortedIndex[index];
                len++;
            }
            while (!st.isEmpty()) {
                BinaryStdOut.write(st.pop(), 8);
            }

        }
        BinaryStdOut.flush();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args.length >= 1) {
            if (args[0].trim().equals("-")) {
                transform();
            } else if (args[0].trim().equals("+")) {
                inverseTransform();
            } else {
                throw new IllegalArgumentException();
            }
        } else {
            throw new IllegalArgumentException();
        }
    }
}
