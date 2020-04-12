public class RedBlackBST<Key extends Comparable<Key>, Value> {
    private Node root;
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private class Node implements Comparable<Node> {
        Key key;
        Value val;
        int count;
        Node left, right;
        Boolean color;

        public Node(Key key, Value val) {
            this.key = key;
            this.val = val;
            this.left = null;
            this.right = null;
            this.count = 1;
            this.color = RED;
        }

        // does this equal that?
        @Override
        public boolean equals(Object x) {
            if (x == this) return true;
            if (x == null) return false;
            if (x.getClass() != this.getClass()) return false;
            Node cast = (Node) x;
            return cast.key.compareTo(this.key) == 0;
        }

        @Override
        public int compareTo(Node x) {
            return this.key.compareTo(x.key);
        }

    }

    public RedBlackBST() {
        root = null;
    }

    public void put(Key key, Value val) {
        root = put(root, key, val);
    }

    private Node put(Node x, Key key, Value val) {
        if (x == null) return new Node(key, val);
        int keyCompare = x.key.compareTo(key);
        if (keyCompare < 0) {
            x.right = put(x.right, key, val);
        } else if (keyCompare > 0) {
            x.left = put(x.left, key, val);
        } else {
            x.val = val;
        }
        x.count = 1 + size(x.right) + size(x.left);
        return x;
    }

    private int size(Node x) {
        if (x == null) return 0;
        return x.count;
    }

    public int size() {
        return size(root);
    }


}
