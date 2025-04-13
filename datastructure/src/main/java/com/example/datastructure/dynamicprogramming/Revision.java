package com.example.datastructure.dynamicprogramming;

import java.util.Arrays;

public class Revision {

    int knapsack(int W, int[] val, int[] wt) {
        int memo[][] = new int[val.length+1][W+1];
        for(int row[]: memo) {
            Arrays.fill(row, -1);
        }
        return helper(W, val, wt, 0, memo);
    }

    int helper(int W, int val[], int wt[], int i, int[][] memo) {
        //Base
        if(i== val.length)
            return 0;

        //Cache
        if(memo[i][W]!=-1) {
            return memo[i][W];
        }

        //Compute
        int notTake = helper(W, val, wt, i+1, memo);
        int take=Integer.MIN_VALUE;
        if(i<wt.length && W-wt[i]>=0) {
            take = val[i] + helper(W-wt[i], val, wt, i+1, memo);
        }

        //Save and return
        return memo[i][W] = Math.max(take, notTake);
    }
}
