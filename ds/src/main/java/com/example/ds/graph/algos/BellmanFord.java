package com.example.ds.graph.algos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Edge {
    int source;
    int weight;
    int dest;

    Edge(int s, int d, int w) {
        this.source = s;
        this.weight = w;
        this.dest = d;
    }
}

public class BellmanFord {
    int V;
    List<Edge> edges;

    BellmanFord(int v) {
        this.V = v;
        edges = new ArrayList<>();
    }

    void addEdge(int s, int d, int w) {
        edges.add(new Edge(s,d,w));
    }

    void shortestPath(int src) {
        int[] dist = new int[V];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[src] = 0;

        for(int i=1; i<=V-1; i++) {
            for(Edge edge: edges) {
                if(dist[edge.source] !=  Integer.MAX_VALUE && dist[edge.source] + edge.weight < dist[edge.dest]) {
                    dist[edge.dest] = dist[edge.source] + edge.weight;
                }
            }
        }
        for(Edge edge: edges) {
            if(dist[edge.source] !=  Integer.MAX_VALUE && dist[edge.source] + edge.weight < dist[edge.dest]) {
                System.out.println("Graph contains negative weight cycle");
                return;
            }
        }

        System.out.println("Vertex\tDistance from Source");
        for(int i=0; i<V; i++) {
            System.out.println(i + "\t" + (dist[i] == Integer.MAX_VALUE ? "∞" : dist[i]));
        }
    }

    public static void main(String[] args) {
        BellmanFord graph = new BellmanFord(5);

        graph.addEdge(0, 1, -1);
        graph.addEdge(0, 2, 4);
        graph.addEdge(1, 2, 3);
        graph.addEdge(1, 3, 2);
        graph.addEdge(1, 4, 2);
        graph.addEdge(3, 2, 5);
        graph.addEdge(3, 1, 1);
        graph.addEdge(4, 3, -3);

        graph.shortestPath(0);
    }
}





















