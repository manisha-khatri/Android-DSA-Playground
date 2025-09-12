package com.example.ds.graph;

import java.util.LinkedList;
import java.util.Queue;

public class ShortestDistanceFromAllBuildings {
    static public int shortestDistance(int[][] grid) {
        int n = grid.length;
        int m = grid[0].length;
        int[][] reach = new int[n][m];
        int[][] dist = new int[n][m];
        int buildings = 0;

        for(int i=0; i<n; i++) {
            for(int j=0; j<m; j++) {
                if(grid[i][j] == 1) {
                    ++buildings;
                    bfs(i, j, reach, dist, grid);
                }
            }
        }

        int res = Integer.MAX_VALUE;
        for(int i=0; i<n; i++) {
            for(int j=0; j<m; j++) {
                if(reach[i][j]==buildings && grid[i][j] == 0) {
                    res = Math.min(dist[i][j], res);
                }
            }
        }
        return res == Integer.MAX_VALUE ? -1 : res;
    }

    static void bfs(int r, int c, int[][] reach, int[][] dist, int[][] grid) {
        int n = grid.length;
        int m = grid[0].length;
        boolean[][] vis = new boolean[n][m];
        Queue<int[]> queue = new LinkedList<>();

        queue.offer(new int[]{r,c,0});
        vis[r][c] = true;

        while(!queue.isEmpty()) {
            int[] res = queue.poll();
            int x = res[0];
            int y = res[1];
            int d = res[2];

            int[][] dirs = {{-1,0}, {0,-1}, {1,0}, {0,1}};
            for(int[] dir: dirs) {
                int nx = x + dir[0];
                int ny = y + dir[1];

                if(nx>=0 && nx<n && ny>=0 && ny<m && !vis[nx][ny] && grid[nx][ny] == 0) {
                    vis[nx][ny] = true;
                    dist[nx][ny] += 1 + d;
                    reach[nx][ny] += 1; // buildings
                    queue.offer(new int[] {nx, ny, d+1});
                }
            }
        }
    }

    public static void main(String[] args) {
        int[][] grid = {
                {1,0,2,0,1},
                {0,0,0,0,0},
                {0,0,1,0,0}
        };

        System.out.println("Result = " + shortestDistance(grid));
    }
}
