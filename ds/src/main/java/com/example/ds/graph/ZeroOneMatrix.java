package com.example.ds.graph;

import java.util.*;

public class ZeroOneMatrix {
    public int[][] updateMatrix(int[][] mat) {
        int m = mat.length, n = mat[0].length;
        int[][] dist = new int[m][n];
        Queue<int[]> queue = new LinkedList<>();

        // Step 1: Initialize
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (mat[i][j] == 0) {
                    dist[i][j] = 0;
                    queue.offer(new int[]{i, j}); // multi-source BFS starting points
                } else {
                    dist[i][j] = -1; // unvisited
                }
            }
        }

        // Directions (up, down, left, right)
        int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};

        // Step 2: BFS traversal
        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int r = cell[0], c = cell[1];

            for (int[] d : dirs) {
                int nr = r + d[0], nc = c + d[1];
                if (nr >= 0 && nr < m && nc >= 0 && nc < n && dist[nr][nc] == -1) {
                    dist[nr][nc] = dist[r][c] + 1;
                    queue.offer(new int[]{nr, nc});
                }
            }
        }

        return dist;
    }

    public static void main(String[] args) {
        int[][] mat = {
                {1, 0, 0},
                {1, 1, 0},
                {1, 1, 1}
        };

        ZeroOneMatrix sol = new ZeroOneMatrix();
        int[][] result = sol.updateMatrix(mat);

        // Print the output matrix
        for (int[] row : result) {
            System.out.println(Arrays.toString(row));
        }
    }
}
