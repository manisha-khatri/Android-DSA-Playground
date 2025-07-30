package com.example.ds.graph.algos;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class Dijkstra {
    static class Edge {
        int vertex;
        int weight;

        Edge(int vertex, int weight) {
            this.vertex = vertex;
            this.weight = weight;
        }
    }

    static class Node implements Comparable<Node> {
        int destination;
        int distance;

        Node(int destination, int distance) {
            this.destination = destination;
            this.distance = distance;
        }

        @Override
        public int compareTo(@NotNull Node other) {
            return Integer.compare(this.distance, other.distance);
        }
    }

    static int[] dijkstra(List<List<Edge>> graph, int source) {
        int[] dist = new int[graph.size()];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[source] = 0;

        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.offer(new Node(source, 0));

        while(!pq.isEmpty()) {
            Node node = pq.poll();
            int u = node.destination;

            for(Edge edge: graph.get(u)) {
                int v = edge.vertex;
                int sum = dist[u] + edge.weight;

                if(dist[v] > sum) {
                    dist[v] = sum;
                    pq.offer(new Node(v, dist[v]));
                }
            }
        }
        return dist;
    }

    public static void main(String[] args) {
        int V = 5;
        List<List<Edge>> graph = new ArrayList<>();

        for(int i=0; i<V; i++) {
            graph.add(new ArrayList<>());
        }

        graph.get(0).add(new Edge(1, 9));
        graph.get(0).add(new Edge(2, 6));
        graph.get(0).add(new Edge(3, 5));
        graph.get(0).add(new Edge(4, 3));
        graph.get(2).add(new Edge(1, 2));
        graph.get(2).add(new Edge(3, 4));

        int source = 0;
        int dist[] = dijkstra(graph, source);

        for(int i=0; i< dist.length; i++) {
            System.out.println(i + " --> " + dist[i]);
        }
    }
}








