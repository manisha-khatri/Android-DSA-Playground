package com.example.ds.dp;

import static java.lang.Math.abs;

import java.util.Arrays;

public class FrogJump {

    static int[] memo;
    static int frogJumpI(int[] height) {
        int n = height.length;
        if(n==1) return 0;

        memo = new int[n];
        Arrays.fill(memo, -1);

        return jump(n-1, height);
    }

    static int jump(int i, int[] height) {
        if(i==0) return 0;

        if(memo[i]!=-1) return memo[i];

        int left = abs(height[i] - height[i-1]) + jump(i-1, height);
        int right = Integer.MAX_VALUE;
        if(i>1) {
            right = jump(i-2, height) + abs(height[i] - height[i-2]);
        }
        return memo[i] = Math.min(left, right);
    }

    static int frogJumpII(int[] height, int k) {
        int n = height.length;
        if(n==1) return 0;

        memo = new int[n];
        Arrays.fill(memo, -1);

        return jumpII(n-1, height, k);
    }

    static int jumpII(int i, int[] height, int k) {
        if(i==0) return 0;

        if(memo[i]!=-1) return memo[i];

        int minCost = Integer.MAX_VALUE;

        for(int j=1; j<=k; j++) {
            if(i-j>=0) {
                int curCost = jumpII(i-j, height, k) + Math.abs(height[i]-height[i-j]);
                minCost = Math.min(curCost, minCost);
            }
        }

        return memo[i] = minCost;
    }

    public static void main(String[] args) {
        int heights[] = {20, 30, 40, 20};

        System.out.println(frogJumpI(heights)); //20

        heights = new int[]{10, 5, 20, 0, 15};
        int k = 2;

        System.out.println(frogJumpII(heights, k)); //20
    }
}
