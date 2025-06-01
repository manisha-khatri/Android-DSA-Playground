package com.example.datastructure.dp;

import java.util.Arrays;

public class CoinChange {
   static int coinChange(int[] coins, int amount) {
       int[] memo = new int[amount+1];
       Arrays.fill(memo, -1);
       return helper(coins, amount, memo);
   }

   static int helper(int[] coins, int amt, int[] memo) {
       if(amt == 0) {
           return 0;
       }
       if(amt < 0) {
           return Integer.MAX_VALUE;
       }

       if(memo[amt] != -1) {
           return memo[amt];
       }

       int minCoins = Integer.MAX_VALUE;

       for(int coin: coins) {
           int res = helper(coins, amt-coin, memo);
           if(res != Integer.MAX_VALUE) {
               minCoins = Math.min(minCoins, res+1);
           }
       }

       return memo[amt] = minCoins;
   }

    public static void main(String[] args) {
        int[] coins = {1, 2, 3};
        int amount = 4;
        int result = coinChange(coins, amount);

        System.out.println("and is = " + (result == Integer.MAX_VALUE ? -1 : result)); // Output: 3 (5+5+1)
    }
}
