package com.example.ds.dp.pattern;

import java.util.Arrays;

public class IOKnapsack {
    public static void main(String[] args) {
        int[] values = {30, 40, 60};
        int[] wt = {3, 2, 5};
        int W = 6;

        int profit = knapsack(values, wt, W);
        System.out.print(profit);
    }

    static int knapsack(int[] values, int[] wt, int W) {
        int memo[][] = new int[wt.length][W+1];

        for(int[] row: memo) {
            Arrays.fill(row, -1);
        }

        return knapsack(wt.length-1, W, wt, values, memo);
    }

    static int knapsack(int i, int w, int[] wt, int[] val, int[][] memo) {
        // base-case
        if(i==0) {
            if(w-wt[0]>=0)
                return val[0];
            else
                return 0;
        }
        if(memo[i][w]!=-1) return memo[i][w];

        int notTake = knapsack(i-1, w, wt, val, memo);
        int take = Integer.MIN_VALUE;
        if(wt[i] <= w) {
            take = val[i] + knapsack(i-1, w-wt[i], wt, val, memo);
        }

        return memo[i][w] = Math.max(take, notTake);
    }
}
