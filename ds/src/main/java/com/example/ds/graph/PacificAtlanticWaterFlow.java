package com.example.ds.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PacificAtlanticWaterFlow {

    public List<List<Integer>> pacificAtlantic(int[][] heights) {
        if(heights.length == 0 || heights[0].length == 0) return Collections.emptyList();

        int row = heights.length;
        int col = heights[0].length;

        boolean[][] pacific = new boolean[row][col];
        boolean[][] atlantic = new boolean[row][col];

        for(int i=0; i<row; i++) {
            dfs(i, 0, heights[i][0], heights, pacific); // Left
            dfs(i, col-1, heights[i][col-1], heights, atlantic); // Right
        }

        for(int i=0; i<col; i++) {
            dfs(0, i, heights[0][i], heights, pacific); // Top
            dfs(row-1, i, heights[row-1][i], heights, atlantic); // Bottom
        }

        List<List<Integer>> res = new ArrayList<>();

        for(int i=0; i<row; i++) {
            for(int j=0; j<col; j++) {
                if(pacific[i][j] && atlantic[i][j]) {
                    res.add(Arrays.asList(i,j));
                }
            }
        }

        return res;
    }

    void dfs(int i, int j, int prevHeight, int[][] heights, boolean[][] vis) {
        int row = heights.length;
        int col = heights[0].length;

        if(i<0 || i>=row || j<0 || j>=col || vis[i][j] || prevHeight>heights[i][j])
            return;

        vis[i][j] = true;

        dfs(i+1, j, heights[i][j], heights, vis);
        dfs(i-1, j, heights[i][j], heights, vis);
        dfs(i, j+1, heights[i][j], heights, vis);
        dfs(i+1, j-1, heights[i][j], heights, vis);
    }

    public static void main(String[] args) {

        int[][] heights = {
                {1,2,2,3,5},
                {3,2,3,4,4},
                {2,4,5,3,1},
                {6,7,1,4,5},
                {5,1,1,2,4}
        };

        PacificAtlanticWaterFlow sol = new PacificAtlanticWaterFlow();

        List<List<Integer>>  list = sol.pacificAtlantic(heights);

        for (List<Integer> cor: list) {
            System.out.println(cor);
        }
    }
}
