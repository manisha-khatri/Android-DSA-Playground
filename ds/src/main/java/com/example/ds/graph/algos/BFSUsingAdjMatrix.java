package com.example.ds.graph.algos;

import java.util.LinkedList;
import java.util.Queue;

public class BFSUsingAdjMatrix {

    static void bfs(int[][] graph, int source) {
        int n = graph.length;
        boolean[] vis = new boolean[n];
        Queue<Integer> queue = new LinkedList<>();

        queue.offer(source);
        vis[source] = true;

        while (!queue.isEmpty()) {
            int node = queue.poll();
            System.out.print(node + " ");

            for(int i=0; i<n; i++) { // row wise traversal
                if(graph[node][i] == 1 && !vis[i]) {
                    vis[i] = true;
                    queue.offer(i);
                }
            }
        }
    }

    public static void main(String[] args) {
        int[][] adjMatrix = {
                {0,1,1,0},
                {0,0,0,1},
                {0,0,0,1},
                {0,0,0,0},
        };

        bfs(adjMatrix, 0);
    }
}














