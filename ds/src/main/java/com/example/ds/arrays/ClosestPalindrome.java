package com.example.ds.arrays;

import java.util.*;

public class ClosestPalindrome {

    static String closestPalindromeBruteForce(String n) {
        long num = Long.parseLong(n);

        for(int i=1; ; i++) {
            if(checkPalindrome(num-i)) {
                return String.valueOf(num-i);
            }
            if(checkPalindrome(num+i)) {
                return String.valueOf(num+i);
            }
        }
    }

    static boolean checkPalindrome(long num) {
        String x = String.valueOf(num);
        int right = x.length()-1;
        int left=0;

        while(left<right) {
            if(x.charAt(left) != x.charAt(right)) return false;
            left++;
            right--;
        }
        return true;
    }

    /**
     * Optimal
     */
    static public String nearestPalindromic(String n) {
        long num = Long.parseLong(n);
        int len = n.length();
        Set<Long> candidates = new HashSet<>();

        // Edge cases like 1000 -> 999, 999 -> 1001
        candidates.add((long)Math.pow(10, len) + 1);
        candidates.add((long)Math.pow(10, len - 1) - 1);

        long prefix = Long.parseLong(n.substring(0, (len + 1) / 2));

        for (long i = -1; i <= 1; i++) {
            long newPrefix = prefix + i;
            String prefixStr = String.valueOf(newPrefix);
            String palin;

            if (len % 2 == 0) {
                palin = prefixStr + new StringBuilder(prefixStr).reverse();
            } else {
                palin = prefixStr + new StringBuilder(prefixStr.substring(0, prefixStr.length() - 1)).reverse();
            }
            candidates.add(Long.parseLong(palin));
        }

        candidates.remove(num);

        long closest = -1;
        for (long cand : candidates) {
            if (closest == -1 || Math.abs(cand - num) < Math.abs(closest - num) ||
                    (Math.abs(cand - num) == Math.abs(closest - num) && cand < closest)) {
                closest = cand;
            }
        }

        return String.valueOf(closest);
    }

    public static void main(String[] args) {
        String n = "123";
        System.out.println(nearestPalindromic(n));
    }
}
