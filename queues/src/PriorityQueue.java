import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class PriorityQueue<Key extends Comparable<Key>> implements Iterable<Key> {
    private int size;
    private int capacity;
    private Key[] list;
    private final int elementCapacity = 64;

    private void exch(int i, int j) {
        Key tmp = list[i];
        list[i] = list[j];
        list[j] = tmp;
    }

    private void swim(int k) {
        while (k > 1 && (list[k].compareTo(list[k / 2]) > 0)) {
            exch(k, k / 2);
            k = k / 2;
        }
    }


    private void sink(int k) {
        while (2 * k <= size) {
            int j = 2 * k;
            if (j < size && list[j].compareTo(list[j + 1]) < 0) j++;
            if (list[k].compareTo(list[j]) > 0) break;
            exch(k, j);
            k = j;
        }
    }

    private void resize() {
        Key[] tmp = (Key[]) new Comparable[capacity * 2];
        capacity *= 2;
        if (size >= 0) System.arraycopy(list, 1, tmp, 1, size);
        list = tmp;
    }

    private void shrink() {
        Key[] tmp = (Key[]) new Comparable[capacity / 2];
        capacity /= 2;
        if (size >= 0) System.arraycopy(list, 1, tmp, 1, size);
        list = tmp;
    }


    public PriorityQueue() {
        // Create an empty priority queue
        capacity = elementCapacity;
        list = (Key[]) new Comparable[capacity];
        size = 0;
    }

    public void insert(Key v) {
        // Insert key to the priority queue
        if (size >= capacity - 1) resize();
        list[++size] = v;
        swim(size);
    }

    public void unorderInsert(Key v) {
        // Insert key to the end of the queue
        if (size >= capacity - 1) resize();
        list[++size] = v;
    }

    public void sort() {
        for (int i = size; i > 0; i--) {
            sink(i);
        }
    }


    public Key delMax() {
        // pop the largest key
        if (isEmpty()) throw new IndexOutOfBoundsException("Delete max from an empty queue");
        Key max = list[1];
        exch(1, size--);
        sink(1);
        list[size + 1] = null;
        if (capacity > elementCapacity && size < capacity / 4) shrink();
        return max;
    }

    public boolean isEmpty() {
        // is the queue empty
        return size == 0;
    }

    public Key max() {
        // Return the largest key
        if (size > 0) return list[1];
        return null;
    }

    public int size() {
        // Return the size of the queue
        return size;
    }

    @Override
    public Iterator<Key> iterator() {
        return new SortIterator();
    }

    private class SortIterator implements Iterator<Key> {

        private int end = size;

        @Override
        public boolean hasNext() {
            return end >= 1;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Iterator doesn't support remove operation");
        }

        @Override
        public Key next() {
            if (!hasNext()) {
                throw new NoSuchElementException("End of list");
            }
            Key val = list[1];
            exch(1, end--);
            sink(1);
            return val;

        }
    }

    private class ListIterator implements Iterator<Key> {
        private Key current = list[1];
        private int idx = 1;

        @Override
        public boolean hasNext() {
            return idx <= size;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Iterator doesn't support remove operation");
        }

        @Override
        public Key next() {
            if (!hasNext()) {
                throw new NoSuchElementException("End of list");
            }
            return list[idx++];
        }
    }


    public static void main(String[] args) {
        // Test module
        int M = Integer.parseInt(args[0]);
        In input = new In("./tinyBatch.txt");
        PriorityQueue<Transaction> transactionPQueue = new PriorityQueue<Transaction>();
        while (input.hasNextLine()) {
            String[] inputlist = input.readLine().split(" ");
            String name = inputlist[0];
            Date dt = new Date(inputlist[1]);
            double amount = Double.parseDouble(inputlist[2]);
            transactionPQueue.unorderInsert(new Transaction(name, dt, amount));
        }
//        for (Transaction it : transactionPQueue) {
//            StdOut.println(it.toString());
//        }
//        StdOut.println();
        transactionPQueue.sort();
        for (Transaction it : transactionPQueue) {
            StdOut.println(it.toString());
        }
        StdOut.println();

        int id = 0;
        while (id++ < M) {
            StdOut.println(transactionPQueue.delMax().toString());
        }
        StdOut.println();
    }


}


final class Transaction implements Comparable<Transaction> {
    private final String name;
    private final Date date;
    private final double transAmount;

    public Transaction(String nm, Date dt, double ta) {
        name = nm;
        date = dt;
        transAmount = ta;
    }

    public double getTransAmount() {
        return transAmount;
    }

    @Override
    public String toString() {
        return String.format("%10s   %s  %8.2f", name, date.toString(), transAmount);
    }

    @Override
    public int compareTo(Transaction o) {
        if (this == o) return 0;
        double tmp = this.transAmount - o.getTransAmount();
        if (tmp > 0) return 1;
        if (tmp < 0) return -1;
        return 0;
    }

}

class Date implements Comparable<Date> {
    private final int year;
    private final int month;
    private final int day;

    public Date(int mth, int dt, int yr) {
        year = yr;
        month = mth;
        day = dt;
    }

    public Date(String date) {
        String[] dt = date.split("/");
        year = Integer.parseInt(dt[2]);
        month = Integer.parseInt(dt[0]);
        day = Integer.parseInt(dt[1]);
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    @Override
    public int compareTo(Date o) {
        if (this == o) return 0;
        if (this.year != o.getYear()) return this.year - o.getYear();
        if (this.month != o.getMonth()) return this.month - o.getMonth();
        if (this.day != o.getDay()) return this.day - o.getDay();
        return 0;
    }

    @Override
    public String toString() {
        return String.format("%2d/%2d/%4d", month, day, year);
    }
}
