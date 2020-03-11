public class Queues<Item> {
    private Node first, last;

    private class Node {
        Item item;
        Node next;
    }

    public boolean isEmpty() {
        return first == null;
    }

    public void enqueue(Item elem) {
        Node oldLast = last;
        last = new Node();
        last.item = elem;
        last.next = null;
        if (isEmpty()) first = last;
        else oldLast.next = last;
    }

    public Item dequeue() {
        Item elem = first.item;
        first = first.next;
        if (isEmpty()) last = null;
        return elem;

    }
}

class QueueOfStacks<Item> {

}
