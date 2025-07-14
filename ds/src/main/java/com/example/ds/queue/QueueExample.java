package com.example.ds.queue;

import java.util.LinkedList;
import java.util.Queue;

public class QueueExample {
    public static void main(String[] args) {
        Queue<String> queue = new LinkedList<>();

        // Adding elements
        queue.add("Apple");
        queue.add("Banana");
        queue.add("Cherry");

        System.out.println("Queue: " + queue); // [Apple, Banana, Cherry]

        // Removing elements
        System.out.println("Removed: " + queue.poll()); // Apple
        System.out.println("Queue after removal: " + queue); // [Banana, Cherry]

        // Peek (check first element)
        System.out.println("Front element: " + queue.peek()); // Banana
    }
}
