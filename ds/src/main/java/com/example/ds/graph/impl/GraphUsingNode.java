package com.example.ds.graph.impl;

import java.util.ArrayList;
import java.util.List;

public class GraphUsingNode {

    static class Edge {
        int destination;
        int weight;

        Edge(int destination, int weight) {
            this.destination = destination;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "(" + destination + ", weight=" + weight + ")";
        }
    }

    static void printGraph(List<List<Edge>> graph) {
        for(int i=0; i<graph.size(); i++) {
            System.out.print("Vertex " + i + ": ");
            for(Edge edge: graph.get(i)) {
                System.out.print(edge + " ");
            }
            System.out.println();
        }

    }

    public static void main(String[] args) {
        int vertices = 5;
        List<List<Edge>> graph = new ArrayList<>();

        for(int i=0; i<vertices; i++) {
            graph.add(new ArrayList<>());
        }

        graph.get(0).add(new Edge(1, 9));
        graph.get(0).add(new Edge(2, 6));
        graph.get(0).add(new Edge(3, 5));
        graph.get(0).add(new Edge(4, 3));
        graph.get(2).add(new Edge(1, 2));
        graph.get(2).add(new Edge(3, 4));

        printGraph(graph);
    }

}
