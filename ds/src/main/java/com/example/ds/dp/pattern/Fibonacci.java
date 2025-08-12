package com.example.ds.dp.pattern;

import java.util.HashMap;

public class Fibonacci {

    /**
     * TC: f(n) = f(n-1) + f(n-2)
     * = 2^n
     */
    static int fibonacciRec(int n) {
        if(n<=1)
            return n;

        return fibonacciRec(n-1) + fibonacciRec(n-2);
    }

    /**
     * Memoization (Top-Down Dynamic Programming)
     * <key, value> = <index, value>
     */
    static HashMap<Integer, Integer> memo = new HashMap<>();
    static int fibonacciRecOptimized(int n) {
        if(n<=1) return n;
        if(memo.containsKey(n)) return memo.get(n);

        int res = fibonacciRec(n-1) + fibonacciRec(n-2);
        memo.put(n, res);
        return res;
    }

    /**
     * Approach : Tabulation (Bottom-Up Dynamic Programming)
     * TC = O(n)
     */
    static int fibonacciItr(int n) {
        if(n<=1) return n;

        int[] arr = new int[n+1];
        arr[0] = 0;
        arr[1] = 1;

        for(int i=2; i<=n; i++) {
            arr[i] = arr[i-1] + arr[i-2];
        }

        return arr[n];
    }

    /**
     * TC = O(n)
     */
    static int fibonacciItrOptimized(int n) {
        if(n<=1) return n;
        int prev=0, cur=1, next;

        for(int i=2; i<=n; i++) {
            next = cur + prev;
            prev = cur;
            cur = next;
        }

        return cur;
    }

    public static void main(String[] args) {
        System.out.println("fibonacci = " + fibonacciRec(6));
        System.out.println("fibonacci = " + fibonacciRecOptimized(6));
        System.out.println("fibonacci = " + fibonacciItr(6));
        System.out.println("fibonacci = " + fibonacciItrOptimized(6));
    }
}
