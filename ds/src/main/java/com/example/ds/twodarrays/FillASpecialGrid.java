package com.example.ds.twodarrays;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FillASpecialGrid {
    public static void main(String[] args) {

        int[] coins = {1,2,3,4,5};

        List<Integer> list = new ArrayList<>();
        for(int k: coins){
            list.add(k);
        }
        list.sort(Collections.reverseOrder()); //5 4 3 2 1

        int amt = 16;
      //-------------------------------------

        int[][] grid = specialGrid(2);

        for (int[] row : grid) {
            for (int val : row) {
                System.out.print(val + "\t");
            }
            System.out.println();
        }

    }

    static int[][] specialGrid(int n) {
        int size = 1<<n;
        int[][] grid = new int[size][size];
        fill(grid, 0,0,size,0);

        return grid;
    }

    static void fill(int[][] grid, int row, int col, int size, int start) {
        if(size==1) {
            grid[row][col] = start;
            return;
        }

        int quad_size = size/2;
        int quad_start = quad_size * quad_size;

        fill(grid, row, col+quad_size, quad_size, start); // Top Right Quad
        fill(grid, row+quad_size, col+quad_size, quad_size, start+quad_start); // Bottom Right Quad
        fill(grid, row+quad_size, col, quad_size, start+quad_start*2); // Bottom Left Quad
        fill(grid, row, col, quad_size, start+quad_start*3); // Top Left Quad
    }
}
