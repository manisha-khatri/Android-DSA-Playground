package com.example.ds.graph.algos;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BFSGraph {
    int V;
    List<List<Integer>> adjList;

    BFSGraph(int V) {
        this.V = V;
        adjList = new ArrayList<>();

        for(int i=0; i<V; i++) {
            adjList.add(new ArrayList<>());
        }
    }

    void addEdge(int u, int v) {
        adjList.get(u).add(v);
        adjList.get(v).add(u);
    }

    void bfs(int start) {
        boolean vis[] = new boolean[V];
        Queue<Integer> queue = new LinkedList<>();

        queue.add(start);
        vis[start] = true;

        System.out.print("BFS Traversal: ");
        while (!queue.isEmpty()) {
            int data = queue.poll();
            System.out.print(data + " ");

            for(int ver: adjList.get(data)) {
                if(!vis[ver]) {
                    queue.add(ver);
                    vis[ver] = true;
                }
            }
        }
    }

    public static void main(String[] args) {
        BFSGraph graph = new BFSGraph(5);

        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 4);

        graph.bfs(0);
    }
}
