package com.example.ds.interview.uber;

import java.util.Arrays;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

class RobotFinder4 {

    /**
     * Finds the coordinates [row, col] of the robot(s) that match the given
     * closest-blocker distance query.
     * The order of query distances is: Left, Top, Bottom, Right.
     */
    static public List<List<Integer>> findRobot(char[][] grid, int[] query) {
        List<List<Integer>> matchingRobots = new ArrayList<>();

        int rows = grid.length, cols = grid[0].length;

        // Following the problem text: Robot is '0' (zero), Blocker is 'X'
        final char ROBOT_CHAR = '0';
        final char BLOCKER_CHAR = 'X';

        int[][] left = new int[rows][cols];
        int[][] right = new int[rows][cols];
        int[][] top = new int[rows][cols];
        int[][] bottom = new int[rows][cols];

        // --- Precompute Left and Top distances ---
        for (int i = 0; i < rows; i++) {
            int leftDist = 1;
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == BLOCKER_CHAR) {
                    leftDist = 1;
                } else {
                    left[i][j] = leftDist++;
                }
            }
        }

        for (int j = 0; j < cols; j++) {
            int topDist = 1;
            for (int i = 0; i < rows; i++) {
                if (grid[i][j] == BLOCKER_CHAR) {
                    topDist = 1;
                } else {
                    top[i][j] = topDist++;
                }
            }
        }

        // --- Precompute Right and Bottom distances ---
        for (int i = 0; i < rows; i++) {
            int rightDist = 1;
            for (int j = cols - 1; j >= 0; j--) {
                if (grid[i][j] == BLOCKER_CHAR) {
                    rightDist = 1;
                } else {
                    right[i][j] = rightDist++;
                }
            }
        }

        for (int j = 0; j < cols; j++) {
            int bottomDist = 1;
            for (int i = rows - 1; i >= 0; i--) {
                if (grid[i][j] == BLOCKER_CHAR) {
                    bottomDist = 1;
                } else {
                    bottom[i][j] = bottomDist++;
                }
            }
        }


        // --- Find robot matching the query ---
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == ROBOT_CHAR) {
                    // Distances in order: Left, Top, Bottom, Right
                    int[] dist = new int[]{
                            left[i][j],
                            top[i][j],
                            bottom[i][j],
                            right[i][j]
                    };

                    if (Arrays.equals(dist, query)) {
                        matchingRobots.add(Arrays.asList(i, j));
                    }
                }
            }
        }

        return matchingRobots;
    }

    // --- Main method for testing ---
    public static void main(String[] args) {
        System.out.println("--- Robot Finder Test Case ---");

        // Example Map (3x5 grid with '0', 'X', 'E')
        char[][] grid = {
                {'E', 'E', '0', 'X', 'E'},
                {'E', 'E', 'E', 'E', 'E'},
                {'E', 'E', 'E', 'E', '0'}
        };

        // Target Query: Left=3, Top=1, Bottom=3, Right=2. Matches robot at (0, 2).
        int[] query1 = {3, 1, 3, 2};

        // Another Query: Left=5, Top=3, Bottom=1, Right=1. Matches robot at (2, 4).
        int[] query2 = {5, 3, 1, 1};

        // Test Case 1
        List<List<Integer>> result1 = findRobot(grid, query1);
        System.out.println("\nQuery: " + Arrays.toString(query1));
        System.out.println("Expected Match: (0, 2)");
        System.out.println("Result: " + result1);

        // Test Case 2
        List<List<Integer>> result2 = findRobot(grid, query2);
        System.out.println("\nQuery: " + Arrays.toString(query2));
        System.out.println("Expected Match: (2, 4)");
        System.out.println("Result: " + result2);

        // Test Case 3 (No Match)
        int[] query3 = {1, 1, 1, 1}; // Impossible distances for this map
        List<List<Integer>> result3 = findRobot(grid, query3);
        System.out.println("\nQuery: " + Arrays.toString(query3));
        System.out.println("Expected Match: []");
        System.out.println("Result: " + result3);
    }
}