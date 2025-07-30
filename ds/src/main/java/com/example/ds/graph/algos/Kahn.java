package com.example.ds.graph.algos;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Kahn {
    static void kahnSort(List<List<Integer>> adj, int V) {
        List<Integer> res = new ArrayList<>();
        Queue<Integer> queue = new LinkedList<>();
        int[] indegree = new int[V];

        // Step 1: Find in-degree
        for (int i=0; i<V; i++) {
            for(int element: adj.get(i)) {
                ++ indegree[element];
            }
        }

        // Step 2: Add those elements in the queue which have 0 in-degree
        for (int i=0; i<V; i++) {
            if(indegree[i] == 0)
                queue.add(i);
        }

        bfs(adj, res, queue, indegree);

        for(Integer element: res) {
            System.out.print(element + " ");
        }
    }

    /**
     * Step 3:
     * - Take out the element from the queue (having in-degree 0)
     * - Add in the res Array
     * - Explore its neighbour, reduce their in-degree by 1
     * - Add those neighbour whose in-degree is reduced to 0
     */
    static void bfs(List<List<Integer>> adj, List<Integer> res, Queue<Integer> queue, int[] indegree) {
        while(!queue.isEmpty()) {
            int cur = queue.poll();
            res.add(cur);

            for(int element: adj.get(cur)) {
                if(-- indegree[element] == 0) {
                    queue.add(element);
                }
            }
        }
    }


    public static void main(String[] args) {
        int V = 6;
        List<List<Integer>> adj = new ArrayList<>();

        for(int i=0; i<V; i++) {
            adj.add(new ArrayList());
        }

        adj.get(5).add(2);
        adj.get(5).add(0);
        adj.get(4).add(0);
        adj.get(4).add(1);
        adj.get(2).add(3);
        adj.get(3).add(1);

        kahnSort(adj, V);
    }
}
