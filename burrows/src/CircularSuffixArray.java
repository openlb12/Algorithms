import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class CircularSuffixArray {
    private final int LENGTH;
    private int[] index;

    private class SubString implements Comparable<SubString> {
        char[] chars;
        final int begin;
        final int length;
        final int index;

        SubString(char[] str, int begin, int length, int index) {
            this.chars = str;
            this.begin = begin;
            this.length = length;
            this.index = index;
        }

        char get(int i) {
            return chars[i + begin];
        }

        @Override
        public int compareTo(SubString that) {
            for (int i = 0; i < this.length; i++) {
                int del = chars[(i + begin) % LENGTH] - that.chars[(i + that.begin) % LENGTH];
                if (del != 0) return del;
            }
            return 0;
        }
    }


    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException();
        LENGTH = s.length();
        index = new int[LENGTH];
        char[] asisStr = s.toCharArray();


        SubString[] strarry = new SubString[LENGTH];
        for (int i = 0; i < LENGTH; i++) {
            strarry[i] = new SubString(asisStr, i, LENGTH, i);
        }
        Arrays.sort(strarry);
        for (int i = 0; i < LENGTH; i++) {
            index[i] = strarry[i].index;
        }
    }

    // length of s
    public int length() {
        return this.LENGTH;
    }

    private boolean isValid(int i) {
        return (i >= 0 && i < this.LENGTH);
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (!isValid(i)) throw new IllegalArgumentException();
        return index[i];
    }


    // unit testing (required)
    public static void main(String[] args) {
        CircularSuffixArray csarray = new CircularSuffixArray(args[0]);
        StdOut.print(csarray);
    }

}
