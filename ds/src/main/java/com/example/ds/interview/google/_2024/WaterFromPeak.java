package com.example.ds.interview.google._2024;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class WaterFromPeak {

    static int getReachableElevations(int[][] matrix, int town1, int town2) {
        Set<Integer> reachableFromTown1 = findReachable(matrix, town1);
        Set<Integer> reachableFromTown2 = findReachable(matrix, town2);
        Set<Integer> intersection = new HashSet<>();

        for(int town: reachableFromTown1) {
            if(reachableFromTown2.contains(town)) {
                intersection.add(town);
            }
        }

        if(intersection.isEmpty()) {
            return -1;
        } else {
            return Collections.max(intersection);
        }
    }

    static Set<Integer> findReachable(int[][] matrix, int town) {
        Set<Integer> reachable = new HashSet<>();
        boolean[][] vis = new boolean[matrix.length][matrix[0].length];

        for(int i=0; i<matrix.length; i++) {
            for(int j=0; j<matrix[0].length; j++) {
                if(matrix[i][j] == town) {
                    dfs(matrix, reachable, i, j, vis);
                }
            }
        }
        return reachable;
    }

    static int[][] dirs = {{1,0}, {-1,0}, {0,-1}, {0,1}};

    static void dfs(int[][] matrix, Set<Integer> reachable, int i, int j, boolean[][] vis) {
        vis[i][j] = true;
        reachable.add(matrix[i][j]);

        int n = matrix.length;
        int m = matrix[0].length;

        for(int[] d: dirs) {
            int x = i + d[0];
            int y = j + d[1];

            if(x<n && x>=0 && y<m && y>=0 && matrix[x][y]>matrix[i][j] && !vis[x][y]) {
                dfs(matrix, reachable, x, y, vis);
            }
        }
    }



    public static void main(String[] args) {
        int[][] matrix = {
                {1, 9, 7, 6, 5},
                {1, 6, 5, 4, 3},
                {1, 5, 10, 11, 18},
                {1, 4, 1, 1, 2}
        };

        System.out.println(getReachableElevations(matrix, 3, 4));
    }

}
