import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;

public class Sort {

    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    private static void exch(Comparable[] a, int i, int j) {
        Comparable swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

    private static boolean isSorted(Comparable[] a) {
        for (int i = 1; i < a.length; i++) {
            if (less(a[i], a[i - 1])) {
                return false;
            }
        }
        return true;
    }

    public static void selectSort(Comparable[] a) {
        StdOut.printf("%7s ", "");
        for (int pri = 0; pri < a.length; pri++) {
            StdOut.print(a[pri] + " ");
        }
        StdOut.print("\n");

        for (int i = 0; i < a.length; i++) {
            int minId = i;
            for (int j = i + 1; j < a.length; j++) {
                if (less(a[j], a[minId])) {
                    minId = j;
                }
            }
            exch(a, i, minId);
        }
    }

    public static void shellSort(Comparable[] a) {
        int len = a.length;
        int stepLen = 1;
        while (stepLen < len / 3) {
            stepLen = stepLen * 3 + 1;
        }
        while (stepLen >= 1) {
            for (int i = stepLen; i < a.length; i++) {
                int idx = i;
                while (idx - stepLen >= 0 && less(a[idx], a[idx - stepLen])) {
                    exch(a, idx - stepLen, idx);
                    idx -= stepLen;
                }
            }
            stepLen = stepLen / 3;
        }
    }

    public static void insertSort(Comparable[] a) {
        for (int i = 1; i < a.length; i++) {
            for (int j = i; j > 0; j--) {
                if (less(a[j], a[j - 1])) {
                    exch(a, j, j - 1);
                } else {
                    break;
                }
            }
        }
    }

    public static void mergeSort(Comparable[] a) {
        Comparable[] aux = Arrays.copyOf(a, a.length);
        partSort(a, aux, 0, a.length);

    }

    private static void partSort(Comparable[] a, Comparable[] aux, int lo, int hi) {
        if ((hi - lo) <= 1) {
            return;
        }
        partSort(a, aux, lo, (lo + hi) / 2);
        partSort(a, aux, (lo + hi) / 2, hi);
        int ida = lo;
        int idb = (lo + hi) / 2;
        for (int i = lo; i < hi; i++) {
            aux[i] = a[i];
        }

        for (int i = lo; i < hi; i++) {
            if (ida >= (lo + hi) / 2) a[i] = aux[idb++];
            else if (idb >= hi) a[i] = aux[ida++];
            else if (less(aux[ida], aux[idb])) a[i] = aux[ida++];
            else a[i] = aux[idb++];
        }
        printResult(lo + "-" + hi, a);
    }

    public static void quickSort(Comparable[] a) {
        StdRandom.shuffle(a);
        quickScan(a, 0, a.length);

    }

    private static void quickScan(Comparable[] a, int lo, int hi) {
        if (hi - lo < 2) return;
        int left = lo;
        int rigt = hi;
        while (true) {
            while (less(a[lo], a[--rigt])) {
                if (rigt == left) break;
            }
            while (less(a[++left], a[lo])) {
                if (left == rigt) break;
            }
            if (left >= rigt) break;
            exch(a, left, rigt);
        }
        exch(a, lo, rigt);
        printResult(lo + "" + hi, a);
        quickScan(a, lo, rigt);
        quickScan(a, rigt + 1, hi);
    }

    private static void printResult(String id, Object[] list) {
        StdOut.printf("%5s", id);
        for (Object ikey : list) {
            StdOut.printf("%2s", ikey.toString());
        }
        StdOut.print("\n");
    }

    public static void main(String[] args) {
        /*In inFile = new In(args[0]);
        Queue<Integer> inQueue = new Queue<Integer>();
        while (!inFile.isEmpty()) {
            inQueue.enqueue(inFile.readInt());
        }
        int[] inList = new int[inQueue.size()];
        for (int i = 0; i < inList.length; i++) {
            inList[i] = inQueue.dequeue();
        }*/

        String[] testList = args;
//        shellSort(testList);
        printResult(" ", testList);
//        mergeSort(testList);
        quickSort(testList);
        printResult(" ", testList);
//        selectSort(testList);

/*        for (String ikey : testList) {
            StdOut.println(ikey);
        }*/
    }
}
