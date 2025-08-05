package com.example.ds.graph;

import java.util.ArrayList;
import java.util.List;

public class GraphValidTree {

    boolean hasCycle(int cur, int parent, List<List<Integer>> graph, boolean[] vis) {
        vis[cur] = true;

        for(int neighbour: graph.get(cur)) {
            if(!vis[neighbour]) {
                hasCycle(neighbour, cur, graph, vis);
            } else if(neighbour != parent) {
                return true;
            }
        }
        return false;
    }

    /**
     * 1. Detect cycle
     * 2. Check connected component
     */
    public boolean validTree(int n, int[][] edges) {
        if(edges.length != n-1) return false;

        List<List<Integer>> graph = new ArrayList<>();
        for(int i=0; i<n; i++) graph.add(new ArrayList<>());

        for(int[] edge: edges) {
            graph.get(edge[0]).add(edge[1]);
            graph.get(edge[1]).add(edge[0]);
        }

        boolean[] vis = new boolean[n];

        if(hasCycle(0, -1, graph, vis)) return false;

        for(boolean v: vis) {
            if(!v) return false;
        }

        return true;
    }

    public static void main(String[] args) {
        int[][] edge = {
                {0,1}, {1,2}, {2,3}, {1,3}, {1,4}
        };
        int n=5;

        GraphValidTree vt = new GraphValidTree();
        System.out.println("is Valid tree = " + vt.validTree(n, edge));
    }
}
