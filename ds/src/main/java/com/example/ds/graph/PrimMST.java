package com.example.ds.graph;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class PrimMST {
    static class Edge {
        int vertex;
        int weight;

        Edge(int vertex, int weight) {
            this.vertex = vertex;
            this.weight = weight;
        }
    }

    static int primMST(List<List<Edge>> graph, int source) {
        PriorityQueue<Edge> pq = new PriorityQueue<>((a, b) -> a.weight - b.weight);
        boolean[] vis = new boolean[graph.size()];
        int totalWt = 0;

        pq.offer(new Edge(source, 0));

        while(!pq.isEmpty()) {
            Edge u = pq.poll();
            if(vis[u.vertex]) continue;

            vis[u.vertex] = true;
            totalWt += u.weight;

            for(Edge v: graph.get(u.vertex)) {
                if(!vis[v.vertex]) {
                    pq.offer(new Edge(v.vertex, v.weight));
                }
            }
        }
        return totalWt;
    }

    public static void main(String[] args) {
        int V=5;
        List<List<Edge>> graph = new ArrayList<>();

        for(int i=0; i<V; i++) {
            graph.add(new ArrayList<>());
        }

        addEdge(graph, 0, 1, 2);
        addEdge(graph, 0, 3, 6);
        addEdge(graph, 1, 2, 3);
        addEdge(graph, 1, 3, 8);
        addEdge(graph, 1, 4, 5);
        addEdge(graph, 2, 4, 7);
        addEdge(graph, 3, 4, 9);

        System.out.println("Connects all the vertex with minimum edge weight = " + primMST(graph, 0));

    }

    static void addEdge(List<List<Edge>> graph, int u, int v, int w) {
        graph.get(u).add(new Edge(v, w));
        graph.get(v).add(new Edge(u, w));
    }
}
