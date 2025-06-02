package com.example.datastructure.dp;

import java.lang.reflect.Array;
import java.util.Arrays;

public class UniquePaths {
    public static void main(String[] args) {
        System.out.println(uniquePaths(3,2));
    }

    static public int uniquePaths(int m, int n) {
        int[][] memo = new int[m][n];
        for(int[] row: memo) {
            Arrays.fill(row, -1);
        }
        return uniquePaths(m-1, n-1, 0, 0, memo);
    }

    static public int uniquePaths(int m, int n, int mi, int ni, int[][] memo) {
        if(mi>m || ni>n) return 0;

        if(mi == m && ni == n) return 1;

        if(memo[mi][ni]!=-1) return memo[mi][ni];

        // Move Right
        int ways = uniquePaths(m, n, mi, ni+1, memo);

        // Move Down
        ways += uniquePaths(m, n, mi+1, ni, memo);

        return memo[mi][ni] = ways;
    }
}
