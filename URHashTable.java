/*
------------------------
Author: Matvii Repetskyi
------------------------
*/

//credits for nextPrime method to the top responder here: https://stackoverflow.com/questions/47407251/optimal-way-to-find-next-prime-number-java

import java.util.Iterator;
import java.util.NoSuchElementException;

import static java.lang.Math.sqrt;

public class URHashTable<Key,Value> extends UR_HashTable<Key,Value> implements Iterable<Key>{

    //Default constructor
    public URHashTable() {
        this(INIT_CAPACITY);
    }

    //Custom constructor
    public URHashTable(int capacity){
        m = capacity;
        n = 0;
        keys = (Key[]) new Object[m];
        vals = (Value[]) new Object[m];
        inserts = 0;
        collisions = 0;
    }

    private final Key EmptyAfterRemoval = (Key) new Object();

    //hash function to channel keys to buckets
    //the 0x7fffffff ensures only positive hashCodes are allowed
    public int hash(Key key) {
        return (key.hashCode() & 0x7fffffff) % m;
    }

    public void put(Key key, Value value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");

        }
        //if a key doesn't have value, delete the key
        if (value == null) {
            delete(key);
            throw new IllegalArgumentException("Value cannot be null");
        }

        //resize table if necessary
        if (n >= m / 2) {
            resize(m);
        }

        int bucket = hash(key);
        int buckets_probed = 0;
        int N = m;

        while (buckets_probed < N) {

            if (keys[bucket] == null || keys[bucket] == EmptyAfterRemoval) {
                keys[bucket] = key;
                vals[bucket] = value;
                n++;
                inserts++;
                return;
            }

            //case where key already exists in table (may have different value)
            if (keys[bucket].equals(key)) {
                vals[bucket] = value;
                return;
            }

            //increment bucket index as in linear probing
            bucket = (bucket + 1) % N;
            collisions++;
            buckets_probed++;
        }
    }

    public Value get(Key key) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");

        int bucket = hash(key);
        int bucketsProbed = 0;

        while (keys[bucket] != null && bucketsProbed < m) {
            // Skip tombstones and check if the key matches
            if (keys[bucket] != EmptyAfterRemoval && keys[bucket].equals(key)) {
                return vals[bucket];
            }

            // Move to the next bucket if no match is found
            bucket = (bucket + 1) % m;
            bucketsProbed++;
        }
        // Return null if key is not found
        return null;
    }


    public void delete(Key key) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");

        int bucket = hash(key);
        int buckets_probed = 0;
        int N = m;

        while ((keys[bucket] != null && buckets_probed < N)) {

            if (keys[bucket].equals(key)) {
                keys[bucket] = EmptyAfterRemoval;
                vals[bucket] = null;
                n--;
                return;
            }

            bucket = (bucket + 1) % N;
            buckets_probed++;
        }
    }

    public void resize(int capacity) {
        int newSize = nextPrime(capacity * 2);
        URHashTable<Key, Value> newTable = new URHashTable<>(newSize);

        int bucket = 0;
        for (int i = 0; i < m; i++) {
            if (keys[i] != null && keys[i] != EmptyAfterRemoval) {
                newTable.put(keys[i], vals[i]);
            }
        }

        keys = newTable.keys;
        vals = newTable.vals;
        m = newTable.m;
        n = newTable.n;
    }

    public int size() {
        return m;
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public boolean contains(Key key){
        return get(key) != null;
    }

    //credits for this method to the top responder here: https://stackoverflow.com/questions/47407251/optimal-way-to-find-next-prime-number-java
    public int nextPrime(int input){
        int counter;
        input++;
        while(true){
            int l = (int) sqrt(input);
            counter = 0;
            for(int i = 2; i <= l; i ++){
                if(input % i == 0)  counter++;
            }
            if(counter == 0)
                return input;
            else{
                input++;
                continue;
            }
        }
    }

    public Iterable<Key> keys() {
        return this;
    }

    @Override
    public Iterator<Key> iterator() {
        return new KeyIterator();
    }

    // Credits to CHATGPT for this iterator class
    private class KeyIterator implements Iterator<Key> {
        private int current = 0;
        private int keysRemaining = n;

        @Override
        public boolean hasNext() {
            return keysRemaining > 0;
        }

        @Override
        public Key next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            // Find the next non-null key
            while (keys[current] == null) {
                current++;
            }
            keysRemaining--;
            return keys[current++];
        }
    }
}
