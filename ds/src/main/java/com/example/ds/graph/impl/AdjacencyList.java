package com.example.ds.graph.impl;

import java.util.ArrayList;
import java.util.List;

public class AdjacencyList {

    public static void printGraph(List<List<Integer>> adjList) {
        System.out.println("Adjacency List:");

        for(int i=0; i<adjList.size(); i++) {
            System.out.print(i + " --> ");
            for(int node: adjList.get(i)) {
                System.out.print(node + " ");
            }
            System.out.println();
        }
    }

    public static void addEdge(List<List<Integer>> adjList, int u, int v) {
        adjList.get(u).add(v);
        adjList.get(v).add(u);
    }

    public static void main(String[] args) {
        int V = 4;
       List<List<Integer>> adjList = new ArrayList<>();

       for(int i=0; i<V; i++) {
           adjList.add(new ArrayList<>());
       }

        addEdge(adjList, 0, 1);
        addEdge(adjList, 0, 2);
        addEdge(adjList, 1, 2);
        addEdge(adjList, 2, 3);

        printGraph(adjList);
    }
}
