package com.example.ds.linkedlist;

import java.util.HashMap;
import java.util.Map;

public class LRUCache {

    class Node {
        int key, value;
        Node prev, next;

        Node(int key, int value ) {
            this.key = key;
            this.value = value;
        }
    }

    private final Map<Integer, Node> cache;
    private final Node head, tail;
    private final int capacity;

    LRUCache(int capacity) {
        this.capacity = capacity;
        cache = new HashMap<>();
        head = new Node(0,0);
        tail = new Node(0,0);

        head.next = tail;
        tail.prev = head;
    }

    int get(int key) {
        if(!cache.containsKey(key)) return -1;

        Node node = cache.get(key);
        remove(node);
        insertToFront(node);

        return node.value;
    }

    void put(int key, int value) {
        if(cache.containsKey(key)) {    // Already present, then remove old
            remove(cache.get(key));
        }

        if(cache.size() == capacity) { // Check capacity, Remove LRU
            remove(tail.prev);
        }

        Node node = new Node(key, value);
        insertToFront(node);
    }

    void remove(Node node) {
        cache.remove(node.key);

        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    void insertToFront(Node node) {
        cache.put(node.key, node);

        node.next = head.next;
        node.prev = head;

        head.next.prev = node;
        head.next = node;
    }

    public static void main(String[] args) {
        LRUCache cache = new LRUCache(2); // Capacity = 2

        cache.put(1, 1);
        cache.put(2, 2);
        System.out.println(cache.get(1)); // returns 1

        cache.put(3, 3); // evicts key 2
        System.out.println(cache.get(2)); // returns -1

        cache.put(4, 4); // evicts key 1
        System.out.println(cache.get(1)); // returns -1
        System.out.println(cache.get(3)); // returns 3
        System.out.println(cache.get(4)); // returns 4
    }

}
