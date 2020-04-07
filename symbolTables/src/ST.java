import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class ST<Key extends Comparable<Key>, Value> {
    Node root;

    private class Node implements Comparable<Node> {
        private final Key key;
        private Value val;
        private Node right, left;
        private int count;

        Node(Key ky, Value v) {
            key = ky;
            val = v;
            count = 1;
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

        int getCount() {
            return count;
        }

        void setCount(int cnt) {
            count = cnt;
        }
    }

    public ST() {
        root = null;
        // Create a symbol table
    }

//    public void put(Key key, Value val) {
//        // put key-value pair into the table(remove key from table if value is null)
//        if (root == null) {
//            root = new Node(key, val);
//            size++;
//            return;
//        }
//        Node x = root;
//        while (true) {
//            if (x.getKey().compareTo(key) < 0) {
//                if (x.right == null) {
//                    x.right = new Node(key, val);
//                    size++;
//                    break;
//                } else {
//                    x = x.right;
//                }
//            } else if (x.getKey().compareTo(key) > 0) {
//                if (x.left == null) {
//                    x.left = new Node(key, val);
//                    size++;
//                    break;
//                } else {
//                    x = x.left;
//                }
//            } else {
//                x.setVal(val);
//                break;
//            }
//        }
//    }

    public void put(Key key, Value val) {
        root = put(root, key, val);
    }

    private Node put(Node x, Key key, Value val) {
        if (x == null) return new Node(key, val);
        int keyCompare = x.getKey().compareTo(key);
        if (keyCompare < 0) {
            x.right = put(x.right, key, val);
        } else if (keyCompare > 0) {
            x.left = put(x.left, key, val);
        } else {
            x.setVal(val);
        }
        x.setCount(1 + size(x.right) + size(x.left));
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

    private Node delete(Node x, Key key) {
        int compareKey = x.getKey().compareTo(key);
        if (compareKey < 0) x.right = delete(x.right, key);
        else if (compareKey > 0) x.left = delete(x.left, key);
        else {
            if (x.right == null) {
                x
            }
        }
    }

//    public void delete(Key key) {
//        // Delete key and its value from table
//        // put(key, null);// Lazy version
//        Elem<Key, Value> del_node = new Elem<Key, Value>(key, null);
//        for (Elem<Key, Value> el : table) {
//            if (el.equals(del_node)) {
//                return el.getVal();
//            }
//        }
//        return null;
//    }


    public boolean contains(Key key) {
        // Is there a values paired with key?
        return get(key) != null;
    }

    public boolean isEmpty() {
        // Is the table empty?
        return root == null;
    }

    public int size() {
        // Return the number of key-value pairs in the table
        return size(root);
    }

    private int size(Node x) {
        if (x == null) return 0;
        else return x.getCount();
    }

    Iterable<Key> keys() {
        // All the keys in the table. Return an iterable object
        if (root == null) return null;
        Stack<Key> list = new Stack<Key>();
        Stack<Node> ndlist = new Stack<Node>();
        ndlist.push(root);
        while (!ndlist.isEmpty()) {
            Node rt = ndlist.pop();
            Node tmp = rt;
            while (tmp.right != null) {
                ndlist.push(tmp.right);
                tmp = tmp.right
            }
        }
    }
//
//    Iterable<Key> keys(Key lo, Key hi) {
//        // Keys in [lo..hi], in sorted order
//    }
//
//    public int size(Key lo, Key hi) {
//        // Number of keys in [lo..hi]
//    }

    private Node deleteMax(Node x) {
        if (x.right == null) return x.left;
        else {
            x = deleteMax(x.right);
            x.setCount(size(x.left) + size(x.right) + 1);
            return x;
        }
    }

    public void deleteMax() {
        if (root == null) return;
        root = deleteMax(root);
    }

    private Node deleteMin(Node x) {
        if (x.left == null) return x.right;
        else {
            x = deleteMin(x.left);
            x.setCount(size(x.left) + size(x.right) + 1);
            return x;
        }
    }

    public void deleteMin() {
        if (root == null) return;
        root = deleteMin(root);
    }

    private Key select(Node x, int rank) {
//        if (x == null || rank == 0) return null;
        int leftNodeSize = size(x.left);
        if (leftNodeSize + 1 < rank) return select(x.right, rank - leftNodeSize - 1);
        else if (leftNodeSize + 1 == rank) return x.getKey();
        else return select(x.left, rank);

    }

    public Key select(int k) {
        // Key of rank k
        if (size(root) < k) throw new IllegalArgumentException(
                String.format("rank %d is out of max size %d", k, size(root)));
        return select(root, k);
    }


    private int rank(Node x, Key key) {
        if (x == null) return 0;
        int keyCompare = x.getKey().compareTo(key);
        if (keyCompare < 0) {
            return size(x.left) + 1 + rank(x.right, key);
        } else if (keyCompare > 0) {
            return rank(x.left, key);
        } else {
            return size(x.left);
        }
    }

    public int rank(Key key) {
        return rank(root, key);
    }

    private Node ceiling(Node x, Key key) {
        if (x == null) return null;
        int keyCompare = x.getKey().compareTo(key);
        if (keyCompare < 0) {
            return ceiling(x.right, key);
        } else if (keyCompare > 0) {
            Node tmp = ceiling(x.left, key);
            if (tmp == null) return x;
            else return tmp;
        } else {
            return x;
        }
    }

    public Key ceiling(Key key) {
        // Smallest key greater than or equal to key
        Node tmp = ceiling(root, key);
        if (tmp == null) return null;
        else return tmp.getKey();
    }

    private Node floor(Node x, Key key) {
        if (x == null) return null;
        int keyCompare = x.getKey().compareTo(key);
        if (keyCompare < 0) {
            Node tmp = floor(x.right, key);
            if (tmp == null) return x;
            else return tmp;
        } else if (keyCompare > 0) {
            return floor(x.left, key);
        } else return x;
    }

    public Key floor(Key key) {
        // Largest key less than or equal to key
        Node tmp = floor(root, key);
        if (tmp == null) return null;
        else return tmp.getKey();
    }

    public Key max() {
        if (root == null) return null;
        Node tmp = root;
        while (tmp.right != null) {
            tmp = tmp.right;
        }
        return tmp.getKey();
    }

    public Key min() {
        if (root == null) return null;
        Node tmp = root;
        while (tmp.left != null) {
            tmp = tmp.left;
        }
        return tmp.getKey();
    }

    public static void main(String[] args) {
        String[] list = {"P", "H", "T", "D", "O", "S", "U", "A", "E", "I", "Y", "C", "M", "L"};
        ST<String, Integer> st = new ST<String, Integer>();
        for (String s : list) {
            st.put(s, 0);
        }
        StdOut.println(st.size());
        StdOut.println(st.min());
        StdOut.println(st.max());
        StdOut.println(st.ceiling("J"));
        StdOut.println(st.floor("J"));
        StdOut.println(st.rank("I"));
        StdOut.println(st.select(7));
    }

}
