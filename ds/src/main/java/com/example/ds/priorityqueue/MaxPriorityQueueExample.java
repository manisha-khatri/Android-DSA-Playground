package com.example.ds.priorityqueue;

import java.util.Collections;
import java.util.PriorityQueue;

public class MaxPriorityQueueExample {
    public static void main(String[] args) {
        // Max Priority Queue (Reverse order comparator)
        PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());

        // Adding elements
        pq.add(30);
        pq.add(10);
        pq.add(20);
        pq.add(5);

        // Removing elements (largest first)
        System.out.println("Removed: " + pq.poll()); // 30
        System.out.println("Removed: " + pq.poll()); // 20
    }
}

