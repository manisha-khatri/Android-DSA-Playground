package com.example.ds.interview.google._2025;

import java.util.ArrayList;
import java.util.List;

/**
 *
 You are given a list of n intervals in the beginning and after that you are given Q operations.
 Now each operation could be of two types. Either it could be adding a new interval to the list of
 intervals or it could be like a point lookup. You have to, given a point, you have to tell whether
 that point lies in any of the intervals or not. So just to help, let I'm adding example 2 equal to
 n is the initial number of intervals. 1, 3, 5 are initial intervals. 5 equal to Q is the number of operations.
 Then let's say you got lookup 2. Then you got lookup 4. Then lookup let's say 11. Then insert 10, 12.
 Then lookup 11. So the output would be true, false, false, true. Let me know if anything is unclear. Okay, so

 Initial Intervals (n=2): [1, 3], [5, 8]
 Q = 5
 Operations:
 1. lookup 2	= true
 2. lookup 4 	= false
 3. lookup 11	= false
 4. insert 10 12
 5. lookup 11	= true

 TC
 Lookup --> O(n)
 Insert --> O(1)
 */


class IntervalLookupBruteForce {
    static class Interval {
        int start, end;

        Interval(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }
    static List<Interval> intervals = new ArrayList<>();
    static boolean lookup(int x) {
        for(Interval interval: intervals) {
            if(x>interval.start && x< interval.end) return true;
        }
        return false;
    }

    static void insert(int l, int r) {
        intervals.add(new Interval(l,r));
    }


    public static void main(String[] args) {
        insert(1,3);
        insert(5,8);

        System.out.println(lookup(2));
        System.out.println(lookup(4));
        System.out.println(lookup(11));
        System.out.println("inserted (10,12)");
        insert(10,12);
        System.out.println(lookup(11));

    }
}

// [1,10] [6,12] --> overlapping




