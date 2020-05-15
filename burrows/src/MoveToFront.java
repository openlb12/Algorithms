import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int ASCII_SIZE = 256;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] index = new char[ASCII_SIZE];
        for (int i = 0; i < ASCII_SIZE; i++) {
            index[i] = (char) i;
        }
        while (!BinaryStdIn.isEmpty()) {
            char ch = BinaryStdIn.readChar(8);
            int loc = 0;
            char currenCh = index[loc];
            char oldCh = index[loc];
            while (currenCh != ch) {
                oldCh = currenCh;
                currenCh = index[++loc];
                index[loc] = oldCh;
            }
            index[0] = ch;
            BinaryStdOut.write(loc, 8);
//            BinaryStdOut.write(' ');

//            StdOut.print(String.format("%d ", loc));

        }
        BinaryStdOut.flush();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] index = new char[ASCII_SIZE];
        for (int i = 0; i < ASCII_SIZE; i++) {
            index[i] = (char) i;
        }
        while (!BinaryStdIn.isEmpty()) {
            char ch = BinaryStdIn.readChar(8);
            char dech = index[ch];
            for (int i = ch; i > 0; i--) {
                index[i] = index[i - 1];
            }
            index[0] = dech;
            BinaryStdOut.write(dech);
//            BinaryStdOut.write(' ');

//            StdOut.print(String.format("%s ", dech));

        }
        BinaryStdOut.flush();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {

        if (args.length >= 1) {
            char ch = args[0].trim().charAt(0);
            if (ch == '-') {
                encode();
            } else if (ch == '+') {
                decode();
            } else {
                throw new IllegalArgumentException();
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

}
