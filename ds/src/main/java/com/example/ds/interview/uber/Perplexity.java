package com.example.ds.interview.uber;

import java.util.Arrays;

class Perplexity {

    static public int[] findRobot(char[][] grid, int[] query) {
        int rows = grid.length, cols = grid[0].length;
        final char ROBOT_CHAR = '0';
        final char BLOCKER_CHAR = 'X';

        int[][] left = new int[rows][cols];
        int[][] right = new int[rows][cols];
        int[][] top = new int[rows][cols];
        int[][] bottom = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            int dist = 1; // Distance from left boundary/blocker is 1
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == BLOCKER_CHAR) dist = 1;
                else left[i][j] = dist++;
            }
        }

        for (int i = 0; i < rows; i++) {
            int dist = 1;
            for (int j = cols - 1; j >= 0; j--) {
                if (grid[i][j] == BLOCKER_CHAR) dist = 1;
                else right[i][j] = dist++;
            }
        }

        for (int j = 0; j < cols; j++) {
            int dist = 1;
            for (int i = 0; i < rows; i++) {
                if (grid[i][j] == BLOCKER_CHAR) dist = 1;
                else top[i][j] = dist++;
            }
        }

        for (int j = 0; j < cols; j++) {
            int dist = 1;
            for (int i = rows - 1; i >= 0; i--) {
                if (grid[i][j] == BLOCKER_CHAR) dist = 1;
                else bottom[i][j] = dist++;
            }
        }

        // Find robot matching the query
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == ROBOT_CHAR) {
                    int[] dist = new int[]{
                            left[i][j],     // Index 0: Left
                            top[i][j],      // Index 1: Top
                            bottom[i][j],   // Index 2: Bottom
                            right[i][j]     // Index 3: Right
                    };
                    if (Arrays.equals(dist, query)) return new int[]{i, j};
                }
            }
        }
        // Per problem, return an empty array if no match is found.
        return new int[]{};
    }

    public static void main(String[] args) {
        char[][] grid1 = {
                {'X', 'E', 'E', 'X'},
                {'E', '0', 'E', 'E'},
                {'E', 'E', 'X', 'E'},
                {'X', 'E', 'E', 'E'}
        };
        int[] query1 = {1, 1, 2, 2};
        System.out.println(Arrays.toString(findRobot(grid1, query1))); // [1, 1]
    }
}
