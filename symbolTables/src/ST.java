import java.util.Stack;

public class ST<Key, Value> {
    private Stack<Elem<Key, Value>> table;


    private class Elem<Key, Value> {
        Key key;
        Value val;
        Elem<Key, Value> next;

        Elem(Key ky, Value v, Elem<Key, Value> nd) {
            key = ky;
            val = v;
            next = nd;
        }

        // does this board equal y?
        @Override
        public boolean equals(Object y) {
            if (y == this) return true;
            if (y == null) return false;
            if (y.getClass() != this.getClass()) return false;
            Elem<Key, Value> that = (Elem<Key, Value>) y;
            return that.key == this.key;
        }

        void setVal(Value val) {
            this.val = val;
        }

        Key getKey() {
            return key;
        }

        Value getVal() {
            return val;
        }
    }

    public ST() {
        table = new Stack<Elem<Key, Value>>();
        // Create a symbol table
    }

    public void put(Key key, Value val) {
        // put key-value pair into the table(remove key from table if value is null)
        Elem<Key, Value> add_dict = new Elem<Key, Value>(key, val);
        for (Elem<Key, Value> el : table) {
            if (el.equals(add_dict)) {
                el.setVal(val);
                return;
            }
        }
        table.push(add_dict);
    }

    public Value get(Key key) {
        // value paired with key(null if key is absent)
        Elem<Key, Value> check_node = new Elem<Key, Value>(key, null);
        for (Elem<Key, Value> el : table) {
            if (el.equals(check_node)) {
                return el.getVal();
            }
        }
        return null;
    }

    public void delete(Key key) {
        // Delete key and its value from table
        // put(key, null);// Lazy version
        Elem<Key, Value> del_node = new Elem<Key, Value>(key, null);
        while (table.)

            for (Elem<Key, Value> el : table) {
                if (el.equals(del_node)) {
                    return el.getVal();
                }
            }
        return null;
    }

}


    public boolean contains(Key key) {
        // Is there a values paired with key?
        return get(key) != null;
    }

    public boolean isEmpty() {
        // Is the table empty?
    }

    public int size() {
        // Return the number of key-value pairs in the table
    }

    Iterable<Key> keys() {
        // All the keys in the table. Return an iterable object
    }

    public static void main(String[] args) {

    }

}
