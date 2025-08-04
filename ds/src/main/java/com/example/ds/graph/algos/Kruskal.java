package com.example.ds.graph.algos;

import java.util.ArrayList;
import java.util.Collections;

public class Kruskal {
    static int[] parent;
    static int[] rank;


    static class Edge implements Comparable<Edge> {
        int src, dest, wt;

        Edge(int src, int dest, int wt) {
            this.src = src;
            this.dest = dest;
            this.wt = wt;
        }

        @Override
        public int compareTo( Edge that) {
            return this.wt - that.wt;
        }
    }

    static int find(int i) {
        if(parent[i]==i) return i;
        return find(parent[i]);
    }

    static void union(int a, int b) {
        int x = find(a);
        int y = find(b);

        if(x == y) return;

        if(rank[x] > rank[y]) {
            parent[y] = x;
        } else if(rank[x] < rank[y]) {
            parent[x] = y;
        } else {
            parent[y] = x;
            rank[x]++;
        }
    }

    public static void main(String[] args) {
        int V=5;
        ArrayList<Edge> edges = new ArrayList<Edge>();

        edges.add(new Edge(0, 1, 10));
        edges.add(new Edge(0, 2, 6));
        edges.add(new Edge(0, 3, 5));
        edges.add(new Edge(1, 3, 15));
        edges.add(new Edge(2, 3, 4));

        Collections.sort(edges);

        parent = new int[V];
        rank = new int[V];

        for(int i=0; i<V; i++) parent[i] = i;

        int resWt = 0;
        int count = 1; // cover v-1 edges

        for(int i=0; count<V && i<edges.size(); i++) {
            Edge edge = edges.get(i);
            int x = find(edge.src);
            int y = find(edge.dest);

            if(x!=y) {
                union(x, y);
                count++;
                resWt += edge.wt;
            }
        }

        System.out.println("Final Res = " + resWt);
    }
}
