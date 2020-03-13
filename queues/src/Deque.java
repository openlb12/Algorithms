import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int size;


    private class Node {
        Item elem;
        Node next;
        Node previous;
    }

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {

        if (item == null) {
            throw new IllegalArgumentException("wrong input of addFirst function: null");
        }

        Node oldFirst = first;
        first = new Node();
        first.elem = item;
        if (isEmpty()) {
            last = first;
        } else {
            first.next = oldFirst;
            oldFirst.previous = first;
        }
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("wrong input of addLast function: null");
        }

        Node oldLast = last;
        last = new Node();
        last.elem = item;
        if (first == null) {
            first = last;
        } else {
            oldLast.next = last;
            last.previous = oldLast;
        }
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("Try to remove empty list");
        }
        Item elem = first.elem;
        first = first.next;
        if (size > 1) {
            first.previous = null;
        } else {
            first = null;
            last = null;
        }
        size--;
        return elem;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("Try to remove empty list");
        }
        Item elem = last.elem;
        last = last.previous;
        if (size > 1) {
            last.next = null;
        } else {
            first = null;
            last = null;
        }
        size--;
        return elem;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new ListIterator();

    }

    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        @Override
        public boolean hasNext() {
            return current != null;
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
            Item elem = current.elem;
            current = current.next;
            return elem;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        In input = new In(args[0]);
        Deque<String> text = new Deque<String>();
        String[] inputString = input.readAllStrings();

        for (String ikey : inputString) {
            text.addFirst(ikey);

        }

        for (String ikey : text) {
            StdOut.print(ikey + " ");

        }

        StdOut.println();

        for (String ikey : inputString) {
            text.addLast(ikey);

        }

        for (String ikey : text) {
            StdOut.print(ikey + " ");

        }
        StdOut.println();

        text.removeFirst();
        for (String ikey : text) {
            StdOut.print(ikey + " ");
        }
        StdOut.println();
        text.removeLast();
        for (String ikey : text) {
            StdOut.print(ikey + " ");
        }
        StdOut.println();

        while (!text.isEmpty()) {
            text.removeFirst();
            StdOut.print(text.size);
            for (String ikey : text) {
                StdOut.print(ikey + " ");
            }
            StdOut.println();
        }

    }

}
