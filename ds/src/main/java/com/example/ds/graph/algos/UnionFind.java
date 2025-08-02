package com.example.ds.graph.algos;

public class UnionFind {
    int[] parent;
    int[] rank;

    UnionFind(int n) {
        parent = new int[n];
        rank = new int[n];

        for(int i=0; i<n; i++) {
            parent[i] = i;
        }
    }

    int find(int i) {
        if(parent[i] != i) {
            // Path compression
            parent[i] = find(parent[i]);
        }
        return parent[i];
    }

    void union(int a, int b) {
        int x = find(a);
        int y = find(b);

        if(x==y) return;

        // Attach smaller rank tree under root of higher rank tree
        if(rank[x]>rank[y]) {
            parent[y] = x;
        } else if (rank[x]<rank[y]) {
            parent[x] = y;
        } else {
            parent[y] = parent[x];
            rank[x] = rank[x] + 1;
        }
    }

    boolean isConnected(int x, int y) {
        return parent[x] == parent[y];
    }



    public static void main(String[] args) {
        UnionFind uf = new UnionFind(5);

        uf.union(0,1);
        uf.union(1,2);

        System.out.println(uf.isConnected(0,2));
        System.out.println(uf.isConnected(0,3));

        uf.union(3,4);
        System.out.println(uf.isConnected(3,4));
    }
}
