package com.example.ds.priorityqueue;

import java.util.PriorityQueue;

public class MinPriorityQueueExample {

    public static void main(String[] args) {
        // Creating a priority queue (default: min heap)
        PriorityQueue<Integer> pq = new PriorityQueue<>();

        // Adding elements
        pq.add(30);
        pq.add(10);
        pq.add(20);
        pq.add(5);

        // Printing the queue
        System.out.println("Priority Queue: " + pq);  // [5, 10, 20, 30] (Heap order)

        // Removing elements (smallest first)
        System.out.println("Removed: " + pq.poll()); // 5
        System.out.println("Removed: " + pq.poll()); // 10

        // Peek at the next element
        System.out.println("Next Element: " + pq.peek()); // 20
    }
}
