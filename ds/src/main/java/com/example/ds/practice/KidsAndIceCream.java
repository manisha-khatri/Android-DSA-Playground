package com.example.ds.practice;

import java.util.Arrays;

public class KidsAndIceCream {

    public static int findSmallestCoverageDistance2(int[] kidPositions, int[] sellerPositions) {
        int n = kidPositions.length;
        int m = sellerPositions.length;
        int sellerPos = 0;
        int max = Integer.MIN_VALUE;

        Arrays.sort(kidPositions);
        Arrays.sort(sellerPositions);

        for (int kidsPos : kidPositions) {
            while (sellerPos + 1 < m && sellerPositions[sellerPos + 1] <= kidsPos) {
                sellerPos++;
            }

            int leftSeller = Math.abs(kidsPos - sellerPositions[sellerPos]);
            int min = leftSeller;
            if (sellerPos + 1 < m) {
                int rightSeller = Math.abs(sellerPositions[sellerPos + 1] - kidsPos);
                min = Math.min(leftSeller, rightSeller);
            }

            max = Math.max(max, min);
        }

        return max;
    }

    //brute force
    public static int findSmallestCoverageDistance(int[] kidPositions, int[] sellerPositions) {
        int n = kidPositions.length;
        int m = sellerPositions.length;
        int max = 0;

        for(int i=0; i<n; i++) {
            int min = Integer.MAX_VALUE;
            int kid = kidPositions[i];

            for(int j=0; j<m; j++) {
                int seller = sellerPositions[j];
                int pos = Math.abs(kid - seller);
                min = Math.min(min, pos);
            }

            max = Math.max(max, min);
        }
        return max;
    }

    public static void main(String[] args) {
        int[] kids = {2, 10, 20};
        int[] sellers = {1, 15};

        System.out.println(findSmallestCoverageDistance2(kids, sellers));
    }
}
