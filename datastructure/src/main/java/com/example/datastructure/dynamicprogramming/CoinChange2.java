package com.example.datastructure.dynamicprogramming;

import java.util.Arrays;

public class CoinChange2 {
   static int coinChange2(int[] coins, int amount) {
       int[][] memo = new int[amount+1][coins.length];
       for(int[] row: memo) {
           Arrays.fill(row, -1);
       }
       return helper(coins, amount, memo, 0);
   }

   static int helper(int[] coins, int amt, int[][] memo, int i) {
      // Base
       if(amt == 0) return 1;

       if(i>= coins.length) return 0;

       // cache
       if(memo[amt][i]!=-1)
           return memo[amt][i];

       //Compute
       int notTake = helper(coins, amt, memo, i+1);
       int take=0;
       if(amt-coins[i] >=0) {
           take = helper(coins, amt-coins[i], memo, i);
       }

       //save and return
       return memo[amt][i] = (take + notTake);
   }


    public static void main(String[] args) {
        int[] coins = {1, 2, 5};
        int amount = 11;
        int result = coinChange2(coins, amount);

        System.out.println(result); // Output: 11
    }
}
