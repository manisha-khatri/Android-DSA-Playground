package com.example.ds.dp;

import java.util.Arrays;

public class Knapsack {

    static int[] val = {10,40,30,50};
    static int[] wt = {1,3,2,4};
    static int W = 5;

    static int knapsackMemo(int WeightTillNow, int i, int[][] memo) {
        // Base case
        if(val.length == i) {
            return 0;
        }

        // Cache
        if(memo[WeightTillNow][i] != -1) {
            return memo[WeightTillNow][i];
        }

        //Compute
        int notTake = knapsackMemo(WeightTillNow, i+1, memo);
        int take = Integer.MIN_VALUE;

        if(WeightTillNow - wt[i] >= 0 ) {
            take = val[i] + knapsackMemo(WeightTillNow - wt[i], i+1, memo);
        }

        //Save and return
        return memo[WeightTillNow][i] = Math.max(take,notTake);
    }

    static int knapsack(int W, int val[], int wt[]) {
        int memo[][] = new int[val.length+1][W+1];
        for (int[] row : memo) {
            Arrays.fill(row, -1);
        }

        return helper(W, val, wt, 0, memo);
    }

    static int helper(int W, int val[], int wt[], int i, int memo[][]) {
        //Base case
        if(i == val.length) return 0;

        //Cache
        if(memo[i][W]!=-1) {
            return memo[i][W];
        }

        //Computation
        int notTake = helper(W, val, wt, i+1, memo);
        int take = Integer.MIN_VALUE;

        if(W - wt[i] >= 0 && i < val.length) {
            take = val[i] + helper(W - wt[i], val, wt, i+1, memo);
        }

        //Save and return
        return memo[i][W] = Math.max(take, notTake);
    }


    public static void main(String[] args) {
        int[][] memo = new int[W + 1][wt.length + 1];
        for (int[] row : memo) {
            Arrays.fill(row, -1);
        }

        System.out.println(knapsackMemo(W, 0, memo));

        System.out.println("2nd = " + knapsack(W, val, wt));
    }
}
