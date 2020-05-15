import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray {
    private final int LENGTH;
    private int[] index;

    private class SubString implements Comparable<SubString> {
        char[] chars;
        final int begin;
        final int length;

        SubString(char[] str, int begin, int length) {
            this.chars = str;
            this.begin = begin;
            this.length = length;
        }

        char get(int i) {
            return chars[(i + begin) % this.length];
        }

        @Override
        public int compareTo(SubString that) {
            for (int i = 0; i < this.length; i++) {
                int del = chars[(i + begin) % this.length] - that.chars[(i + that.begin) % this.length];
                if (del != 0) return del;
            }
            return 0;
        }
    }

    private void msd(SubString[] arr, int ichar, int start, int end) {
        if (ichar >= arr.length) return;
        int[] count = new int[256 + 1];
        SubString[] assArr = new SubString[end - start];
        for (int i = start; i < end; i++) {
            count[arr[i].get(ichar) + 1]++;
        }
        for (int i = 0; i < count.length - 1; i++) {
            count[i + 1] += count[i];
        }
        for (int i = start; i < end; i++) {
            assArr[count[arr[i].get(ichar)]] = arr[i];
            count[arr[i].get(ichar)]++;
        }
        for (int i = start; i < end; i++) {
            arr[i] = assArr[i - start];
        }

        if (count[0] > 1) msd(arr, ichar + 1, 0 + start, count[0] + start);
        for (int i = 1; i < count.length; i++) {
            if (count[i] - count[i - 1] > 1) msd(arr, ichar + 1, count[i - 1] + start, count[i] + start);
        }

    }


    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException();
        LENGTH = s.length();
        index = new int[LENGTH];
        char[] asisStr = s.toCharArray();


        SubString[] strarry = new SubString[LENGTH];
        for (int i = 0; i < strarry.length; i++) {
            strarry[i] = new SubString(asisStr, i, LENGTH);
        }
        msd(strarry, 0, 0, strarry.length);

        for (int i = 0; i < strarry.length; i++) {
            index[i] = strarry[i].begin;
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
        StdOut.println(csarray.length());
        for (int i = 0; i < csarray.length(); i++) {
            StdOut.print(csarray.index(i) + " ");
        }
    }

}
