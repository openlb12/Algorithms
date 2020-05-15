import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.Arrays;

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
//            StdOut.print(first);
            String str = BinaryStdIn.readString();
            int length = str.length();
//            StdOut.print(first);
            Elem[] sortedStr = new Elem[length];
            for (int i = 0; i < length; i++) {
                sortedStr[i] = new Elem(str.charAt(i), i);
//                StdOut.print(str.charAt(i));
            }
            Arrays.sort(sortedStr);

            BinaryStdOut.write(sortedStr[first].key, 8);
//            StdOut.print(sortedStr[first].key);
            int index = sortedStr[first].next;
            int len = 1;
            while (len < length) {
                BinaryStdOut.write(sortedStr[index].key, 8);
//                StdOut.print(sortedStr[index].key);
                index = sortedStr[index].next;
                len++;
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
