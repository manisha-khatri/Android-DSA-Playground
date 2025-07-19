package com.example.ds.graph;

public class GraphAdjacencyMatrix {

    // Method to add an edge (for undirected graph)
    public static void addEdge(int[][] matrix, int i, int j) {
        matrix[i][j] = 1;
        matrix[j][i] = 1;
    }

    public static void printMatrix(int[][] matrix) {
        System.out.println("Adjacency Matrix:");

        for(int i=0; i<matrix.length; i++) {
            for(int j=0; j<matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        int v = 4;

        int[][] adjMatrix = new int[v][v];

        addEdge(adjMatrix, 0, 1);
        addEdge(adjMatrix, 0, 2);
        addEdge(adjMatrix, 1, 2);
        addEdge(adjMatrix, 2, 3);

        printMatrix(adjMatrix);
    }
}
