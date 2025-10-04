package com.example.ds.interview.zeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WarriorSuccess {

    static int bitCount(int x) {
        return Integer.bitCount(x);
    }

    static boolean checkAscending(int[] arr) {
        int n = arr.length;
        int i=0, j=0;
        List<Integer> res = new ArrayList<>();

        while(i<n) {
            j=i;

            while(j<n && bitCount(arr[i]) == bitCount(arr[j])) {
                j++;
            }

            int[] segment = new int[j-i];
            for(int k=i; k<j; k++) {
                segment[k-i] = arr[i];
            }

            for(int val: segment) {
                res.add(val);
            }
            Arrays.sort(segment);

            i=j;
        }

        for(int k=1; k<res.size(); k++) {
            if(res.get(k)<res.get(k-1)) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        int[] arr = {3,2,4,5};
        System.out.println(checkAscending(arr));
    }

}
