package com.example.ds.deque;

import java.util.*;

public class DequeExample {
    public static void main(String[] args) {
        Deque<Integer> dq = new ArrayDeque<>();

        // Adding elements at both ends
        dq.addFirst(10);  // front <- 10
        dq.addLast(20);   // 10 <- rear
        dq.addLast(30);   // 10, 20, 30
        dq.addFirst(5);   // 5, 10, 20, 30

        System.out.println("After adding elements: " + dq);

        // Peek from both ends
        System.out.println("Front element (peekFirst): " + dq.peekFirst());
        System.out.println("Rear element (peekLast): " + dq.peekLast());

        // Removing elements from both ends
        dq.removeFirst(); // removes 5
        dq.removeLast();  // removes 30

        System.out.println("After removing first and last: " + dq);

        // Stack behavior (LIFO)
        dq.push(100); // same as addFirst()
        dq.push(200);
        System.out.println("After stack-like pushes: " + dq);

        dq.pop(); // removes 200
        System.out.println("After pop (stack behavior): " + dq);

        // Queue behavior (FIFO)
        dq.offer(40); // addLast()
        dq.offer(50);
        System.out.println("After queue-like offers: " + dq);

        // [100, 10, 20, 40, 50]
        dq.poll(); // removes from front (FIFO)
        //  [10, 20, 40, 50]
        dq.pollFirst();
        //  [20, 40, 50]
        dq.offer(90);
        //  [20, 40, 50, 90]
        System.out.println("After poll (queue behavior): " + dq);
    }
}
