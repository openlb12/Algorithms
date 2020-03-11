public class Stacks<Item> {
    private Node first = null;

    private class Node {
        Item item;
        Node next;
    }

    public boolean isEmpty() {
        return first == null;
    }

    public void push(Item elem) {
        Node oldFirst = first;
        first = new Node;
        first.item = elem;
        first.next = oldFirst;
    }

    public Item pop() {
        Item elem = first.item;
        first = first.next;
    }
}
