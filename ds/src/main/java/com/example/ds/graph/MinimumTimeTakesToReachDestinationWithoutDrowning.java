package com.example.ds.graph;

import java.util.*;

public class MinimumTimeTakesToReachDestinationWithoutDrowning {
    static public int minimumSeconds(List<List<String>> land) {
        String[][] grid = new String[land.size()][];
        for(int i=0; i<land.size(); i++) {
            List<String> row = land.get(i);
            grid[i] = row.toArray(new String[0]);
        }

        int n = grid.length;
        int m = grid[0].length;
        Queue<int[]> waterQ = new LinkedList<>();
        int[] src = null, dest = null;
        int[][] waterTime = new int[n][m];
        for(int[] row: waterTime) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }

        // Find the positions
        for(int i=0; i<n; i++) {
            for(int j=0; j<m; j++) {
                switch(grid[i][j]) {
                    case "S":
                        src = new int[]{i,j};
                        break;
                    case "D":
                        dest = new int[]{i,j};
                        break;
                    case "*":
                        waterQ.offer(new int[]{i,j,0});
                        waterTime[i][j] = 0;
                        break;
                }
            }
        }

        int[][] dirs = {{0,1}, {0,-1}, {1,0}, {-1,0}};

        // Calculate the flood by time
        while(!waterQ.isEmpty()) {
            int[] cur = waterQ.poll(); // 1,2,0
            int x = cur[0];
            int y = cur[1];
            int t = cur[2];

            for(int[] dir: dirs) {
                int nx = x + dir[0];
                int ny = y + dir[1];

                if(nx>=0 && nx<n && ny>=0 && ny<m && !grid[nx][ny].equals("X") && !grid[nx][ny].equals("D") && waterTime[nx][ny]>t+1) {
                    waterTime[nx][ny] = t+1;
                    waterQ.offer(new int[]{nx, ny, t+1});
                }
            }
        }

        // Moving from src to dest
        boolean[][] vis = new boolean[n][m];
        Queue<int[]> move = new LinkedList<>();
        move.offer(new int[]{src[0], src[1], 0});
        vis[src[0]][src[1]] = true;

        while(!move.isEmpty()) {
            int[] cur = move.poll();
            int x = cur[0];
            int y = cur[1];
            int t = cur[2];

            if(x == dest[0] && y == dest[1]) return t;

            for(int[] dir: dirs) {
                int nx = x + dir[0];
                int ny = y + dir[1];

                if(nx>=0 && nx<n && ny>=0 && ny<m && !vis[nx][ny] && t+1<waterTime[nx][ny] && !grid[nx][ny].equals("X")) {
                    vis[nx][ny] = true;
                    move.offer(new int[]{nx, ny, t+1});
                }
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        List<List<String>> land = new ArrayList<>();
        land.add(Arrays.asList("D", ".", "*"));
        land.add(Arrays.asList(".", ".", "."));
        land.add(Arrays.asList(".", "S", "."));


        System.out.println(minimumSeconds(land));
    }
}
