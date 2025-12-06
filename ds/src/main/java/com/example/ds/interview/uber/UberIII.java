package com.example.ds.interview.uber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class UberIII {

    static public int[] findRobot(char[][] grid, int[] query) {
        int rows = grid.length, cols = grid[0].length;

        int[][] left = new int[rows][cols];
        int[][] right = new int[rows][cols];
        int[][] top = new int[rows][cols];
        int[][] bottom = new int[rows][cols];

        // Precompute left distances
        for (int i = 0; i < rows; i++) {
            int dist = 1;
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == 'X') dist = 1;
                else left[i][j] = dist++;
            }
        }

        // Precompute right distances
        for (int i = 0; i < rows; i++) {
            int dist = 1;
            for (int j = cols - 1; j >= 0; j--) {
                if (grid[i][j] == 'X') dist = 1;
                else right[i][j] = dist++;
            }
        }

        // Precompute top distances
        for (int j = 0; j < cols; j++) {
            int dist = 1;
            for (int i = 0; i < rows; i++) {
                if (grid[i][j] == 'X') dist = 1;
                else top[i][j] = dist++;
            }
        }

        // Precompute bottom distances
        for (int j = 0; j < cols; j++) {
            int dist = 1;
            for (int i = rows - 1; i >= 0; i--) {
                if (grid[i][j] == 'X') dist = 1;
                else bottom[i][j] = dist++;
            }
        }

        // Find robot matching the query
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == 'O') {
                    int[] dist = new int[]{
                            left[i][j],
                            top[i][j],
                            bottom[i][j],
                            right[i][j]
                    };
                    if (matches(dist, query))
                        return new int[]{i, j};
                }
            }
        }
        return new int[]{};
    }

    static private boolean matches(int[] a, int[] b) {
        for (int i = 0; i < 4; i++)
            if (a[i] != b[i]) return false;
        return true;
    }

 /*   public static void main(String[] args) {
        char[][] grid = {{'O','E','E','E','X'},{'E','O','X','X','X'},{'E','E','E','E','E'},{'X','E','O','E','E'},{'X','E','X','E','X'}};
        int[] query = {2, 2, 4, 1};

        int[] ans = findRobot(grid, query);
        System.out.println("{" + ans[0] + "," + ans[1] + "}");
    }*/

    public static void main(String[] args) {
        char[][] grid = {{'O','E','E','E','X'},{'E','O','X','X','X'},{'E','E','E','E','E'},{'X','E','O','E','E'},{'X','E','X','E','X'}};
        int[] query = {2, 2, 4, 1};

        System.out.println(Arrays.toString(findRobot(grid, query)));

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


class RobotLocator {
    // Returns {r, c} of a robot matching [L, U, D, R]; otherwise {-1, -1}
    public static int[] findRobotIndex(char[][] grid, int[] query) {
        int m = grid.length;
        if (m == 0) return new int[]{-1, -1};
        int n = grid[0].length;

        int[][] leftDist = new int[m][n];
        int[][] rightDist = new int[m][n];
        int[][] upDist = new int[m][n];
        int[][] downDist = new int[m][n];

        // Helper: is blocker (X or boundary implied via sweep initialization)
        // During sweeps, boundaries are represented by initializing last = -1 or = n/m.

        // Left distances: nearest blocker to the left
        for (int r = 0; r < m; r++) {
            int lastBlocker = -1; // boundary as blocker at c = -1
            for (int c = 0; c < n; c++) {
                if (grid[r][c] == 'X') {
                    lastBlocker = c;
                    leftDist[r][c] = 0; // distance for the blocker cell itself is 0, though we won't match on X
                } else {
                    leftDist[r][c] = c - lastBlocker - 1;
                }
            }
        }

        // Right distances: nearest blocker to the right
        for (int r = 0; r < m; r++) {
            int lastBlocker = n; // boundary as blocker at c = n
            for (int c = n - 1; c >= 0; c--) {
                if (grid[r][c] == 'X') {
                    lastBlocker = c;
                    rightDist[r][c] = 0;
                } else {
                    rightDist[r][c] = lastBlocker - c - 1;
                }
            }
        }

        // Up distances: nearest blocker above
        for (int c = 0; c < n; c++) {
            int lastBlocker = -1; // boundary as blocker at r = -1
            for (int r = 0; r < m; r++) {
                if (grid[r][c] == 'X') {
                    lastBlocker = r;
                    upDist[r][c] = 0;
                } else {
                    upDist[r][c] = r - lastBlocker - 1;
                }
            }
        }

        // Down distances: nearest blocker below
        for (int c = 0; c < n; c++) {
            int lastBlocker = m; // boundary as blocker at r = m
            for (int r = m - 1; r >= 0; r--) {
                if (grid[r][c] == 'X') {
                    lastBlocker = r;
                    downDist[r][c] = 0;
                } else {
                    downDist[r][c] = lastBlocker - r - 1;
                }
            }
        }

        // Scan robot cells for a match in order Left, Up, Down, Right
        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                if (grid[r][c] == '0') { // robot cells marked as '0'
                    if (leftDist[r][c] == query[0]
                            && upDist[r][c] == query[1]
                            && downDist[r][c] == query[2]
                            && rightDist[r][c] == query[3]) {
                        return new int[]{r, c};
                    }
                }
            }
        }
        return new int[]{-1, -1};
    }

    // Example usage
    public static void main(String[] args) {
        char[][] grid = {
                {'E','X','E','E','E'},
                {'E','0','E','X','E'},
                {'E','E','E','E','E'},
                {'X','E','0','E','E'}
        };
        int[] query = {2, 2, 4, 1}; // [Left, Up, Down, Right]

        int[] ans = findRobotIndex(grid, query);
        System.out.println("{" + ans[0] + "," + ans[1] + "}");
    }
}

class Discuss {
    public static List<List<Integer>> solution(char[][] grid, int[] target) {
        int m = grid.length;
        int n = grid[0].length;
        // left, up, right, down
        int[] top = new int[n];
        Arrays.fill(top, -1);
        int[][][] dp = new int[4][m][n];
        for (int i = 0; i<m; i++) {
            int left = -1;
            for (int j = 0; j<n; j++) {
                if (grid[i][j] == 'O') {
                    dp[0][i][j] = Math.abs(j - left);
                    dp[1][i][j] = Math.abs(i - top[j]);
                } else if (grid[i][j] == 'X') {
                    left = i;
                    top[j] = i;
                }
            }
        }
        int[] bot = new int[n];
        Arrays.fill(bot, n);
        for (int i = m-1; i>=0; i--) {
            int right = -1;
            for (int j = n-1; j>=0; j--) {
                if (grid[i][j] == 'O') {
                    dp[3][i][j] = Math.abs(j - right);
                    dp[2][i][j] = Math.abs(i - bot[j]);
                } else if (grid[i][j] == 'X') {
                    right = j;
                    bot[j] = i;
                }
            }
        }
        List<List<Integer>> ans = new ArrayList<>();
        for (int i = 0; i<m; i++) {
            for (int j = 0; j<n ;j++) {
                boolean isValid = true;
                for (int k = 0; k<4; k++) {
                    if (dp[k][i][j] != target[k]) {
                        isValid = false;
                    }
                }

                if (isValid) {
                    List<Integer> tempList = new ArrayList<>();
                    tempList.add(i);
                    tempList.add(j);
                    ans.add(new ArrayList<>(tempList));
                }
            }
        }
        return ans;
    }
}
