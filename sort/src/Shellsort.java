import edu.princeton.cs.algs4.StdOut;

public class Shellsort {

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

            StdOut.printf("%3d ", i);
            StdOut.printf("%3d ", minId);
            for (int pri = 0; pri < a.length; pri++) {
                StdOut.print(a[pri] + " ");
            }
            StdOut.print("\n");
        }
    }

    public static void shellSort(Comparable[] a) {

        int len = a.length;
        int stepLen = 1;
        while (stepLen < len / 3) {
            stepLen = stepLen * 3 + 1;
        }

        StdOut.printf("%3s ", " ");
        for (int pri = 0; pri < a.length; pri++) {
            StdOut.print(a[pri] + " ");
        }
        StdOut.print("\n");

        while (stepLen >= 1) {
            for (int i = stepLen; i < a.length; i++) {
                int idx = i;
                while (idx - stepLen >= 0 && less(a[idx], a[idx - stepLen])) {
                    exch(a, idx - stepLen, idx);
                    idx -= stepLen;
                }
            }
            StdOut.printf("%3d ", stepLen);
            for (int pri = 0; pri < a.length; pri++) {
                StdOut.print(a[pri] + " ");
            }
            StdOut.print("\n");

            stepLen = stepLen / 3;
        }
    }

    public static void insertSort(Comparable[] a) {


        for (int pri = 0; pri < a.length; pri++) {
            StdOut.print(a[pri] + " ");
        }
        StdOut.print("\n");

        for (int i = 1; i < a.length; i++) {
            for (int j = i; j > 0; j--) {
                if (less(a[j], a[j - 1])) {
                    exch(a, j, j - 1);

                } else {
                    break;
                }
            }

            for (int pri = 0; pri < a.length; pri++) {
                StdOut.print(a[pri] + " ");
            }
            StdOut.print("\n");
        }

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
        shellSort(testList);
//        selectSort(testList);

/*        for (String ikey : testList) {
            StdOut.println(ikey);
        }*/
    }
}
