import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private static final int DATA_SIZE = 8;

    private Item[] data;
    private int end;
    private int capacity;


    // construct an empty randomized queue
    public RandomizedQueue() {
        capacity = DATA_SIZE;
        data = (Item[]) new Object[capacity];
        end = 0;

    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return end == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return end;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Invalidated enqueue argument");
        }
        if (end >= capacity) {
            extendData();
        }
        data[end++] = item;


    }

    // resize data array
    private void extendData() {
        capacity *= 2;
        Item[] tmpData = (Item[]) new Object[capacity];
        for (int idx = 0; idx < end; idx++) {
            tmpData[idx] = data[idx];
        }
        data = tmpData;

    }

    // resize data array
    private void shrinkData() {
        if (capacity <= DATA_SIZE) return;
        capacity /= 2;
        Item[] tmpData = (Item[]) new Object[capacity];
        for (int idx = 0; idx < end; idx++) {
            tmpData[idx] = data[idx];
        }
        data = tmpData;

    }


    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Empty queue");
        }
        int id = StdRandom.uniform(end);
        Item elem = data[id];
        for (int idx = id; idx < end - 1; idx++) {
            data[idx] = data[idx + 1];
        }
        end--;
        if (end < capacity / 4 && capacity > DATA_SIZE) shrinkData();
        return elem;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException("Empty queue");
        }
        int id = StdRandom.uniform(end);
        return data[id];
    }

    private class ListIterator implements Iterator<Item> {
        private int iterLoc = 0;
        private final int[] ranOrd = StdRandom.permutation(end);

//        public void ListIterator(int[] ranSeq) {
//            ranOrd = ranSeq;
//            iterLoc = 0;
//        }

        @Override

        public boolean hasNext() {
            return iterLoc < end;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Iterator doesn't support remove operation");
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("End of list");
            }
            return data[ranOrd[iterLoc++]];
        }
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new ListIterator();

    }

    // unit testing (required)
    public static void main(String[] args) {

        int optSize = Integer.parseInt(args[0]);
        In input = new In(args[1]);
        RandomizedQueue<String> list = new RandomizedQueue<String>();
        for (String ikey : input.readAllStrings()) {
            StdOut.print(ikey + " ");
            list.enqueue(ikey);
        }
        StdOut.println(list.size());
        for (String ikey : list) {
            StdOut.print(ikey + " ");
        }
        StdOut.println();

        for (int idx = 0; idx < optSize; idx++) {
            StdOut.print(list.dequeue() + " ");
        }
        StdOut.println();

        for (String ikey : list) {
            StdOut.print(ikey + " ");
        }
        StdOut.println();


    }

}
