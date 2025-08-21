package com.example.ds.graph;

import java.util.PriorityQueue;

class Tuple {
    int row;
    int col;
    int distance;

    Tuple(int distance, int row, int col) {
        this.distance = distance;
        this.row = row;
        this.col = col;
    }
}

public class PathWithMinimumEffort {

    int[] dirRow = {1,-1,0,0};
    int[] dirCol = {0,0,1,-1};

    public int minimumEffortPath(int[][] heights) {
        int rows = heights.length;
        int cols = heights[0].length;

        int[][] dist = new int[rows][cols];

        for(int i=0; i<rows; i++) {
            for(int j=0; j<cols; j++) {
                dist[i][j] = Integer.MAX_VALUE;
            }
        }
        dist[0][0] = 0;

        PriorityQueue<Tuple> pq = new PriorityQueue<>((a,b) -> a.distance-b.distance);
        pq.add(new Tuple(0,0,0));

        while(!pq.isEmpty()) {
            Tuple tuple = pq.poll();
            int oldRow = tuple.row;
            int oldCol = tuple.col;
            int oldDist = tuple.distance;

            if(oldRow == rows-1 && oldCol == cols-1) return oldDist;

            for(int i=0; i<4; i++) {
                int newRow = oldRow + dirRow[i];
                int newCol = oldCol + dirCol[i];

                if(newRow>=0 && newRow<rows && newCol>=0 && newCol<cols) {
                    int minEffort = Math.max(
                            Math.abs(heights[oldRow][oldCol] - heights[newRow][newCol]), oldDist
                    );

                    if (dist[newRow][newCol] > minEffort) {
                        dist[newRow][newCol] = minEffort;
                        pq.add(new Tuple(minEffort, newRow, newCol));
                    }
                }
            }
        }
        return 0;
    }

    public static void main(String[] args) {
        int[][] heights = {{1,2,2},{3,8,2},{5,3,5}};
        PathWithMinimumEffort solver = new PathWithMinimumEffort();

        System.out.println(solver.minimumEffortPath(heights)); // ✅ Output: 2
    }
}
