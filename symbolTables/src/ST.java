public class ST<Key extends Comparable<Key>, Value> {
    Node root;
    int size;

    private class Node implements Comparable<Node> {
        private final Key key;
        private Value val;
        private Node right, left;

        Node(Key ky, Value v) {
            key = ky;
            val = v;
            right = null;
            left = null;
        }

        // does this equal y?
        @Override
        public boolean equals(Object y) {
            if (y == this) return true;
            if (y == null) return false;
            if (y.getClass() != this.getClass()) return false;
            Node that = (Node) y;
            return that.key.compareTo(this.key) == 0;
        }

        @Override
        public int compareTo(Node o) {
            return this.key.compareTo(o.getKey());
        }

        Value getVal() {
            return val;
        }

        void setVal(Value vl) {
            val = vl;
        }

        Key getKey() {
            return key;
        }
    }

    public ST() {
        root = null;
        size = 0;
        // Create a symbol table
    }

    public void put(Key key, Value val) {
        // put key-value pair into the table(remove key from table if value is null)
        if (root == null) {
            root = new Node(key, val);
            size++;
            return;
        }
        Node x = root;
        while (true) {
            if (x.getKey().compareTo(key) < 0) {
                if (x.right == null) {
                    x.right = new Node(key, val);
                    size++;
                    break;
                } else {
                    x = x.right;
                }
            } else if (x.getKey().compareTo(key) > 0) {
                if (x.left == null) {
                    x.left = new Node(key, val);
                    size++;
                    break;
                } else {
                    x = x.left;
                }
            } else {
                x.setVal(val);
                break;
            }
        }
    }

    private Node put(Node x, Key key, Value val) {
        if (x == null) return new Node(key, val);
        if (x.getKey().compareTo(key) < 0) return put(x.right, key, val);
        if (x.getKey().compareTo(key) > 0) return put(x.left, key, val);
        x.setVal(val);
        return x;
    }


    public Value get(Key key) {
        // value paired with key(null if key is absent)
        Node x = root;
        while (x != null) {
            int cmp = x.getKey().compareTo(key);
            if (cmp < 0) {
                x = x.right;
            } else if (cmp > 0) {
                x = x.left;
            } else {
                return x.getVal();
            }
        }
        return null;
    }


    public void delete(Key key) {
        // Delete key and its value from table
        // put(key, null);// Lazy version
        Elem<Key, Value> del_node = new Elem<Key, Value>(key, null);
        for (Elem<Key, Value> el : table) {
            if (el.equals(del_node)) {
                return el.getVal();
            }
        }
        return null;
    }


    public boolean contains(Key key) {
        // Is there a values paired with key?
        return get(key) != null;
    }

    public boolean isEmpty() {
        // Is the table empty?
        return size == 0;
    }

    public int size() {
        // Return the number of key-value pairs in the table
        return size;
    }

    Iterable<Key> keys() {
        // All the keys in the table. Return an iterable object
    }

    Iterable<Key> keys(Key lo, Key hi) {
        // Keys in [lo..hi], in sorted order
    }

    public int size(Key lo, Key hi) {
        // Number of keys in [lo..hi]
    }

    public void deleteMax() {

    }

    public void deleteMin() {

    }

    public Key select(int k) {
        // Key of rank k
    }

    public int rank(Key key) {

    }

    public Key ceiling(Key key) {
        // Smallest key greater than or equal to key
    }

    public Key floor(Key key) {
        // Largest key less than or equal to key
        if (root == null) return null;
        Node x = root;
        while (true) {
            int cmp = x.getKey().compareTo(key);
            if (cmp < 0) {
                if (x.right != null) {

                }
                if (x.right)
            } else if (cmp > 0) {

            } else {
                return x.getKey();
            }
        }

    }

    public Key max() {

    }

    public Key min() {

    }

    public static void main(String[] args) {

    }

}
