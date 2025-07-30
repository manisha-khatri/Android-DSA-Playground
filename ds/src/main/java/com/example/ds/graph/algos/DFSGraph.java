package com.example.ds.graph.algos;

import java.util.ArrayList;
import java.util.List;

public class DFSGraph {
    private int V;
    private List<List<Integer>> adjList;

    public DFSGraph(int V) {
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

    void dfsUtil(int node, boolean[] vis) {
        vis[node] = true;
        System.out.print(node + " ");

        for(int neighbour: adjList.get(node)) {
            if(!vis[neighbour]) {
                dfsUtil(neighbour, vis);
            }
        }
    }

    void dfs(int start) {
        boolean vis[] = new boolean[V];
        System.out.println("DFS Traversal:");
        dfsUtil(start, vis);
    }

    public static void main(String[] args) {
        DFSGraph graph = new DFSGraph(5);

        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 4);

        graph.dfs(0);
    }
}
