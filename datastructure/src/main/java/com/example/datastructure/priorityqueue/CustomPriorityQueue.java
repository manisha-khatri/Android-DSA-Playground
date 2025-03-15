package com.example.datastructure.priorityqueue;

import java.util.PriorityQueue;

class Task implements Comparable<Task> {
    String name;
    int priority;

    public Task(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }

    @Override
    public int compareTo(Task o) {
        return this.priority - o.priority;
    }

    @Override
    public String toString() {
        return name + " (Priority: " + priority + ")";
    }
}

public class CustomPriorityQueue {
    public static void main(String[] args) {
        PriorityQueue<Task> pq = new PriorityQueue<>();

        pq.add(new Task("Fix Bug", 2));
        pq.add(new Task("Code Review", 1));
        pq.add(new Task("Deploy", 3));

        while (!pq.isEmpty()) {
            System.out.println("Processing: " + pq.poll());
        }
    }
}
