public class ST<Key, Value> {
    public ST() {
        // Create a symbol table
    }

    public void put(Key key, Value val) {
        // put key-value pair into the table(remove key from table if value is null)
    }

    public Value get(Key key) {
        // value paired with key(null if key is absent)
    }

    public void delete(Key key) {
        // Delete key and its value from table
        put(key, null);// Lazy version
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
