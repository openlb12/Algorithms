import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class ST<Key extends Comparable<Key>, Value> {
    private Node root;

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
            if (y == null) return false;
            if (y == this) return true;
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
        if (x == null) return null;
        int compareKey = x.getKey().compareTo(key);
        if (compareKey < 0) x.right = delete(x.right, key);
        else if (compareKey > 0) x.left = delete(x.left, key);
        else {
            if (size(x) <= 1) return null;
            else {
                if (size(x.left) > size(x.right)) {
                    Node tmp = max(x.left);
                    tmp = new Node(tmp.getKey(), tmp.getVal());
                    tmp.right = x.right;
                    tmp.left = deleteMax(x.left);
                    x = tmp;
                } else {
                    Node tmp = min(x.right);
                    tmp = new Node(tmp.getKey(), tmp.getVal());
                    tmp.left = x.left;
                    tmp.right = deleteMin(x.right);
                    x = tmp;
                }
            }
        }
        x.setCount(1 + size(x.left) + size(x.right));
        return x;
    }

    public void delete(Key key) {
        // Delete key and its value from table
        // put(key, null);// Lazy version
        root = delete(root, key);
    }


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

    public Iterable<Key> reverseSortedKeys() {
        // All the keys in reverse sorted the table. Return an iterable object
        if (root == null) return null;
        Stack<Key> list = new Stack<Key>();
        Stack<Node> ndlist = new Stack<Node>();
        Node tmp = root;
        do {
            while (tmp != null) {
                ndlist.push(tmp);
                tmp = tmp.left;
            }
            tmp = ndlist.pop();
            list.push(tmp.getKey());
            tmp = tmp.right;
        } while (!ndlist.isEmpty() || tmp != null);

        return list;
    }

    public Iterable<Key> sortedKeys() {
        // All the keys in reverse sorted the table. Return an iterable object
        if (root == null) return null;
        Queue<Key> list = new Queue<Key>();
        Stack<Node> ndlist = new Stack<Node>();
        Node tmp = root;
        do {
            while (tmp != null) {
                ndlist.push(tmp);
                tmp = tmp.left;
            }
            tmp = ndlist.pop();
            list.enqueue(tmp.getKey());
            tmp = tmp.right;
        } while (!ndlist.isEmpty() || tmp != null);

        return list;
    }

    public Iterable<Key> keys() {
        if (root == null) return null;
        Queue<Key> keys = new Queue<Key>();
        Queue<Node> nds = new Queue<Node>();
        nds.enqueue(root);
        while (!nds.isEmpty()) {
            Node tmp = nds.dequeue();
            keys.enqueue(tmp.getKey());
            if (tmp.left != null) {
                nds.enqueue(tmp.left);
            }
            if (tmp.right != null) {
                nds.enqueue(tmp.right);
            }
        }
        return keys;
    }


    Iterable<Key> keys(Key lo, Key hi) {
        // Keys in [lo..hi], in sorted order
        Queue<Key> que = new Queue<Key>();
        keys(root, que, lo, hi);
        return que;
    }

    private void keys(Node x, Queue<Key> que, Key lo, Key hi) {
        if (x == null) return;
        int loCmp = lo.compareTo(x.getKey());
        int hiCmp = hi.compareTo(x.getKey());
        if (loCmp < 0) keys(x.left, que, lo, hi);
        if (hiCmp > 0) keys(x.right, que, lo, hi);
        if (loCmp <= 0 && hiCmp >= 0) {
            que.enqueue(x.getKey());
        }
    }

    public int size(Key lo, Key hi) {
        // Number of keys in [lo..hi]
        int rangeSize = 0;
        for (Key s : keys(lo, hi)) {
            rangeSize++;
        }
        return rangeSize;
    }

    private Node deleteMax(Node x) {
        if (x == null) return null;
        if (x.right == null) return x.left;
        else {
            x.right = deleteMax(x.right);
            x.setCount(size(x.left) + size(x.right) + 1);
            return x;
        }
    }

    public void deleteMax() {
        if (root == null) return;
        root = deleteMax(root);
    }

    private Node deleteMin(Node x) {
        if (x == null) return null;
        if (x.left == null) return x.right;
        else {
            x.left = deleteMin(x.left);
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
        if (leftNodeSize + 1 < rank)
            return select(x.right, rank - leftNodeSize - 1);
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

    private Node max(Node x) {
        if (x == null) return null;
        Node tmp = x;
        while (tmp.right != null) {
            tmp = tmp.right;
        }
        return tmp;
    }

    public Key max() {
        if (root == null) return null;
        return max(root).getKey();
    }

    private Node min(Node x) {
        if (x == null) return null;
        Node tmp = x;
        while (tmp.left != null) {
            tmp = tmp.left;
        }
        return tmp;
    }

    public Key min() {
        if (root == null) return null;
        return min(root).getKey();
    }

    public static void main(String[] args) {
        String[] list = {"P", "H", "T", "D", "O", "S", "U", "A", "E", "I", "Y", "C", "M", "L"};
        ST<String, Integer> st = new ST<String, Integer>();
        for (String s : list) {
            st.put(s, 0);
        }
        StdOut.print("Print Keys: ");
        for (String s : st.keys()) {
            StdOut.print(s + ", ");
        }
        StdOut.println();
        st.delete("H");
        StdOut.print("Print deleting H: ");
        for (String s : st.keys()) {
            StdOut.print(s + ", ");
        }
        StdOut.println();
        st.deleteMax();
        StdOut.print("Print keys after deleting Max: ");
        for (String s : st.keys()) {
            StdOut.print(s + ", ");
        }
        StdOut.println();
        StdOut.print("Print sorted keys: ");
        for (String s : st.sortedKeys()) {
            StdOut.print(s + ", ");
        }
        StdOut.println();
        StdOut.print("Print reversed keys: ");
        for (String s : st.reverseSortedKeys()) {
            StdOut.print(s + ", ");
        }
        StdOut.println();
        StdOut.print("Print keys ranging between 'E' and 'P': ");
        for (String s : st.keys("E", "P")) {
            StdOut.print(s + ", ");
        }
        StdOut.println();

        StdOut.println(st.size());
        StdOut.println(st.min());
        StdOut.println(st.max());
        StdOut.println(st.ceiling("J"));
        StdOut.println(st.floor("J"));
        StdOut.println(st.rank("I"));
        StdOut.println(st.select(7));
    }

}
