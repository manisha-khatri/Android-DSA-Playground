package com.example.ds.dp;

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

    static int maxVal = 1000000;

    static public int coinChangeK(int[] coins, int amount) {
        int n = coins.length;
        int memo[][] = new int[n][amount+1];
        int ans = coinChangeRec(n-1, amount, coins, memo);
        if(ans >= maxVal)
            return -1;
        else return ans;
    }

    static int coinChangeRec(int index, int target, int nums[], int memo[][]) {
        if(index == 0) {
            if(target % nums[0] == 0)
                return target/nums[0];
            return maxVal;
        }
        if(memo[index][target]!=0) return memo[index][target];
        int notTake = coinChangeRec(index-1, target, nums, memo);

        int take = maxVal;
        if(nums[index] <= target){
            take = 1 + coinChangeRec(index, target-nums[index], nums, memo);
        }
        return memo[index][target] = Math.min(take, notTake);
    }

    public static void main(String[] args) {
        int[] coins = {9,6,5,1};
        int amount = 11;
        int result = coinChangeK(coins, amount);

        System.out.println("and is = " + (result == Integer.MAX_VALUE ? -1 : result)); // Output: 3 (5+5+1)
    }
}
