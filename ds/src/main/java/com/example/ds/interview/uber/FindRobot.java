package com.example.ds.interview.uber;

import java.util.*;

class FindRobot {

    public static int[] findRobot(char[][] grid, int[] query) {
        int rows = grid.length, cols = grid[0].length;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == '0') {
                    int left = distanceLeft(grid, i, j);
                    int top = distanceTop(grid, i, j);
                    int bottom = distanceBottom(grid, i, j);
                    int right = distanceRight(grid, i, j);

                    int[] dist = {left, top, bottom, right};
                    if (matches(dist, query)) {
                        return new int[]{i, j};
                    }
                }
            }
        }
        return new int[]{-1, -1};
    }

    private static int distanceLeft(char[][] grid, int i, int j) {
        int dist = 0;
        for (int c = j - 1; c >= 0; c--) {
            dist++;
            if (grid[i][c] == 'X') return dist;
        }
        return dist + 0; // boundary as blocker
    }

    private static int distanceRight(char[][] grid, int i, int j) {
        int dist = 0;
        for (int c = j + 1; c < grid[0].length; c++) {
            dist++;
            if (grid[i][c] == 'X') return dist;
        }
        return dist + 0;
    }

    private static int distanceTop(char[][] grid, int i, int j) {
        int dist = 0;
        for (int r = i - 1; r >= 0; r--) {
            dist++;
            if (grid[r][j] == 'X') return dist;
        }
        return dist + 0;
    }

    private static int distanceBottom(char[][] grid, int i, int j) {
        int dist = 0;
        for (int r = i + 1; r < grid.length; r++) {
            dist++;
            if (grid[r][j] == 'X') return dist;
        }
        return dist + 0;
    }

    private static boolean matches(int[] a, int[] b) {
        for (int k = 0; k < 4; k++) {
            if (a[k] != b[k]) return false;
        }
        return true;
    }

    // --- Test driver ---
    public static void main(String[] args) {
        char[][] grid1 = {
                {'X', 'E', 'E', 'X'},
                {'E', '0', 'E', 'E'},
                {'E', 'E', 'X', 'E'},
                {'X', 'E', 'E', 'E'}
        };
        int[] query1 = {1, 1, 2, 2};
        System.out.println(Arrays.toString(findRobot(grid1, query1))); // [1, 1]

        char[][] grid2 = {
                {'X', 'E', '0', 'E', 'X'},
                {'E', 'E', 'E', 'E', 'E'},
                {'E', 'X', 'E', 'X', 'E'}
        };
        int[] query2 = {2, 1, 2, 2};
        System.out.println(Arrays.toString(findRobot(grid2, query2))); // [0, 2]

        char[][] grid3 = {
                {'0', 'E', 'X'},
                {'E', 'E', 'E'},
                {'X', 'E', 'E'}
        };
        int[] query3 = {1, 0, 3, 2};
        System.out.println(Arrays.toString(findRobot(grid3, query3))); // [0, 0]

        char[][] grid4 = {
                {'E', 'E', 'E', 'E'},
                {'E', 'X', '0', 'E'},
                {'E', 'E', 'E', 'X'}
        };
        int[] query4 = {1, 1, 1, 1};
        System.out.println(Arrays.toString(findRobot(grid4, query4))); // [1, 2]
    }
}
