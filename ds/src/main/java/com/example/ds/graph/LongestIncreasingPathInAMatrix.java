package com.example.ds.graph;

public class LongestIncreasingPathInAMatrix {
    int[][] dir = {{0,1}, {0,-1}, {-1,0}, {1,0}};
    int m,n;
    int[][] memoization;

    public int longestIncreasingPath(int[][] matrix) {
        if(matrix.length==0) return 0;
        m = matrix.length;
        n = matrix[0].length;
        memoization = new int[m][n];

        int maxPath = 0;
        for(int i=0; i<m; i++) {
            for(int j=0; j<n; j++) {
                maxPath = Math.max(maxPath, dfs(matrix, i, j));
            }
        }
        return maxPath;
    }

    int dfs(int[][] matrix, int i, int j) {
        if(memoization[i][j]!=0) return memoization[i][j];

        int max =1;
        for(int[] d: dir) {
            int x = i + d[0], y = j + d[1];

            if(x>=0 && x<matrix.length && y>=0 && y<matrix[0].length && matrix[x][y]>matrix[i][j]) {
                int length = 1 + dfs(matrix, x, y);
                max = Math.max(max, length);
            }
        }
        memoization[i][j] = max;
        return max;
    }

    public static void main(String[] args) {
        int[][] matrix = {
                {3,4,5},
                {3,2,6},
                {2,2,1}
        };
        LongestIncreasingPathInAMatrix sol = new LongestIncreasingPathInAMatrix();

        System.out.println(sol.longestIncreasingPath(matrix));
    }
}
