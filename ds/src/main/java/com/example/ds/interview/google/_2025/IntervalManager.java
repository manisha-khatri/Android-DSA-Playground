package com.example.ds.interview.google._2025;

import java.util.*;

/**
 * Brute Force:
 * {(1,3), (2,4), (2,6), (8,9), (8,10), (9,11)}
 * insert(2,4) --> O(1)
 * lookup(5) --> O(n)
 *      1<5, 3<5
 *      2<5, 4<5
 *      2<5, 6>5 --> true
 *
 *
 * Optimal:
 * intervals = {(1,3)}
 * - insert(2,4) -->
 *  BinarySort gives position to insert (2,4) => i=1
 *
 *  - inclusive of boundaries
 *  (1,3)(2,4)
 *  3>=1 i-- = 0
 *  1<=5
 *  start = min(1,2) = 1
 *  end = max(3,4) = 4
 *  (1,4)
 *intervals = {(1,4)}
 *
 * lookup(3)
 * l=0 right=1
 * mid = l+(r-l)/2=0
 * cur = intervals.get(mid)
 * if(x>=cur[0] && x<=cur[1]) return true;
 * if(x<cur[0]) right=mid-1;
 * else left=mid+1;
 *
 */

public class IntervalManager {
    List<int[]> intervals = new ArrayList<>();

    int findIndex(int k) {
        int left=0, right=intervals.size()-1;

        while(left<=right) {
            int mid = left + (right-left)/2;
            int current = intervals.get(mid)[0];

            if(current==k) return mid;
            else if(current<k) {
                left= mid+1;
            } else {
                right = mid-1;
            }
        }
        return left;
    }

    void insert(int l, int r) {
        int i = findIndex(l);
        int start=l, end=r;

        while(i>0 && intervals.get(i-1)[1]>=start-1) {
            i--;
        }

        while(i<intervals.size() && intervals.get(i)[1]<=end+1) {
            start = Math.min(intervals.get(i)[0], start);
            end = Math.min(intervals.get(i)[1], end);
            intervals.remove(i);
        }

        intervals.add(new int[]{start, end});
    }

    boolean lookup(int x) {
        int left=0, right=intervals.size()-1;
        while(left<=right) {
            int mid = left+(right-left)/2;
            int[] current = intervals.get(mid);

            if(x>=current[0] && x<=current[1]) return true;
            else if(x>current[0]) {
                left=mid+1;
            } else {
                right=mid-1;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        IntervalManager manager = new IntervalManager();

        try {
            manager.insert(1, 3);
            manager.insert(2, 4);
            manager.insert(2, 6);
            manager.insert(8, 9);

            System.out.println(manager.lookup(5));
            System.out.println(manager.lookup(7));  //false
            System.out.println(manager.lookup(9));


            manager.insert(1, 3);
            manager.insert(5, 8);
            System.out.println(manager.lookup(2));  // true
            System.out.println(manager.lookup(4));  // false
            System.out.println(manager.lookup(11)); // false
            manager.insert(10, 12);
            System.out.println(manager.lookup(11)); // true

            // Let’s trigger a merge
            manager.insert(6, 10);
            System.out.println(manager.lookup(9));  // true
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}



