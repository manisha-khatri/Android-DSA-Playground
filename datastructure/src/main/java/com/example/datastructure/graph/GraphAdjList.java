package com.example.datastructure.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphAdjList {

    Map<Integer, List<Integer>> graph = new HashMap<>();

    void addEdge(int u, int v) {
        if(!graph.containsKey(u)) {
            graph.put(u, new ArrayList<>());
        }
        graph.get(u).add(v);
    }

    public static void main(String[] args) {
        GraphAdjList g = new GraphAdjList();

        g.addEdge(0, 1);
        g.addEdge(0, 4);
        g.addEdge(1, 2);
        g.addEdge(1, 3);
    }





}
