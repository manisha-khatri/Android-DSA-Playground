package com.example.ds.graph.algos;

/**
 * Topological Sort is a linear ordering of vertices in a Directed Acyclic Graph (DAG) such that for
 * every directed edge u → v, vertex u comes before v in the ordering.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class TopologicalSort {

    static void topologicalSort(int V, List<List<Integer>> adj) {
        boolean[] vis = new boolean[V];
        Stack<Integer> stack = new Stack<>();

        for(int i=0; i<V; i++) {
            if(!vis[i]) {
                dfs(i, adj, stack, vis);
            }
        }

        if(!stack.isEmpty()) {
            while (!stack.isEmpty()) {
                System.out.print(stack.pop() + " ");
            }
        }
    }

    static void dfs(int node, List<List<Integer>> adj, Stack<Integer> stack, boolean[] vis) {
        vis[node] = true;

        for(Integer neighbour: adj.get(node)) {
            if(!vis[neighbour]) {
                dfs(neighbour, adj, stack, vis);
            }
        }
        stack.add(node);
    }

    public static void main(String[] args) {
        int V = 6; // number of vertices
        List<List<Integer>> adj = new ArrayList<>();

        // Initialize adjacency list
        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }

        // Add edges (example DAG)
        adj.get(5).add(2);
        adj.get(5).add(0);
        adj.get(4).add(0);
        adj.get(4).add(1);
        adj.get(2).add(3);
        adj.get(3).add(1);

        topologicalSort(V, adj);
    }
}
